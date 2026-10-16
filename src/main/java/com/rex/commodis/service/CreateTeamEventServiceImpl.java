/** Service implementation for CreateTeamEvent business logic; called by the corresponding controller and delegates to the repository layer. */
//package com.rex.commodis.service;
//
//import com.rex.commodis.dto.events.CreateTeamEventRequestDto;
//import com.rex.commodis.dto.events.CreateTeamEventResponseDto;
//import com.rex.commodis.exceptions.NotFoundException;
//import com.rex.commodis.model.ProjectLeader;
//import com.rex.commodis.model.RexUser;
//import com.rex.commodis.model.enums.TeamEventStatusEnum;
//import com.rex.commodis.model.events.TeamEvent;
//import com.rex.commodis.model.events.TeamEventApprover.TeamEventApprover;
//import com.rex.commodis.model.events.TeamEventHistory;
//import com.rex.commodis.model.events.TeamEventStatus;
//import com.rex.commodis.repository.PicklistRepository;
//import com.rex.commodis.repository.ProjectLeaderRepository;
//import com.rex.commodis.repository.ProjectNumberRepository;
//import com.rex.commodis.repository.events.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.OffsetDateTime;
//import java.util.List;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class CreateTeamEventServiceImpl implements CreateTeamEventService {
//
//    private final TeamEventRepository teamEventRepository;
//    private final TeamEventHistoryRepository teamEventHistoryRepository;
//    private final TeamEventStatusRepository teamEventStatusRepository;
//    private final TeamEventTypeRepository teamEventTypeRepository;
//    private final ProjectNumberRepository projectNumberRepository;
//    private final PicklistRepository picklistRepository;
//    private final ProjectLeaderRepository projectLeaderRepository;
//    private final TeamEventApproverRepository teamEventApproverRepository;
//
//    @Override
//    public CreateTeamEventResponseDto createTeamEvent(
//            CreateTeamEventRequestDto requestDto,
//            Long rexUserId,
//            Long platformId
//    ) {
//        log.debug("Creating team event — rexUserId={}, platformId={}, eventTitle={}",
//                rexUserId, platformId, requestDto.getEventTitle());
//
//        // Validate projectnumber_id exists
//        validateProjectNumberExists(requestDto.getProjectnumberId());
//
//        // Validate teameventtype_id exists
//        validateTeamEventTypeExists(requestDto.getTeameventtypeId());
//
//        // Validate optional picklist_id only if provided
//        if (requestDto.getPicklistId() != null) {
//            validatePicklistExists(requestDto.getPicklistId());
//        }
//
//        // Resolve the SUBMITTED status entity (required for initial history entry)
//        TeamEventStatus submittedStatus = resolveStatus(TeamEventStatusEnum.SUBMITTED);
//
//        // Build and persist the TeamEvent
//        TeamEvent savedTeamEvent = buildAndSaveTeamEvent(requestDto, rexUserId);
//
//        // Seed the initial history row as SUBMITTED + active = true
//        seedInitialHistoryEntry(savedTeamEvent, submittedStatus, rexUserId);
//
//        seedApproverFromProjectLeaders(savedTeamEvent);
//
//        log.info("Team event created — teamEventId={}, rexUserId={}, platformId={}",
//                savedTeamEvent.getId(), rexUserId, platformId);
//
//        return CreateTeamEventResponseDto.builder()
//                .teamEventId(savedTeamEvent.getId())
//                .build();
//    }
//
//    // ═══════════════════════════════════════════════════════════════════
//    // Private helpers
//    // ═══════════════════════════════════════════════════════════════════
//
//    private void validateProjectNumberExists(Long projectnumberId) {
//        if (!projectNumberRepository.existsById(projectnumberId)) {
//            throw new NotFoundException(
//                    "Project number not found with ID: " + projectnumberId
//            );
//        }
//    }
//
//    private void validateTeamEventTypeExists(Long teameventtypeId) {
//        if (!teamEventTypeRepository.existsById(teameventtypeId)) {
//            throw new NotFoundException(
//                    "Team event type not found with ID: " + teameventtypeId
//            );
//        }
//    }
//
//    private void validatePicklistExists(Long picklistId) {
//        if (!picklistRepository.existsById(picklistId)) {
//            throw new NotFoundException(
//                    "Picklist not found with ID: " + picklistId
//            );
//        }
//    }
//
//    private TeamEventStatus resolveStatus(TeamEventStatusEnum statusEnum) {
//        return teamEventStatusRepository
//                .findByStatusCode(statusEnum)
//                .orElseThrow(() -> new NotFoundException(
//                        "Status '" + statusEnum.name() + "' is not configured in the database. " +
//                                "Please contact your administrator."
//                ));
//    }
//
//    private TeamEvent buildAndSaveTeamEvent(CreateTeamEventRequestDto dto, Long rexUserId) {
//        TeamEvent teamEvent = new TeamEvent();
//
//        teamEvent.setEventTitle(dto.getEventTitle());
//        teamEvent.setEventDate(dto.getEventDate());
//        teamEvent.setProjectnumberId(dto.getProjectnumberId());
//        teamEvent.setTeamEventTypeId(dto.getTeameventtypeId());
//        teamEvent.setBookableThrough(dto.getBookableThrough());
//        teamEvent.setBookableBy(dto.getBookableBy());
//        teamEvent.setPicklistId(dto.getPicklistId());
//        teamEvent.setRexuserId(rexUserId);
//        // createdAt / updatedAt set automatically via @CreationTimestamp / @UpdateTimestamp
//        return teamEventRepository.save(teamEvent);
//    }
//
//    private void seedInitialHistoryEntry(
//            TeamEvent savedTeamEvent,
//            TeamEventStatus status,
//            Long rexUserId
//    ) {
//        TeamEventHistory initialHistory = new TeamEventHistory();
//        initialHistory.setTeamEvent(savedTeamEvent);
//        initialHistory.setStatus(status);
//        initialHistory.setCreator(buildUserRef(rexUserId));
//        initialHistory.setActive(true);
//        initialHistory.setExternalNote(null);
//        initialHistory.setCreatedAt(OffsetDateTime.now());
//
//        teamEventHistoryRepository.save(initialHistory);
//
//        log.debug("Initial history entry seeded — teamEventId={}, status=SUBMITTED",
//                savedTeamEvent.getId());
//    }
//
//
//    private void seedApproverFromProjectLeaders(TeamEvent savedEvent) {
//
//        Long projectnumberId = savedEvent.getProjectnumberId();
//
//        List<ProjectLeader> leaders =
//                projectLeaderRepository.findByProjectNumberId(projectnumberId);
//
//        if (leaders.isEmpty()) {
//
//            log.warn(
//                    "No project leaders found for projectnumberId={}",
//                    projectnumberId
//            );
//
//            return;
//        }
//
//        List<TeamEventApprover> approvers = leaders.stream()
//                .map(leader -> {
//
//                    RexUser rexUser = new RexUser();
//                    rexUser.setId(leader.getRexUserId());
//
//                    TeamEventApprover approver = new TeamEventApprover();
//
//                    approver.setTeamEvent(savedEvent);
//                    approver.setRexUser(rexUser);
//                    approver.setCreatedAt(OffsetDateTime.now());
//
//                    return approver;
//                })
//                .toList();
//
//        teamEventApproverRepository.saveAll(approvers);
//
//        log.debug(
//                "Approvers seeded — teamEventId={}, approverCount={}",
//                savedEvent.getId(),
//                approvers.size()
//        );
//    }
//
//    /**
//     * Returns a proxy reference to RexUser by ID — avoids a redundant SELECT
//     * since creator_id FK only needs the ID for the INSERT.
//     */
//    private RexUser buildUserRef(Long rexUserId) {
//        RexUser ref = new RexUser();
//        ref.setId(rexUserId);
//        return ref;
//    }
//}