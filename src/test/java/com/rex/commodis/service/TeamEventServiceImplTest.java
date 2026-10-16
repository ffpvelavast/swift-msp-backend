package com.rex.commodis.service;

import com.rex.commodis.dto.ListResponseDto;
import com.rex.commodis.dto.TeamEventGetListDto;
import com.rex.commodis.dto.TeamEventGetListRequestDto;
import com.rex.commodis.dto.events.CreateTeamEventRequestDto;
import com.rex.commodis.dto.events.EditTeamEventRequestDto;
import com.rex.commodis.dto.events.EditTeamEventResponseDto;
import com.rex.commodis.dto.events.MySubmittedTeamEventGetListDto;
import com.rex.commodis.dto.events.MySubmittedTeamEventGetListRequestDto;
import com.rex.commodis.dto.events.TeamEventResponseDto;
import com.rex.commodis.dto.events.TeamEventNotesRequestDto;
import com.rex.commodis.dto.events.TeamEventNotesResponseDto;
import com.rex.commodis.dto.events.TeamEventDetailsResponseDto;
import com.rex.commodis.dto.events.TeamEventReceiptDto;
import com.rex.commodis.dto.receipts.ReceiptFileResponseDto;
import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.exceptions.ForbiddenException;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.mapper.MySubmittedTeamEventGetListProjection;
import com.rex.commodis.mapper.TeamEventGetListProjection;
import com.rex.commodis.model.ProjectLeader;
import com.rex.commodis.model.RexUser;
import com.rex.commodis.model.enums.BookableByTypeEnum;
import com.rex.commodis.model.enums.TeamEventSortBy;
import com.rex.commodis.model.enums.TeamEventStatusEnum;
import com.rex.commodis.model.events.TeamEvent;
import com.rex.commodis.model.events.TeamEventApprover.TeamEventApprover;
import com.rex.commodis.model.events.TeamEventHistory;
import com.rex.commodis.repository.ProjectLeaderRepository;
import com.rex.commodis.repository.events.TeamEventApproverRepository;
import com.rex.commodis.repository.events.TeamEventDetailsProjection;
import com.rex.commodis.repository.events.TeamEventHistoryRepository;
import com.rex.commodis.repository.events.TeamEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamEventServiceImplTest {

    @Mock private TeamEventRepository teamEventRepository;
    @Mock private TeamEventApproverRepository teamEventApproverRepository;
    @Mock private TeamEventHistoryRepository teamEventHistoryRepository;
    @Mock private ProjectLeaderRepository projectLeaderRepository;
    @Mock private RexUserService rexUserService;
    @Mock private TeamEventMailService teamEventMailService;

    @InjectMocks
    private TeamEventServiceImpl teamEventService;

    private static final Long REX_USER_ID = 1L;
    private static final Long TEAM_EVENT_ID = 10L;
    private static final Long PLATFORM_ID = 20L;
    private static final Long PROJECT_NUMBER_ID = 30L;
    private static final String LOCALE = "en_US";

    @Test
    void getTeamEvents_withDates_normalizesFiltersDelegatesToDateQueryAndMapsResponse() {
        LocalDate fromDate = LocalDate.of(2024, 3, 1);
        LocalDate toDate = LocalDate.of(2024, 3, 31);
        TeamEventGetListProjection projection = teamEventProjection(
                10L,
                "Submitted",
                LocalDate.of(2024, 3, 15),
                "Quarterly dinner",
                "Jane Doe",
                "PN-100",
                new BigDecimal("300.25")
        );
        Page<TeamEventGetListProjection> page = new PageImpl<>(List.of(projection));

        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(teamEventRepository.getTeamEventsWithFiltersAndDates(
                eq(REX_USER_ID),
                eq(TeamEventStatusEnum.SUBMITTED),
                eq("Quarterly dinner"),
                eq("Jane Doe"),
                eq("PN-100"),
                eq(fromDate),
                eq(toDate),
                eq(LOCALE),
                org.mockito.ArgumentMatchers.any(Pageable.class)
        )).thenReturn(page);

        ListResponseDto<TeamEventGetListDto, TeamEventGetListRequestDto> response =
                teamEventService.getTeamEvents(
                        REX_USER_ID,
                        TeamEventStatusEnum.SUBMITTED,
                        "  Quarterly dinner  ",
                        "  Jane Doe  ",
                        "  PN-100  ",
                        fromDate,
                        toDate,
                        2,
                        20,
                        TeamEventSortBy.TOTAL_AMOUNT,
                        "ASC"
                );

        assertThat(response.getData()).hasSize(1);
        TeamEventGetListDto dto = response.getData().get(0);
        assertThat(dto.getTeamEventId()).isEqualTo(10L);
        assertThat(dto.getStatus()).isEqualTo("Submitted");
        assertThat(dto.getEventDate()).isEqualTo(LocalDate.of(2024, 3, 15));
        assertThat(dto.getEventTitle()).isEqualTo("Quarterly dinner");
        assertThat(dto.getSubmittedBy()).isEqualTo("Jane Doe");
        assertThat(dto.getProjectNumber()).isEqualTo("PN-100");
        assertThat(dto.getTotalAmount()).isEqualByComparingTo("300.25");

        assertThat(response.getMeta().getRexUserId()).isEqualTo(REX_USER_ID);
        assertThat(response.getMeta().getStatus()).isEqualTo(TeamEventStatusEnum.SUBMITTED);
        assertThat(response.getMeta().getEventTitle()).isEqualTo("  Quarterly dinner  ");
        assertThat(response.getMeta().getSubmittedBy()).isEqualTo("  Jane Doe  ");
        assertThat(response.getMeta().getProjectNumber()).isEqualTo("  PN-100  ");
        assertThat(response.getMeta().getFromDate()).isEqualTo(fromDate);
        assertThat(response.getMeta().getToDate()).isEqualTo(toDate);
        assertThat(response.getMeta().getPageNumber()).isEqualTo(2);
        assertThat(response.getMeta().getPageSize()).isEqualTo(20);
        assertThat(response.getMeta().getSortBy()).isEqualTo(TeamEventSortBy.TOTAL_AMOUNT);
        assertThat(response.getMeta().getDirection()).isEqualTo("ASC");

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(teamEventRepository).getTeamEventsWithFiltersAndDates(
                eq(REX_USER_ID),
                eq(TeamEventStatusEnum.SUBMITTED),
                eq("Quarterly dinner"),
                eq("Jane Doe"),
                eq("PN-100"),
                eq(fromDate),
                eq(toDate),
                eq(LOCALE),
                pageableCaptor.capture()
        );
        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getPageNumber()).isEqualTo(1);
        assertThat(pageable.getPageSize()).isEqualTo(20);
        assertThat(pageable.getSort().getOrderFor("totalAmount").getDirection().name()).isEqualTo("ASC");
    }

    @Test
    void getTeamEvents_withoutDates_usesNoDatesQueryAndDefaultSortDirection() {
        Page<TeamEventGetListProjection> page = new PageImpl<>(List.of());

        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(teamEventRepository.getTeamEventsWithFiltersNoDates(
                eq(REX_USER_ID),
                eq(null),
                eq(null),
                eq(null),
                eq(null),
                eq(LOCALE),
                org.mockito.ArgumentMatchers.any(Pageable.class)
        )).thenReturn(page);

        ListResponseDto<TeamEventGetListDto, TeamEventGetListRequestDto> response =
                teamEventService.getTeamEvents(
                        REX_USER_ID,
                        null,
                        "   ",
                        "",
                        null,
                        null,
                        null,
                        1,
                        10,
                        null,
                        null
                );

        assertThat(response.getData()).isEmpty();
        assertThat(response.getMeta().getStatus()).isNull();
        assertThat(response.getMeta().getEventTitle()).isEqualTo("   ");
        assertThat(response.getMeta().getSubmittedBy()).isEqualTo("");
        assertThat(response.getMeta().getProjectNumber()).isNull();
        assertThat(response.getMeta().getFromDate()).isNull();
        assertThat(response.getMeta().getToDate()).isNull();
        assertThat(response.getMeta().getPageNumber()).isEqualTo(1);
        assertThat(response.getMeta().getPageSize()).isEqualTo(10);
        assertThat(response.getMeta().getSortBy()).isEqualTo(TeamEventSortBy.EVENT_DATE);
        assertThat(response.getMeta().getDirection()).isEqualTo("DESC");

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(teamEventRepository).getTeamEventsWithFiltersNoDates(
                eq(REX_USER_ID),
                eq(null),
                eq(null),
                eq(null),
                eq(null),
                eq(LOCALE),
                pageableCaptor.capture()
        );
        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getPageNumber()).isZero();
        assertThat(pageable.getPageSize()).isEqualTo(10);
        assertThat(pageable.getSort().getOrderFor("eventDate").getDirection().name()).isEqualTo("DESC");
    }

    @Test
    void getTeamEvents_withOnlyFromDate_usesNoDatesQuery() {
        LocalDate fromDate = LocalDate.of(2024, 4, 1);
        Page<TeamEventGetListProjection> page = new PageImpl<>(List.of());

        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(teamEventRepository.getTeamEventsWithFiltersNoDates(
                eq(REX_USER_ID),
                eq(TeamEventStatusEnum.APPROVED),
                eq("Team lunch"),
                eq(null),
                eq(null),
                eq(LOCALE),
                org.mockito.ArgumentMatchers.any(Pageable.class)
        )).thenReturn(page);

        ListResponseDto<TeamEventGetListDto, TeamEventGetListRequestDto> response =
                teamEventService.getTeamEvents(
                        REX_USER_ID,
                        TeamEventStatusEnum.APPROVED,
                        "Team lunch",
                        null,
                        null,
                        fromDate,
                        null,
                        1,
                        5,
                        TeamEventSortBy.EVENT_TITLE,
                        "DESC"
                );

        assertThat(response.getData()).isEmpty();
        assertThat(response.getMeta().getFromDate()).isEqualTo(fromDate);
        assertThat(response.getMeta().getToDate()).isNull();

        verify(teamEventRepository).getTeamEventsWithFiltersNoDates(
                eq(REX_USER_ID),
                eq(TeamEventStatusEnum.APPROVED),
                eq("Team lunch"),
                eq(null),
                eq(null),
                eq(LOCALE),
                org.mockito.ArgumentMatchers.any(Pageable.class)
        );
    }

    @Test
    void getTeamEvents_withInvalidSortOrder_throwsBadRequestException() {
        assertThatThrownBy(() ->
                teamEventService.getTeamEvents(
                        REX_USER_ID,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        1,
                        10,
                        TeamEventSortBy.EVENT_DATE,
                        "asc"
                ))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid sort_order: 'asc'");

        verifyNoInteractions(teamEventRepository);
    }

    @Test
    void updateTeamEventNotes_whenCallerIsApprover_updatesInternalNoteAndReturnsResponse() {
        TeamEvent teamEvent = new TeamEvent();
        teamEvent.setId(TEAM_EVENT_ID);
        teamEvent.setInternalNote("old note");
        TeamEventNotesRequestDto request = new TeamEventNotesRequestDto();
        request.setInternalNote("Needs finance review");

        when(teamEventRepository.findById(TEAM_EVENT_ID)).thenReturn(Optional.of(teamEvent));
        when(teamEventApproverRepository.existsByTeamEventIdAndRexUserId(TEAM_EVENT_ID, REX_USER_ID))
                .thenReturn(true);

        TeamEventNotesResponseDto response =
                teamEventService.updateTeamEventNotes(TEAM_EVENT_ID, request, REX_USER_ID);

        assertThat(teamEvent.getInternalNote()).isEqualTo("Needs finance review");
        assertThat(response.getTeamEventId()).isEqualTo(TEAM_EVENT_ID);
        assertThat(response.getInternalNote()).isEqualTo("Needs finance review");
        verify(teamEventRepository).findById(TEAM_EVENT_ID);
        verify(teamEventApproverRepository).existsByTeamEventIdAndRexUserId(TEAM_EVENT_ID, REX_USER_ID);
    }

    @Test
    void updateTeamEventNotes_whenRequestNoteIsNull_clearsInternalNote() {
        TeamEvent teamEvent = new TeamEvent();
        teamEvent.setId(TEAM_EVENT_ID);
        teamEvent.setInternalNote("existing note");
        TeamEventNotesRequestDto request = new TeamEventNotesRequestDto();
        request.setInternalNote(null);

        when(teamEventRepository.findById(TEAM_EVENT_ID)).thenReturn(Optional.of(teamEvent));
        when(teamEventApproverRepository.existsByTeamEventIdAndRexUserId(TEAM_EVENT_ID, REX_USER_ID))
                .thenReturn(true);

        TeamEventNotesResponseDto response =
                teamEventService.updateTeamEventNotes(TEAM_EVENT_ID, request, REX_USER_ID);

        assertThat(teamEvent.getInternalNote()).isNull();
        assertThat(response.getTeamEventId()).isEqualTo(TEAM_EVENT_ID);
        assertThat(response.getInternalNote()).isNull();
    }

    @Test
    void updateTeamEventNotes_whenTeamEventDoesNotExist_throwsNotFoundException() {
        TeamEventNotesRequestDto request = new TeamEventNotesRequestDto();
        request.setInternalNote("new note");
        when(teamEventRepository.findById(TEAM_EVENT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                teamEventService.updateTeamEventNotes(TEAM_EVENT_ID, request, REX_USER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Team event not found: " + TEAM_EVENT_ID);

        verifyNoInteractions(teamEventApproverRepository);
    }

    @Test
    void updateTeamEventNotes_whenCallerIsNotApprover_throwsForbiddenException() {
        TeamEvent teamEvent = new TeamEvent();
        teamEvent.setId(TEAM_EVENT_ID);
        teamEvent.setInternalNote("unchanged note");
        TeamEventNotesRequestDto request = new TeamEventNotesRequestDto();
        request.setInternalNote("new note");

        when(teamEventRepository.findById(TEAM_EVENT_ID)).thenReturn(Optional.of(teamEvent));
        when(teamEventApproverRepository.existsByTeamEventIdAndRexUserId(TEAM_EVENT_ID, REX_USER_ID))
                .thenReturn(false);

        assertThatThrownBy(() ->
                teamEventService.updateTeamEventNotes(TEAM_EVENT_ID, request, REX_USER_ID))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("Not authorized to update team event notes");

        assertThat(teamEvent.getInternalNote()).isEqualTo("unchanged note");
    }

    @Test
    void getTeamEventDetails_mapsHeaderGroupsReceiptsDeduplicatesFilesAndTotalsUniqueReceipts() {
        TeamEventDetailsProjection firstFile = teamEventDetailsProjection(
                100L,
                "Taxi receipt",
                new BigDecimal("10.50"),
                1,
                "https://files.example/taxi.pdf"
        );
        TeamEventDetailsProjection duplicateFile = teamEventDetailsProjection(
                100L,
                "Taxi receipt",
                new BigDecimal("10.50"),
                1,
                "https://files.example/taxi.pdf"
        );
        TeamEventDetailsProjection secondReceipt = teamEventDetailsProjection(
                101L,
                "Dinner receipt",
                new BigDecimal("20.00"),
                2,
                "https://files.example/dinner.pdf"
        );

        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(teamEventRepository.getTeamEventDetails(TEAM_EVENT_ID, REX_USER_ID, LOCALE))
                .thenReturn(List.of(firstFile, duplicateFile, secondReceipt));

        TeamEventDetailsResponseDto response =
                teamEventService.getTeamEventDetails(TEAM_EVENT_ID, REX_USER_ID);

        assertThat(response.getTeamEvent().getTeamEventId()).isEqualTo(TEAM_EVENT_ID);
        assertThat(response.getTeamEvent().getProjectNumber()).isEqualTo("PN-100");
        assertThat(response.getTeamEvent().getEventTitle()).isEqualTo("Quarterly dinner");
        assertThat(response.getTeamEvent().getEventDate()).isEqualTo(LocalDate.of(2024, 3, 15));
        assertThat(response.getTeamEvent().getSubmittedBy()).isEqualTo("Jane Doe");
        assertThat(response.getTeamEvent().getInternalNote()).isEqualTo("Review receipts");
        assertThat(response.getTeamEvent().getTeameventtypeId()).isEqualTo(7L);
        assertThat(response.getTeamEvent().getBookableThrough()).isEqualTo(LocalDate.of(2024, 4, 15));
        assertThat(response.getTeamEvent().getStatus()).isEqualTo("Submitted");
        assertThat(response.getTeamEvent().getProjectNumberId()).isEqualTo(77L);
        assertThat(response.getTeamEvent().getPicklistId()).isEqualTo(88L);
        assertThat(response.getTeamEvent().getPicklistMemberCount()).isEqualTo(3L);

        assertThat(response.getData()).hasSize(2);
        TeamEventReceiptDto taxiReceipt = response.getData().get(0);
        assertThat(taxiReceipt.getReceiptId()).isEqualTo(100L);
        assertThat(taxiReceipt.getReceiptTitle()).isEqualTo("Taxi receipt");
        assertThat(taxiReceipt.getReceiptDate()).isEqualTo(LocalDate.of(2024, 3, 16));
        assertThat(taxiReceipt.getReceiptNumber()).isEqualTo("REC-100");
        assertThat(taxiReceipt.getSubmittedFor()).isEqualTo("John Smith");
        assertThat(taxiReceipt.getReceiptAmount()).isEqualByComparingTo("10.50");
        assertThat(taxiReceipt.getReceiptCurrency()).isEqualTo("EUR");
        assertThat(taxiReceipt.getGratuity()).isEqualByComparingTo("1.00");
        assertThat(taxiReceipt.getAmountNetFullVat()).isEqualByComparingTo("9.50");
        assertThat(taxiReceipt.getVatRateFull()).isEqualTo((short) 19);
        assertThat(taxiReceipt.getExpenseCategory()).isEqualTo("Travel");
        assertThat(taxiReceipt.getExpenseNature()).isEqualTo("Taxi");
        assertThat(taxiReceipt.getIssuanceRegion()).isEqualTo("DE");
        assertThat(taxiReceipt.getDeductTeamBalance()).isTrue();
        assertThat(taxiReceipt.getStatus()).isEqualTo("Submitted");
        assertThat(taxiReceipt.getReceiptFiles()).hasSize(1);
        ReceiptFileResponseDto taxiFile = taxiReceipt.getReceiptFiles().get(0);
        assertThat(taxiFile.getSequence()).isEqualTo(1);
        assertThat(taxiFile.getFileUrl()).isEqualTo("https://files.example/taxi.pdf");

        TeamEventReceiptDto dinnerReceipt = response.getData().get(1);
        assertThat(dinnerReceipt.getReceiptId()).isEqualTo(101L);
        assertThat(dinnerReceipt.getReceiptTitle()).isEqualTo("Dinner receipt");
        assertThat(dinnerReceipt.getReceiptFiles()).hasSize(1);
        assertThat(dinnerReceipt.getReceiptFiles().get(0).getFileUrl())
                .isEqualTo("https://files.example/dinner.pdf");
        assertThat(response.getTotalAmount()).isEqualByComparingTo("30.50");

        verify(rexUserService).getLocale(REX_USER_ID);
        verify(teamEventRepository).getTeamEventDetails(TEAM_EVENT_ID, REX_USER_ID, LOCALE);
    }

    @Test
    void getTeamEventDetails_whenRowsHaveNoReceipt_returnsHeaderWithEmptyDataAndZeroTotal() {
        TeamEventDetailsProjection headerOnly = teamEventDetailsProjection(
                null,
                null,
                null,
                null,
                null
        );

        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(teamEventRepository.getTeamEventDetails(TEAM_EVENT_ID, REX_USER_ID, LOCALE))
                .thenReturn(List.of(headerOnly));

        TeamEventDetailsResponseDto response =
                teamEventService.getTeamEventDetails(TEAM_EVENT_ID, REX_USER_ID);

        assertThat(response.getTeamEvent().getTeamEventId()).isEqualTo(TEAM_EVENT_ID);
        assertThat(response.getData()).isEmpty();
        assertThat(response.getTotalAmount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void getTeamEventDetails_whenRepositoryReturnsNoRows_throwsBadRequestException() {
        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(teamEventRepository.getTeamEventDetails(TEAM_EVENT_ID, REX_USER_ID, LOCALE))
                .thenReturn(List.of());

        assertThatThrownBy(() -> teamEventService.getTeamEventDetails(TEAM_EVENT_ID, REX_USER_ID))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Team event not found or unauthorized access");
    }

    @Test
    void createTeamEvent_withoutPicklist_savesOwnerEventCreatesInitialHistoryAssignsApproversAndReturnsId() {
        CreateTeamEventRequestDto request = createTeamEventRequest(null);
        when(teamEventRepository.save(org.mockito.ArgumentMatchers.any(TeamEvent.class)))
                .thenAnswer(invocation -> {
                    TeamEvent saved = invocation.getArgument(0);
                    saved.setId(TEAM_EVENT_ID);
                    return saved;
                });
        when(projectLeaderRepository.findByProjectNumberId(PROJECT_NUMBER_ID))
                .thenReturn(List.of(projectLeader(101L), projectLeader(102L)));

        TeamEventResponseDto response =
                teamEventService.createTeamEvent(request, REX_USER_ID, PLATFORM_ID);

        assertThat(response.getTeamEventId()).isEqualTo(TEAM_EVENT_ID);

        ArgumentCaptor<TeamEvent> teamEventCaptor = ArgumentCaptor.forClass(TeamEvent.class);
        verify(teamEventRepository).save(teamEventCaptor.capture());
        TeamEvent savedTeamEvent = teamEventCaptor.getValue();
        assertThat(savedTeamEvent.getPlatformId()).isEqualTo(PLATFORM_ID);
        assertThat(savedTeamEvent.getRexuserId()).isEqualTo(REX_USER_ID);
        assertThat(savedTeamEvent.getEventTitle()).isEqualTo("Quarterly dinner");
        assertThat(savedTeamEvent.getEventDate()).isEqualTo(request.getEventDate());
        assertThat(savedTeamEvent.getProjectnumberId()).isEqualTo(PROJECT_NUMBER_ID);
        assertThat(savedTeamEvent.getTeamEventTypeId()).isEqualTo(7L);
        assertThat(savedTeamEvent.getBookableThrough()).isEqualTo(request.getBookableThrough());
        assertThat(savedTeamEvent.getPicklistId()).isNull();
        assertThat(savedTeamEvent.getBookableBy()).isEqualTo(BookableByTypeEnum.OWNER.name());

        ArgumentCaptor<TeamEventHistory> historyCaptor = ArgumentCaptor.forClass(TeamEventHistory.class);
        verify(teamEventHistoryRepository).save(historyCaptor.capture());
        TeamEventHistory savedHistory = historyCaptor.getValue();
        assertThat(savedHistory.getTeamEventId()).isEqualTo(TEAM_EVENT_ID);
        assertThat(savedHistory.getCreatorId()).isEqualTo(REX_USER_ID);
        assertThat(savedHistory.getStatusId()).isEqualTo((short) 1);
        assertThat(savedHistory.getActive()).isTrue();
        assertThat(savedHistory.getCreatedAt()).isNotNull();

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<TeamEventApprover>> approversCaptor =
                ArgumentCaptor.forClass(List.class);
        verify(teamEventApproverRepository).saveAll(approversCaptor.capture());
        assertThat(approversCaptor.getValue()).hasSize(2);
        assertThat(approversCaptor.getValue())
                .extracting(TeamEventApprover::getTeamEventId)
                .containsOnly(TEAM_EVENT_ID);
        assertThat(approversCaptor.getValue())
                .extracting(TeamEventApprover::getRexUserId)
                .containsExactly(101L, 102L);
        assertThat(approversCaptor.getValue())
                .allSatisfy(approver -> assertThat(approver.getCreatedAt()).isNotNull());
    }

    @Test
    void createTeamEvent_withPicklist_savesPicklistEvent() {
        Long picklistId = 99L;
        CreateTeamEventRequestDto request = createTeamEventRequest(picklistId);
        when(teamEventRepository.save(org.mockito.ArgumentMatchers.any(TeamEvent.class)))
                .thenAnswer(invocation -> {
                    TeamEvent saved = invocation.getArgument(0);
                    saved.setId(TEAM_EVENT_ID);
                    return saved;
                });
        when(projectLeaderRepository.findByProjectNumberId(PROJECT_NUMBER_ID))
                .thenReturn(List.of());

        TeamEventResponseDto response =
                teamEventService.createTeamEvent(request, REX_USER_ID, PLATFORM_ID);

        assertThat(response.getTeamEventId()).isEqualTo(TEAM_EVENT_ID);
        ArgumentCaptor<TeamEvent> teamEventCaptor = ArgumentCaptor.forClass(TeamEvent.class);
        verify(teamEventRepository).save(teamEventCaptor.capture());
        assertThat(teamEventCaptor.getValue().getPicklistId()).isEqualTo(picklistId);
        assertThat(teamEventCaptor.getValue().getBookableBy()).isEqualTo(BookableByTypeEnum.PICKLIST.name());
        verify(teamEventApproverRepository).saveAll(List.of());
    }

    @Test
    void createTeamEvent_whenBookableThroughIsNotAfterEventDate_throwsBadRequestException() {
        CreateTeamEventRequestDto request = createTeamEventRequest(null);
        request.setBookableThrough(request.getEventDate());

        assertThatThrownBy(() -> teamEventService.createTeamEvent(request, REX_USER_ID, PLATFORM_ID))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("bookable_through must be greater than event_date");

        verifyNoInteractions(teamEventRepository, teamEventHistoryRepository,
                teamEventApproverRepository, projectLeaderRepository);
    }

    @Test
    void createTeamEvent_whenBookableThroughIsInPast_throwsBadRequestException() {
        CreateTeamEventRequestDto request = createTeamEventRequest(null);
        request.setEventDate(LocalDate.now().minusDays(2));
        request.setBookableThrough(LocalDate.now().minusDays(1));

        assertThatThrownBy(() -> teamEventService.createTeamEvent(request, REX_USER_ID, PLATFORM_ID))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("bookable_through cannot be in the past");

        verifyNoInteractions(teamEventRepository, teamEventHistoryRepository,
                teamEventApproverRepository, projectLeaderRepository);
    }

    @Test
    void editTeamEvent_withAllEditableFields_updatesEventSavesAndReturnsResponse() {
        TeamEvent teamEvent = editableTeamEvent();
        EditTeamEventRequestDto request = new EditTeamEventRequestDto(
                LocalDate.now().plusDays(20),
                9L,
                99L
        );
        when(teamEventRepository.findById(TEAM_EVENT_ID)).thenReturn(Optional.of(teamEvent));
        when(teamEventRepository.save(teamEvent)).thenReturn(teamEvent);

        EditTeamEventResponseDto response =
                teamEventService.editTeamEvent(TEAM_EVENT_ID, request, REX_USER_ID);

        assertThat(teamEvent.getBookableThrough()).isEqualTo(request.getBookableThrough());
        assertThat(teamEvent.getTeamEventTypeId()).isEqualTo(9L);
        assertThat(teamEvent.getPicklistId()).isEqualTo(99L);
        assertThat(teamEvent.getBookableBy()).isEqualTo(BookableByTypeEnum.PICKLIST.name());
        assertThat(response.getTeamEventId()).isEqualTo(TEAM_EVENT_ID);
        assertThat(response.getBookableThrough()).isEqualTo(request.getBookableThrough());
        assertThat(response.getTeameventtypeId()).isEqualTo(9L);
        assertThat(response.getPicklistId()).isEqualTo(99L);
        verify(teamEventRepository).save(teamEvent);
    }

    @Test
    void editTeamEvent_withNullFields_keepsExistingValuesAndSaves() {
        TeamEvent teamEvent = editableTeamEvent();
        LocalDate originalBookableThrough = teamEvent.getBookableThrough();
        EditTeamEventRequestDto request = new EditTeamEventRequestDto(null, null, null);

        when(teamEventRepository.findById(TEAM_EVENT_ID)).thenReturn(Optional.of(teamEvent));
        when(teamEventRepository.save(teamEvent)).thenReturn(teamEvent);

        EditTeamEventResponseDto response =
                teamEventService.editTeamEvent(TEAM_EVENT_ID, request, REX_USER_ID);

        assertThat(teamEvent.getBookableThrough()).isEqualTo(originalBookableThrough);
        assertThat(teamEvent.getTeamEventTypeId()).isEqualTo(7L);
        assertThat(teamEvent.getPicklistId()).isNull();
        assertThat(teamEvent.getBookableBy()).isEqualTo(BookableByTypeEnum.OWNER.name());
        assertThat(response.getBookableThrough()).isEqualTo(originalBookableThrough);
        assertThat(response.getTeameventtypeId()).isEqualTo(7L);
        assertThat(response.getPicklistId()).isNull();
        verify(teamEventRepository).save(teamEvent);
    }

    @Test
    void editTeamEvent_whenTeamEventDoesNotExist_throwsNotFoundException() {
        EditTeamEventRequestDto request = new EditTeamEventRequestDto(
                LocalDate.now().plusDays(10),
                9L,
                99L
        );
        when(teamEventRepository.findById(TEAM_EVENT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teamEventService.editTeamEvent(TEAM_EVENT_ID, request, REX_USER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Team event not found: " + TEAM_EVENT_ID);

        verify(teamEventRepository, org.mockito.Mockito.never())
                .save(org.mockito.ArgumentMatchers.any(TeamEvent.class));
    }

    @Test
    void editTeamEvent_whenCallerIsNotOwner_throwsForbiddenExceptionAndDoesNotSave() {
        TeamEvent teamEvent = editableTeamEvent();
        teamEvent.setRexuserId(999L);
        EditTeamEventRequestDto request = new EditTeamEventRequestDto(
                LocalDate.now().plusDays(10),
                9L,
                99L
        );
        when(teamEventRepository.findById(TEAM_EVENT_ID)).thenReturn(Optional.of(teamEvent));

        assertThatThrownBy(() -> teamEventService.editTeamEvent(TEAM_EVENT_ID, request, REX_USER_ID))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("Not authorized to edit this team event");

        verify(teamEventRepository, org.mockito.Mockito.never())
                .save(org.mockito.ArgumentMatchers.any(TeamEvent.class));
    }

    @Test
    void closeTeamEvent_whenCallerIsOwner_setsReleaseMetadataSavesAndReturnsResponse() {
        TeamEvent teamEvent = editableTeamEvent();
        RexUser rexUser = new RexUser();
        rexUser.setFullName("Jane Doe");

        when(teamEventRepository.findById(TEAM_EVENT_ID)).thenReturn(Optional.of(teamEvent));
        when(rexUserService.getById(REX_USER_ID)).thenReturn(rexUser);
        when(teamEventRepository.save(teamEvent)).thenReturn(teamEvent);

        TeamEventResponseDto response = teamEventService.closeTeamEvent(TEAM_EVENT_ID, REX_USER_ID);

        assertThat(response.getTeamEventId()).isEqualTo(TEAM_EVENT_ID);
        assertThat(teamEvent.getProcessingReleasedAt()).isNotNull();
        assertThat(teamEvent.getProcessingReleasedBy()).isEqualTo("Jane Doe");
        verify(teamEventRepository).save(teamEvent);
        verify(rexUserService).getById(REX_USER_ID);
    }

    @Test
    void closeTeamEvent_whenTeamEventDoesNotExist_throwsNotFoundException() {
        when(teamEventRepository.findById(TEAM_EVENT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teamEventService.closeTeamEvent(TEAM_EVENT_ID, REX_USER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Team Event Not Found");

        verify(teamEventRepository, org.mockito.Mockito.never())
                .save(org.mockito.ArgumentMatchers.any(TeamEvent.class));
        verifyNoInteractions(rexUserService);
    }

    @Test
    void closeTeamEvent_whenCallerIsNotOwner_throwsForbiddenExceptionAndDoesNotSave() {
        TeamEvent teamEvent = editableTeamEvent();
        teamEvent.setRexuserId(999L);
        when(teamEventRepository.findById(TEAM_EVENT_ID)).thenReturn(Optional.of(teamEvent));

        assertThatThrownBy(() -> teamEventService.closeTeamEvent(TEAM_EVENT_ID, REX_USER_ID))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("Not authorized to close this team event");

        assertThat(teamEvent.getProcessingReleasedAt()).isNull();
        assertThat(teamEvent.getProcessingReleasedBy()).isNull();
        verify(teamEventRepository, org.mockito.Mockito.never())
                .save(org.mockito.ArgumentMatchers.any(TeamEvent.class));
        verifyNoInteractions(rexUserService);
    }

    @Test
    void getMySubmittedTeamEvents_withDates_normalizesFiltersDelegatesToDateQueryAndMapsResponse() {
        LocalDate fromDate = LocalDate.of(2024, 5, 1);
        LocalDate toDate = LocalDate.of(2024, 5, 31);
        MySubmittedTeamEventGetListProjection projection = mySubmittedTeamEventProjection(
                40L,
                "Approved",
                LocalDate.of(2024, 5, 10),
                "Strategy offsite",
                "PN-500",
                new BigDecimal("450.00")
        );
        Page<MySubmittedTeamEventGetListProjection> page = new PageImpl<>(List.of(projection));

        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(teamEventRepository.getMySubmittedTeamEventsWithFiltersAndDates(
                eq(REX_USER_ID),
                eq(TeamEventStatusEnum.APPROVED),
                eq("Strategy offsite"),
                eq("PN-500"),
                eq(fromDate),
                eq(toDate),
                eq(LOCALE),
                org.mockito.ArgumentMatchers.any(Pageable.class)
        )).thenReturn(page);

        ListResponseDto<MySubmittedTeamEventGetListDto, MySubmittedTeamEventGetListRequestDto> response =
                teamEventService.getMySubmittedTeamEvents(
                        REX_USER_ID,
                        TeamEventStatusEnum.APPROVED,
                        "  Strategy offsite  ",
                        "  PN-500  ",
                        fromDate,
                        toDate,
                        2,
                        25,
                        TeamEventSortBy.TOTAL_AMOUNT,
                        "ASC"
                );

        assertThat(response.getData()).hasSize(1);
        MySubmittedTeamEventGetListDto dto = response.getData().get(0);
        assertThat(dto.getTeamEventId()).isEqualTo(40L);
        assertThat(dto.getStatus()).isEqualTo("Approved");
        assertThat(dto.getEventDate()).isEqualTo(LocalDate.of(2024, 5, 10));
        assertThat(dto.getEventTitle()).isEqualTo("Strategy offsite");
        assertThat(dto.getProjectNumber()).isEqualTo("PN-500");
        assertThat(dto.getTotalAmount()).isEqualByComparingTo("450.00");

        assertThat(response.getMeta().getRexUserId()).isEqualTo(REX_USER_ID);
        assertThat(response.getMeta().getStatus()).isEqualTo(TeamEventStatusEnum.APPROVED);
        assertThat(response.getMeta().getEventTitle()).isEqualTo("  Strategy offsite  ");
        assertThat(response.getMeta().getProjectNumber()).isEqualTo("  PN-500  ");
        assertThat(response.getMeta().getFromDate()).isEqualTo(fromDate);
        assertThat(response.getMeta().getToDate()).isEqualTo(toDate);
        assertThat(response.getMeta().getPageNumber()).isEqualTo(2);
        assertThat(response.getMeta().getPageSize()).isEqualTo(25);
        assertThat(response.getMeta().getSortBy()).isEqualTo(TeamEventSortBy.TOTAL_AMOUNT);
        assertThat(response.getMeta().getDirection()).isEqualTo("ASC");

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(teamEventRepository).getMySubmittedTeamEventsWithFiltersAndDates(
                eq(REX_USER_ID),
                eq(TeamEventStatusEnum.APPROVED),
                eq("Strategy offsite"),
                eq("PN-500"),
                eq(fromDate),
                eq(toDate),
                eq(LOCALE),
                pageableCaptor.capture()
        );
        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getPageNumber()).isEqualTo(1);
        assertThat(pageable.getPageSize()).isEqualTo(25);
        assertThat(pageable.getSort().getOrderFor("totalAmount").getDirection().name()).isEqualTo("ASC");
    }

    @Test
    void getMySubmittedTeamEvents_withoutDates_usesNoDatesQueryAndDefaultSortDirection() {
        Page<MySubmittedTeamEventGetListProjection> page = new PageImpl<>(List.of());

        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(teamEventRepository.getMySubmittedTeamEventsWithFiltersNoDates(
                eq(REX_USER_ID),
                eq(null),
                eq(null),
                eq(null),
                eq(LOCALE),
                org.mockito.ArgumentMatchers.any(Pageable.class)
        )).thenReturn(page);

        ListResponseDto<MySubmittedTeamEventGetListDto, MySubmittedTeamEventGetListRequestDto> response =
                teamEventService.getMySubmittedTeamEvents(
                        REX_USER_ID,
                        null,
                        "   ",
                        "",
                        null,
                        null,
                        1,
                        10,
                        null,
                        null
                );

        assertThat(response.getData()).isEmpty();
        assertThat(response.getMeta().getStatus()).isNull();
        assertThat(response.getMeta().getEventTitle()).isEqualTo("   ");
        assertThat(response.getMeta().getProjectNumber()).isEqualTo("");
        assertThat(response.getMeta().getFromDate()).isNull();
        assertThat(response.getMeta().getToDate()).isNull();
        assertThat(response.getMeta().getPageNumber()).isEqualTo(1);
        assertThat(response.getMeta().getPageSize()).isEqualTo(10);
        assertThat(response.getMeta().getSortBy()).isEqualTo(TeamEventSortBy.EVENT_DATE);
        assertThat(response.getMeta().getDirection()).isEqualTo("DESC");

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(teamEventRepository).getMySubmittedTeamEventsWithFiltersNoDates(
                eq(REX_USER_ID),
                eq(null),
                eq(null),
                eq(null),
                eq(LOCALE),
                pageableCaptor.capture()
        );
        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getPageNumber()).isZero();
        assertThat(pageable.getPageSize()).isEqualTo(10);
        assertThat(pageable.getSort().getOrderFor("eventDate").getDirection().name()).isEqualTo("DESC");
    }

    @Test
    void getMySubmittedTeamEvents_withOnlyFromDate_usesNoDatesQuery() {
        LocalDate fromDate = LocalDate.of(2024, 6, 1);
        Page<MySubmittedTeamEventGetListProjection> page = new PageImpl<>(List.of());

        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(teamEventRepository.getMySubmittedTeamEventsWithFiltersNoDates(
                eq(REX_USER_ID),
                eq(TeamEventStatusEnum.SUBMITTED),
                eq("Planning"),
                eq(null),
                eq(LOCALE),
                org.mockito.ArgumentMatchers.any(Pageable.class)
        )).thenReturn(page);

        ListResponseDto<MySubmittedTeamEventGetListDto, MySubmittedTeamEventGetListRequestDto> response =
                teamEventService.getMySubmittedTeamEvents(
                        REX_USER_ID,
                        TeamEventStatusEnum.SUBMITTED,
                        "Planning",
                        null,
                        fromDate,
                        null,
                        1,
                        5,
                        TeamEventSortBy.EVENT_TITLE,
                        "DESC"
                );

        assertThat(response.getData()).isEmpty();
        assertThat(response.getMeta().getFromDate()).isEqualTo(fromDate);
        assertThat(response.getMeta().getToDate()).isNull();

        verify(teamEventRepository).getMySubmittedTeamEventsWithFiltersNoDates(
                eq(REX_USER_ID),
                eq(TeamEventStatusEnum.SUBMITTED),
                eq("Planning"),
                eq(null),
                eq(LOCALE),
                org.mockito.ArgumentMatchers.any(Pageable.class)
        );
    }

    @Test
    void getMySubmittedTeamEvents_withInvalidSortOrder_throwsBadRequestException() {
        assertThatThrownBy(() ->
                teamEventService.getMySubmittedTeamEvents(
                        REX_USER_ID,
                        null,
                        null,
                        null,
                        null,
                        null,
                        1,
                        10,
                        TeamEventSortBy.EVENT_DATE,
                        "asc"
                ))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid sort_order: 'asc'");

        verifyNoInteractions(teamEventRepository);
    }

    @Test
    void processAndReleasePastTeamEvents_whenNoEventsFound_doesNotSaveOrSendMail() {
        when(teamEventRepository.findEventsToRelease()).thenReturn(List.of());

        teamEventService.processAndReleasePastTeamEvents();

        verify(teamEventRepository).findEventsToRelease();
        verify(teamEventRepository, org.mockito.Mockito.never())
                .save(org.mockito.ArgumentMatchers.any(TeamEvent.class));
        verifyNoInteractions(teamEventMailService);
    }

    @Test
    void processAndReleasePastTeamEvents_releasesEventSavesAndSendsMailToApproversWithEmail() {
        TeamEvent teamEvent = releasableTeamEvent();
        RexUser approverUser = new RexUser();
        approverUser.setEmail("approver@example.com");
        approverUser.setFullName("Approver One");
        TeamEventApprover approver = new TeamEventApprover();
        approver.setRexUser(approverUser);
        teamEvent.setTeamEventApprovers(Set.of(approver));
        when(teamEventRepository.findEventsToRelease()).thenReturn(List.of(teamEvent));

        teamEventService.processAndReleasePastTeamEvents();

        assertThat(teamEvent.getProcessingReleasedAt()).isNotNull();
        assertThat(teamEvent.getProcessingReleasedBy()).isEqualTo("SYSTEM");
        verify(teamEventRepository).save(teamEvent);
        verify(teamEventMailService).sendTeamEventReadyForApprovalMail(
                "approver@example.com",
                "Approver One",
                "Quarterly dinner",
                LocalDate.of(2024, 3, 15)
        );
    }

    @Test
    void processAndReleasePastTeamEvents_whenApproversAreNull_releasesAndSavesWithoutSendingMail() {
        TeamEvent teamEvent = releasableTeamEvent();
        teamEvent.setTeamEventApprovers(null);
        when(teamEventRepository.findEventsToRelease()).thenReturn(List.of(teamEvent));

        teamEventService.processAndReleasePastTeamEvents();

        assertThat(teamEvent.getProcessingReleasedAt()).isNotNull();
        assertThat(teamEvent.getProcessingReleasedBy()).isEqualTo("SYSTEM");
        verify(teamEventRepository).save(teamEvent);
        verifyNoInteractions(teamEventMailService);
    }

    @Test
    void processAndReleasePastTeamEvents_whenApproverEmailIsMissing_skipsMailForThatApprover() {
        TeamEvent teamEvent = releasableTeamEvent();
        RexUser approverUser = new RexUser();
        approverUser.setEmail(null);
        approverUser.setFullName("Approver Without Email");
        TeamEventApprover approver = new TeamEventApprover();
        approver.setRexUser(approverUser);
        teamEvent.setTeamEventApprovers(Set.of(approver));
        when(teamEventRepository.findEventsToRelease()).thenReturn(List.of(teamEvent));

        teamEventService.processAndReleasePastTeamEvents();

        assertThat(teamEvent.getProcessingReleasedAt()).isNotNull();
        assertThat(teamEvent.getProcessingReleasedBy()).isEqualTo("SYSTEM");
        verify(teamEventRepository).save(teamEvent);
        verifyNoInteractions(teamEventMailService);
    }

    private TeamEventGetListProjection teamEventProjection(
            Long teamEventId,
            String status,
            LocalDate eventDate,
            String eventTitle,
            String submittedBy,
            String projectNumber,
            BigDecimal totalAmount
    ) {
        return new TeamEventGetListProjection() {
            @Override
            public Long getTeamEventId() {
                return teamEventId;
            }

            @Override
            public String getStatus() {
                return status;
            }

            @Override
            public LocalDate getEventDate() {
                return eventDate;
            }

            @Override
            public String getEventTitle() {
                return eventTitle;
            }

            @Override
            public String getSubmittedBy() {
                return submittedBy;
            }

            @Override
            public String getProjectNumber() {
                return projectNumber;
            }

            @Override
            public BigDecimal getTotalAmount() {
                return totalAmount;
            }
        };
    }

    private MySubmittedTeamEventGetListProjection mySubmittedTeamEventProjection(
            Long teamEventId,
            String status,
            LocalDate eventDate,
            String eventTitle,
            String projectNumber,
            BigDecimal totalAmount
    ) {
        return new MySubmittedTeamEventGetListProjection() {
            @Override
            public Long getTeamEventId() {
                return teamEventId;
            }

            @Override
            public String getStatus() {
                return status;
            }

            @Override
            public LocalDate getEventDate() {
                return eventDate;
            }

            @Override
            public String getEventTitle() {
                return eventTitle;
            }

            @Override
            public String getProjectNumber() {
                return projectNumber;
            }

            @Override
            public BigDecimal getTotalAmount() {
                return totalAmount;
            }
        };
    }

    private CreateTeamEventRequestDto createTeamEventRequest(Long picklistId) {
        CreateTeamEventRequestDto request = new CreateTeamEventRequestDto();
        request.setEventTitle("Quarterly dinner");
        request.setEventDate(LocalDate.now().plusDays(5));
        request.setProjectNumberId(PROJECT_NUMBER_ID);
        request.setTeamEventTypeId(7L);
        request.setBookableThrough(LocalDate.now().plusDays(6));
        request.setPicklistId(picklistId);
        return request;
    }

    private ProjectLeader projectLeader(Long rexUserId) {
        ProjectLeader leader = new ProjectLeader();
        leader.setRexUserId(rexUserId);
        return leader;
    }

    private TeamEvent editableTeamEvent() {
        TeamEvent teamEvent = new TeamEvent();
        teamEvent.setId(TEAM_EVENT_ID);
        teamEvent.setRexuserId(REX_USER_ID);
        teamEvent.setBookableThrough(LocalDate.now().plusDays(5));
        teamEvent.setTeamEventTypeId(7L);
        teamEvent.setPicklistId(null);
        teamEvent.setBookableBy(BookableByTypeEnum.OWNER.name());
        return teamEvent;
    }

    private TeamEvent releasableTeamEvent() {
        TeamEvent teamEvent = new TeamEvent();
        teamEvent.setId(TEAM_EVENT_ID);
        teamEvent.setEventTitle("Quarterly dinner");
        teamEvent.setEventDate(LocalDate.of(2024, 3, 15));
        return teamEvent;
    }

    private TeamEventDetailsProjection teamEventDetailsProjection(
            Long receiptId,
            String receiptTitle,
            BigDecimal receiptAmount,
            Integer fileSequence,
            String fileUrl
    ) {
        return new TeamEventDetailsProjection() {
            @Override
            public Long getTeamEventId() {
                return TEAM_EVENT_ID;
            }

            @Override
            public LocalDate getEventDate() {
                return LocalDate.of(2024, 3, 15);
            }

            @Override
            public String getEventTitle() {
                return "Quarterly dinner";
            }

            @Override
            public String getInternalNote() {
                return "Review receipts";
            }

            @Override
            public String getSubmittedBy() {
                return "Jane Doe";
            }

            @Override
            public String getProjectNumber() {
                return "PN-100";
            }

            @Override
            public Long getReceiptId() {
                return receiptId;
            }

            @Override
            public String getReceiptTitle() {
                return receiptTitle;
            }

            @Override
            public LocalDate getReceiptDate() {
                return LocalDate.of(2024, 3, 16);
            }

            @Override
            public String getReceiptNumber() {
                return receiptId == null ? null : "REC-" + receiptId;
            }

            @Override
            public String getSubmittedFor() {
                return "John Smith";
            }

            @Override
            public String getReceiptStatus() {
                return "Submitted";
            }

            @Override
            public BigDecimal getReceiptAmount() {
                return receiptAmount;
            }

            @Override
            public String getReceiptCurrency() {
                return "EUR";
            }

            @Override
            public BigDecimal getGratuity() {
                return new BigDecimal("1.00");
            }

            @Override
            public BigDecimal getAmountNetFullVat() {
                return new BigDecimal("9.50");
            }

            @Override
            public Short getVatRateFull() {
                return 19;
            }

            @Override
            public String getExpenseCategory() {
                return "Travel";
            }

            @Override
            public String getExpenseNature() {
                return "Taxi";
            }

            @Override
            public String getIssuanceRegion() {
                return "DE";
            }

            @Override
            public Boolean getDeductTeamBalance() {
                return true;
            }

            @Override
            public Integer getFileSequence() {
                return fileSequence;
            }

            @Override
            public String getFileUrl() {
                return fileUrl;
            }

            @Override
            public Long getTeameventtypeId() {
                return 7L;
            }

            @Override
            public LocalDate getBookableThrough() {
                return LocalDate.of(2024, 4, 15);
            }

            @Override
            public String getStatus() {
                return "Submitted";
            }

            @Override
            public Long getProjectNumberId() {
                return 77L;
            }

            @Override
            public Long getPicklistId() {
                return 88L;
            }

            @Override
            public Long getPicklistMemberCount() {
                return 3L;
            }
        };
    }
}
