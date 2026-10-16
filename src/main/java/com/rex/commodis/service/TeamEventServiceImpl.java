/** Service implementation for TeamEvent business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

import com.rex.commodis.dto.events.*;
import com.rex.commodis.mapper.MySubmittedTeamEventGetListProjection;
import com.rex.commodis.model.RexUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rex.commodis.dto.ListResponseDto;
import com.rex.commodis.dto.TeamEventGetListDto;
import com.rex.commodis.dto.TeamEventGetListRequestDto;
import com.rex.commodis.dto.events.CreateTeamEventRequestDto;
import com.rex.commodis.dto.events.TeamEventResponseDto;
import com.rex.commodis.dto.events.TeamEventDetailsHeaderResponseDto;
import com.rex.commodis.dto.events.TeamEventDetailsResponseDto;
import com.rex.commodis.dto.events.TeamEventNotesRequestDto;
import com.rex.commodis.dto.events.TeamEventNotesResponseDto;
import com.rex.commodis.dto.events.TeamEventReceiptDto;
import com.rex.commodis.dto.receipts.BookableEventDto;
import com.rex.commodis.dto.receipts.ReceiptEventRequestDto;
import com.rex.commodis.dto.receipts.ReceiptFileResponseDto;
import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.exceptions.ForbiddenException;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.mapper.TeamEventGetListProjection;
import com.rex.commodis.model.ProjectLeader;
import com.rex.commodis.model.enums.BookableByTypeEnum;
import com.rex.commodis.model.enums.TeamEventSortBy;
import com.rex.commodis.model.enums.TeamEventStatusEnum;
import com.rex.commodis.model.events.TeamEvent;
import com.rex.commodis.model.events.TeamEventHistory;
import com.rex.commodis.model.events.TeamEventApprover.TeamEventApprover;
import com.rex.commodis.repository.ProjectLeaderRepository;
import com.rex.commodis.repository.events.TeamEventApproverRepository;
import com.rex.commodis.repository.events.TeamEventDetailsProjection;
import com.rex.commodis.repository.events.TeamEventHistoryRepository;
import com.rex.commodis.repository.events.TeamEventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TeamEventServiceImpl implements TeamEventService {

    private final TeamEventRepository teamEventRepository;
    private final TeamEventApproverRepository teamEventApproverRepository;
    private final TeamEventHistoryRepository teamEventHistoryRepository;
    private final ProjectLeaderRepository projectLeaderRepository;
    private final RexUserService rexUserService;
    private final TeamEventMailService teamEventMailService;

    public TeamEvent getTeamEvent(ReceiptEventRequestDto eventDto) {
        return teamEventRepository
                .findById(eventDto.getEventId())
                .orElseThrow();
    }

    @Override
    public ListResponseDto<TeamEventGetListDto, TeamEventGetListRequestDto> getTeamEvents(
            Long rexUserId,
            TeamEventStatusEnum status,
            String eventTitle,
            String submittedBy,
            String projectNumber,
            LocalDate fromDate,
            LocalDate toDate,
            int pageNo,
            int pageSize,
            TeamEventSortBy sortBy,
            String sortOrder
    ) {

        String locale = rexUserService.getLocale(rexUserId);
        String resolvedSortBy = sortBy != null ?
                sortBy.getDbField() :
                TeamEventSortBy.EVENT_DATE.getDbField();

        Sort.Direction direction = resolveSortDirection(sortOrder);

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(direction, resolvedSortBy));


        Page<TeamEventGetListProjection> page;

        if (fromDate != null && toDate != null) {
            page = teamEventRepository.getTeamEventsWithFiltersAndDates(
                    rexUserId,
                    status,
                    normalize(eventTitle),
                    normalize(submittedBy),
                    normalize(projectNumber),
                    fromDate,
                    toDate,
                    locale,
                    pageable
            );
        } else {
            page = teamEventRepository.getTeamEventsWithFiltersNoDates(
                    rexUserId,
                    status,
                    normalize(eventTitle),
                    normalize(submittedBy),
                    normalize(projectNumber),
                    locale,
                    pageable
            );
        }

        return ListResponseDto.<TeamEventGetListDto, TeamEventGetListRequestDto>builder().meta(
                TeamEventGetListRequestDto.builder()
                        .rexUserId(rexUserId)
                        .status(status)
                        .fromDate(fromDate)
                        .toDate(toDate)
                        .eventTitle(eventTitle)
                        .submittedBy(submittedBy)
                        .projectNumber(projectNumber)
                        .pageNumber(pageNo)
                        .pageSize(pageSize)
                        .sortBy(sortBy != null ? sortBy : TeamEventSortBy.EVENT_DATE)
                        .direction(direction.toString())
                        .build()
        ).data(page
                .getContent()
                .stream()
                .map(this::map)
                .toList()
        ).build();
    }

    @Override
    public List<BookableEventDto> getBookableEvents(Long rexUserId){

        return teamEventRepository.findBookableEvents(rexUserId)
                .stream()
                .map(event -> new BookableEventDto(
                        event.getEventSource(),
                        event.getEventId(),
                        event.getEventTitle(),
                        event.getInheritParticipants()
                ))
                .toList();
    }

    @Override
    public TeamEventNotesResponseDto updateTeamEventNotes(
            Long teamEventId,
            TeamEventNotesRequestDto requestDto,
            Long rexUserId
    ) {

        TeamEvent dbTeamEvent = teamEventRepository.findById(teamEventId)
                .orElseThrow(() -> new NotFoundException(
                        "Team event not found: " + teamEventId));

        boolean isApprover = teamEventApproverRepository
                .existsByTeamEventIdAndRexUserId(teamEventId, rexUserId);

        if (!isApprover) {
            throw new ForbiddenException("Not authorized to update team event notes");
        }

        dbTeamEvent.setInternalNote(requestDto.getInternalNote());

        return TeamEventNotesResponseDto.builder()
                .teamEventId(dbTeamEvent.getId())
                .internalNote(dbTeamEvent.getInternalNote())
                .build();
    }

    @Transactional(readOnly = true)
    public TeamEventDetailsResponseDto getTeamEventDetails(
            Long teamEventId,
            Long rexUserId
    ) {

        String locale = rexUserService.getLocale(rexUserId);
        List<TeamEventDetailsProjection> rows =
                teamEventRepository.getTeamEventDetails(
                        teamEventId,
                        rexUserId,
                        locale
                );

        if (rows.isEmpty()) {
            throw new BadRequestException(
                    "Team event not found or unauthorized access"
            );
        }

        TeamEventDetailsProjection first = rows.get(0);

        TeamEventDetailsHeaderResponseDto header =
                TeamEventDetailsHeaderResponseDto.builder()
                        .teamEventId(first.getTeamEventId())
                        .projectNumber(first.getProjectNumber())
                        .eventTitle(first.getEventTitle())
                        .eventDate(first.getEventDate())
                        .submittedBy(first.getSubmittedBy())
                        .internalNote(first.getInternalNote())
                        .teameventtypeId(first.getTeameventtypeId())
                        .bookableThrough(first.getBookableThrough())
                        .status(first.getStatus())
                        .projectNumberId(first.getProjectNumberId())
                        .picklistId(first.getPicklistId())
                        .picklistMemberCount(first.getPicklistMemberCount())
                        .build();

        Map<Long, TeamEventReceiptDto> receiptMap =
                new LinkedHashMap<>();

        for (TeamEventDetailsProjection row : rows) {

            if (row.getReceiptId() == null) {
                continue;
            }

            TeamEventReceiptDto dto =
                    receiptMap.computeIfAbsent(
                            row.getReceiptId(),
                            id -> TeamEventReceiptDto.builder()
                                    .receiptId(row.getReceiptId())
                                    .receiptTitle(row.getReceiptTitle())
                                    .receiptDate(row.getReceiptDate())
                                    .receiptNumber(row.getReceiptNumber())
                                    .submittedFor(row.getSubmittedFor())
                                    .receiptAmount(row.getReceiptAmount())
                                    .receiptCurrency(row.getReceiptCurrency())
                                    .gratuity(row.getGratuity())
                                    .amountNetFullVat(row.getAmountNetFullVat())
                                    .vatRateFull(row.getVatRateFull())
                                    .expenseCategory(row.getExpenseCategory())
                                    .expenseNature(row.getExpenseNature())
                                    .issuanceRegion(row.getIssuanceRegion())
                                    .deductTeamBalance(row.getDeductTeamBalance())
                                    .status(row.getReceiptStatus())
                                    .receiptFiles(new ArrayList<>())
                                    .build()
                    );

            if (row.getFileUrl() != null) {

                boolean alreadyExists =
                        dto.getReceiptFiles()
                                .stream()
                                .anyMatch(f ->
                                        f.getFileUrl()
                                                .equals(row.getFileUrl())
                                );

                if (!alreadyExists) {

                    dto.getReceiptFiles().add(
                            ReceiptFileResponseDto.builder()
                                    .sequence(row.getFileSequence())
                                    .fileUrl(row.getFileUrl())
                                    .build()
                    );
                }
            }
        }

        BigDecimal totalAmount =
                receiptMap.values()
                        .stream()
                        .map(TeamEventReceiptDto::getReceiptAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        return TeamEventDetailsResponseDto.builder()
                .teamEvent(header)
                .data(new ArrayList<>(receiptMap.values()))
                .totalAmount(totalAmount)
                .build();
    }

    @Override
    public TeamEventResponseDto createTeamEvent(
            CreateTeamEventRequestDto requestDto,
            Long rexUserId,
            Long platformId
    ) {
        validateBookableThrough(
                requestDto.getEventDate(),
                requestDto.getBookableThrough()
        );
        //Persist team_event data
        Long teamEventId = persistTeamEvent(requestDto, rexUserId, platformId);

        //Create initial team_event_history entry
        createInitialHistoryEntry(teamEventId, rexUserId, (short) 1);

        //Assign approvers for team_event
        assignApproverForTeamEvent(teamEventId, requestDto.getProjectNumberId());

        return TeamEventResponseDto.builder()
                .teamEventId(teamEventId)
                .build();
    }

    //HELPER METHODS

    private TeamEventGetListDto map(TeamEventGetListProjection p) {
        return TeamEventGetListDto.builder()
                .teamEventId(p.getTeamEventId())
                .status(p.getStatus())
                .eventDate(p.getEventDate())
                .eventTitle(p.getEventTitle())
                .submittedBy(p.getSubmittedBy())
                .projectNumber(p.getProjectNumber())
                .totalAmount(p.getTotalAmount())
                .build();
    }

    private String normalize(String v) {
        return (v == null || v.isBlank())
                ? null
                : v.trim();
    }

    private Long persistTeamEvent(CreateTeamEventRequestDto dto, Long rexUserId, Long platformId) {
        TeamEvent teamEvent = new TeamEvent();

        teamEvent.setPlatformId(platformId);
        teamEvent.setRexuserId(rexUserId);
        teamEvent.setEventTitle(dto.getEventTitle());
        teamEvent.setEventDate(dto.getEventDate());
        teamEvent.setProjectnumberId(dto.getProjectNumberId());
        teamEvent.setTeamEventTypeId(dto.getTeamEventTypeId());
        teamEvent.setBookableThrough(dto.getBookableThrough());
        Long picklistId = Long.valueOf(0L).equals(dto.getPicklistId()) ? null : dto.getPicklistId();
        teamEvent.setPicklistId(picklistId);

        if (picklistId == null) {
            teamEvent.setBookableBy(BookableByTypeEnum.OWNER.name());
        } else {
            teamEvent.setBookableBy(BookableByTypeEnum.PICKLIST.name());
        }

        TeamEvent dbTeamEvent = teamEventRepository.save(teamEvent);

        return dbTeamEvent.getId();
    }

    private void createInitialHistoryEntry(
            Long teamEventId,
            Long rexUserId,
            Short statusId
    ) {
        TeamEventHistory teamEventHistory = new TeamEventHistory();

        teamEventHistory.setCreatedAt(OffsetDateTime.now());
        teamEventHistory.setTeamEventId(teamEventId);
        teamEventHistory.setCreatorId(rexUserId);
        teamEventHistory.setStatusId(statusId);
        teamEventHistory.setActive(true);

        teamEventHistoryRepository.save(teamEventHistory);
    }

    private void assignApproverForTeamEvent(Long teamEventId, Long projectNumberId) {

        List<ProjectLeader> leaders =
                projectLeaderRepository.findByProjectNumberId(projectNumberId);

        List<TeamEventApprover> teamEventApprovers = leaders.stream()
                .map(leader -> {

                    TeamEventApprover teamEventApprover = new TeamEventApprover();

                    teamEventApprover.setTeamEventId(teamEventId);
                    teamEventApprover.setRexUserId(leader.getRexUserId());
                    teamEventApprover.setCreatedAt(OffsetDateTime.now());

                    return teamEventApprover;
                })
                .toList();

        teamEventApproverRepository.saveAll(teamEventApprovers);
    }

    private Sort.Direction resolveSortDirection(String sortOrder) {
        if (sortOrder == null || sortOrder.isBlank())
            return Sort.Direction.DESC;

        if (!"ASC".equals(sortOrder) && !"DESC".equals(sortOrder)) {
            throw new BadRequestException(
                    "Invalid sort_order: '" + sortOrder + "'. " +
                            "Only 'ASC' or 'DESC' (uppercase) are accepted."
            );
        }

        return Sort.Direction.valueOf(sortOrder);
    }

	@Override
	public boolean existsByProjectNumber(Long projectNumberId) {
		return teamEventRepository.existsByProjectNumberId(projectNumberId);
	}

    @Override
    public void processAndReleasePastTeamEvents() {

        List<TeamEvent> teamEventsToRelease = teamEventRepository.findEventsToRelease();

        if (teamEventsToRelease.isEmpty()) {
            log.info("No team events found for processing release.");
            return;
        }

        log.info("Found {} team event(s) to process.", teamEventsToRelease.size());

        for (TeamEvent teamEvent : teamEventsToRelease) {

            teamEvent.setProcessingReleasedAt(OffsetDateTime.now());
            teamEvent.setProcessingReleasedBy("SYSTEM");
            teamEventRepository.save(teamEvent);

            java.util.Set<TeamEventApprover> approvers = teamEvent.getTeamEventApprovers();

            if (approvers != null) {
                for (TeamEventApprover approver : approvers) {

                    RexUser rexUser = approver.getRexUser();

                    if (rexUser.getEmail() == null) {
                        log.info(
                                "Skipping email notification for teamEventId={} because approver email is missing.",
                                teamEvent.getId()
                        );
                        continue;
                    }

                    teamEventMailService.sendTeamEventReadyForApprovalMail(
                            rexUser.getEmail(),
                            rexUser.getFullName(),
                            teamEvent.getEventTitle(),
                            teamEvent.getEventDate()
                    );
                }
            }
        }
    }

    @Override
    public EditTeamEventResponseDto editTeamEvent(
            Long teamEventId,
            EditTeamEventRequestDto requestDto,
            Long rexUserId
    ) {

        TeamEvent dbTeamEvent = teamEventRepository
                .findById(teamEventId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Team event not found: "
                                        + teamEventId
                        )
                );

        if (!Objects.equals(dbTeamEvent.getRexuserId(), rexUserId)) {
            throw new ForbiddenException(
                    "Not authorized to edit this team event"
            );
        }

        if (requestDto.getBookableThrough() != null) {
            dbTeamEvent.setBookableThrough(
                    requestDto.getBookableThrough()
            );
        }

        if (requestDto.getTeameventtypeId() != null) {
            dbTeamEvent.setTeamEventTypeId(
                    requestDto.getTeameventtypeId()
            );
        }

        Long editPicklistId = Long.valueOf(0L).equals(requestDto.getPicklistId()) ? null : requestDto.getPicklistId();
        if (editPicklistId != null) {
            dbTeamEvent.setPicklistId(editPicklistId);
            dbTeamEvent.setBookableBy(BookableByTypeEnum.PICKLIST.name());
        }

        dbTeamEvent = teamEventRepository.save(dbTeamEvent);

        return EditTeamEventResponseDto.builder()
                .teamEventId(dbTeamEvent.getId())
                .bookableThrough(dbTeamEvent.getBookableThrough())
                .teameventtypeId(dbTeamEvent.getTeamEventTypeId())
                .picklistId(dbTeamEvent.getPicklistId())
                .build();

    }

    @Override
    public TeamEventResponseDto closeTeamEvent(Long teamEventId, Long rexUserId){
        TeamEvent dbTeamEvent = teamEventRepository.findById(teamEventId)
                .orElseThrow(
                        () -> new NotFoundException("Team Event Not Found")
                );

        if (!Objects.equals(dbTeamEvent.getRexuserId(), rexUserId)) {
            throw new ForbiddenException(
                    "Not authorized to close this team event"
            );
        }
        dbTeamEvent.setProcessingReleasedAt(OffsetDateTime.now());
        dbTeamEvent.setProcessingReleasedBy(rexUserService.getById(rexUserId).getFullName());

        teamEventRepository.save(dbTeamEvent);

        //Send Email to projectLeader
//        teamEventMailService.sendTeamEventClosedMail();

        return TeamEventResponseDto.builder()
                .teamEventId(teamEventId)
                .build();
    }


    private void validateBookableThrough(
            LocalDate eventDate,
            LocalDate bookableThrough
    ) {

        if (!bookableThrough.isAfter(eventDate)) {
            throw new BadRequestException(
                    "bookable_through must be greater than event_date"
            );
        }

        if (bookableThrough.isBefore(LocalDate.now())) {
            throw new BadRequestException(
                    "bookable_through cannot be in the past"
            );
        }
	}

    @Override
    public ListResponseDto<MySubmittedTeamEventGetListDto,MySubmittedTeamEventGetListRequestDto> getMySubmittedTeamEvents(
            Long rexUserId,
            TeamEventStatusEnum status,
            String eventTitle,
            String projectNumber,
            LocalDate fromDate,
            LocalDate toDate,
            int pageNo,
            int pageSize,
            TeamEventSortBy sortBy,
            String sortOrder
    ) {

        String locale = rexUserService.getLocale(rexUserId);

        String resolvedSortBy = sortBy != null
                ? sortBy.getDbField()
                : TeamEventSortBy.EVENT_DATE.getDbField();

        Sort.Direction direction =
                resolveSortDirection(sortOrder);

        Pageable pageable = PageRequest.of(
                pageNo - 1,
                pageSize,
                Sort.by(direction, resolvedSortBy)
        );

        Page<MySubmittedTeamEventGetListProjection> page;

        if (fromDate != null && toDate != null) {

            page = teamEventRepository
                    .getMySubmittedTeamEventsWithFiltersAndDates(
                            rexUserId,
                            status,
                            normalize(eventTitle),
                            normalize(projectNumber),
                            fromDate,
                            toDate,
                            locale,
                            pageable
                    );

        } else {

            page = teamEventRepository
                    .getMySubmittedTeamEventsWithFiltersNoDates(
                            rexUserId,
                            status,
                            normalize(eventTitle),
                            normalize(projectNumber),
                            locale,
                            pageable
                    );
        }

        return ListResponseDto.<MySubmittedTeamEventGetListDto,MySubmittedTeamEventGetListRequestDto>
                        builder().meta(
                        MySubmittedTeamEventGetListRequestDto
                                .builder()
                                .rexUserId(rexUserId)
                                .status(status)
                                .fromDate(fromDate)
                                .toDate(toDate)
                                .eventTitle(eventTitle)
                                .projectNumber(projectNumber)
                                .pageNumber(pageNo)
                                .pageSize(pageSize)
                                .sortBy(
                                        sortBy != null
                                                ? sortBy
                                                : TeamEventSortBy.EVENT_DATE
                                )
                                .direction(direction.toString())
                                .build()
                )

                .data(
                        page.getContent()
                                .stream()
                                .map(this::mapMySubmittedTeamEvents)
                                .toList()
                )
                .build();
    }

    private MySubmittedTeamEventGetListDto mapMySubmittedTeamEvents(
            MySubmittedTeamEventGetListProjection p
    ) {

        return MySubmittedTeamEventGetListDto.builder()
                .teamEventId(p.getTeamEventId())
                .status(p.getStatus())
                .eventDate(p.getEventDate())
                .eventTitle(p.getEventTitle())
                .projectNumber(p.getProjectNumber())
                .totalAmount(p.getTotalAmount())
                .build();
    }
}
