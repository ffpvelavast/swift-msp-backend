package com.rex.commodis.service;

import com.rex.commodis.dto.events.TeamEventStatusUpdateRequestDto;
import com.rex.commodis.dto.events.TeamEventStatusUpdateResponseDto;
import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.exceptions.ForbiddenException;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.model.enums.ReceiptStatusEnum;
import com.rex.commodis.model.enums.TeamEventStatusEnum;
import com.rex.commodis.model.events.TeamEventHistory;
import com.rex.commodis.model.events.TeamEventStatus;
import com.rex.commodis.model.receipts.ReceiptHistory;
import com.rex.commodis.model.receipts.ReceiptStatus;
import com.rex.commodis.model.receipts.receiptToTeamEvent.ReceiptToTeamEvent;
import com.rex.commodis.repository.events.TeamEventApproverRepository;
import com.rex.commodis.repository.events.TeamEventHistoryRepository;
import com.rex.commodis.repository.events.TeamEventRepository;
import com.rex.commodis.repository.events.TeamEventStatusRepository;
import com.rex.commodis.repository.receipts.ReceiptHistoryRepository;
import com.rex.commodis.repository.receipts.ReceiptStatusRepository;
import com.rex.commodis.repository.receipts.ReceiptToTeamEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamEventStatusUpdateServiceImplTest {

    @Mock private TeamEventRepository teamEventRepository;
    @Mock private TeamEventHistoryRepository teamEventHistoryRepository;
    @Mock private TeamEventStatusRepository teamEventStatusRepository;
    @Mock private TeamEventApproverRepository teamEventApproverRepository;
    @Mock private ReceiptToTeamEventRepository receiptToTeamEventRepository;
    @Mock private ReceiptHistoryRepository receiptHistoryRepository;
    @Mock private ReceiptStatusRepository receiptStatusRepository;

    @InjectMocks
    private TeamEventStatusUpdateServiceImpl teamEventStatusUpdateService;

    private static final Long TEAM_EVENT_ID = 10L;
    private static final Long REX_USER_ID = 1L;
    private static final Long RECEIPT_ID = 100L;

    @Test
    void updateTeamEventStatus_toApproved_updatesTeamEventHistoryAndLinkedReceiptHistory() {
        TeamEventStatus currentStatus = teamEventStatus((short) 1, TeamEventStatusEnum.SUBMITTED);
        TeamEventStatus targetStatus = teamEventStatus((short) 5, TeamEventStatusEnum.APPROVED);
        TeamEventHistory currentHistory = activeHistory(currentStatus);
        ReceiptToTeamEvent mapping = ReceiptToTeamEvent.builder()
                .teamEventId(TEAM_EVENT_ID)
                .receiptId(RECEIPT_ID)
                .build();
        ReceiptStatus receiptStatus = ReceiptStatus.builder()
                .id((short) 5)
                .status(ReceiptStatusEnum.APPROVED)
                .build();

        when(teamEventApproverRepository.existsByTeamEventIdAndRexUserId(TEAM_EVENT_ID, REX_USER_ID))
                .thenReturn(true);
        when(teamEventRepository.existsById(TEAM_EVENT_ID)).thenReturn(true);
        when(teamEventStatusRepository.findByStatusCode(TeamEventStatusEnum.APPROVED))
                .thenReturn(Optional.of(targetStatus));
        when(teamEventHistoryRepository.findActiveByTeamEventId(TEAM_EVENT_ID))
                .thenReturn(Optional.of(currentHistory));
        when(teamEventHistoryRepository.findActiveStatusCodeByTeamEventId(TEAM_EVENT_ID))
                .thenReturn(Optional.of(TeamEventStatusEnum.SUBMITTED));
        when(teamEventHistoryRepository.deactivateCurrentActiveStatus(TEAM_EVENT_ID)).thenReturn(1);
        when(receiptToTeamEventRepository.findByTeamEventId(TEAM_EVENT_ID))
                .thenReturn(List.of(mapping));
        when(receiptStatusRepository.findByStatus(ReceiptStatusEnum.APPROVED))
                .thenReturn(Optional.of(receiptStatus));

        TeamEventStatusUpdateResponseDto response =
                teamEventStatusUpdateService.updateTeamEventStatus(
                        TEAM_EVENT_ID,
                        new TeamEventStatusUpdateRequestDto(TeamEventStatusEnum.APPROVED),
                        REX_USER_ID
                );

        assertThat(response.getTeamEventId()).isEqualTo(TEAM_EVENT_ID);
        assertThat(response.getStatus()).isEqualTo("APPROVED");

        ArgumentCaptor<TeamEventHistory> teamHistoryCaptor =
                ArgumentCaptor.forClass(TeamEventHistory.class);
        verify(teamEventHistoryRepository).save(teamHistoryCaptor.capture());
        TeamEventHistory savedTeamHistory = teamHistoryCaptor.getValue();
        assertThat(savedTeamHistory.getTeamEventId()).isEqualTo(TEAM_EVENT_ID);
        assertThat(savedTeamHistory.getCreatorId()).isEqualTo(REX_USER_ID);
        assertThat(savedTeamHistory.getStatusId()).isEqualTo((short) 5);
        assertThat(savedTeamHistory.getActive()).isTrue();
        assertThat(savedTeamHistory.getExternalNote()).isNull();
        assertThat(savedTeamHistory.getCreatedAt()).isNotNull();

        ArgumentCaptor<ReceiptHistory> receiptHistoryCaptor =
                ArgumentCaptor.forClass(ReceiptHistory.class);
        verify(receiptHistoryRepository).deactivateCurrentActiveStatus(RECEIPT_ID);
        verify(receiptHistoryRepository).save(receiptHistoryCaptor.capture());
        ReceiptHistory savedReceiptHistory = receiptHistoryCaptor.getValue();
        assertThat(savedReceiptHistory.getReceiptId()).isEqualTo(RECEIPT_ID);
        assertThat(savedReceiptHistory.getCreatorId()).isEqualTo(REX_USER_ID);
        assertThat(savedReceiptHistory.getReceiptStatusId()).isEqualTo((short) 5);
        assertThat(savedReceiptHistory.getActive()).isTrue();
        assertThat(savedReceiptHistory.getExternalNote()).isNull();
    }

    @Test
    void updateTeamEventStatus_whenNoLinkedReceipts_updatesOnlyTeamEventHistory() {
        TeamEventStatus currentStatus = teamEventStatus((short) 1, TeamEventStatusEnum.SUBMITTED);
        TeamEventStatus targetStatus = teamEventStatus((short) 4, TeamEventStatusEnum.REJECTED);

        when(teamEventApproverRepository.existsByTeamEventIdAndRexUserId(TEAM_EVENT_ID, REX_USER_ID))
                .thenReturn(true);
        when(teamEventRepository.existsById(TEAM_EVENT_ID)).thenReturn(true);
        when(teamEventStatusRepository.findByStatusCode(TeamEventStatusEnum.REJECTED))
                .thenReturn(Optional.of(targetStatus));
        when(teamEventHistoryRepository.findActiveByTeamEventId(TEAM_EVENT_ID))
                .thenReturn(Optional.of(activeHistory(currentStatus)));
        when(teamEventHistoryRepository.findActiveStatusCodeByTeamEventId(TEAM_EVENT_ID))
                .thenReturn(Optional.of(TeamEventStatusEnum.SUBMITTED));
        when(receiptToTeamEventRepository.findByTeamEventId(TEAM_EVENT_ID)).thenReturn(List.of());

        TeamEventStatusUpdateResponseDto response =
                teamEventStatusUpdateService.updateTeamEventStatus(
                        TEAM_EVENT_ID,
                        new TeamEventStatusUpdateRequestDto(TeamEventStatusEnum.REJECTED),
                        REX_USER_ID
                );

        assertThat(response.getStatus()).isEqualTo("REJECTED");
        verify(teamEventHistoryRepository).deactivateCurrentActiveStatus(TEAM_EVENT_ID);
        verify(teamEventHistoryRepository).save(org.mockito.ArgumentMatchers.any(TeamEventHistory.class));
        verifyNoInteractions(receiptHistoryRepository, receiptStatusRepository);
    }

    @Test
    void updateTeamEventStatus_whenUserIsNotApprover_throwsForbiddenException() {
        when(teamEventApproverRepository.existsByTeamEventIdAndRexUserId(TEAM_EVENT_ID, REX_USER_ID))
                .thenReturn(false);

        assertThatThrownBy(() ->
                teamEventStatusUpdateService.updateTeamEventStatus(
                        TEAM_EVENT_ID,
                        new TeamEventStatusUpdateRequestDto(TeamEventStatusEnum.APPROVED),
                        REX_USER_ID
                ))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("User does not have permission");

        verifyNoInteractions(teamEventRepository, teamEventStatusRepository, teamEventHistoryRepository);
    }

    @Test
    void updateTeamEventStatus_whenStatusIsNotAllowed_throwsBadRequestException() {
        when(teamEventApproverRepository.existsByTeamEventIdAndRexUserId(TEAM_EVENT_ID, REX_USER_ID))
                .thenReturn(true);

        assertThatThrownBy(() ->
                teamEventStatusUpdateService.updateTeamEventStatus(
                        TEAM_EVENT_ID,
                        new TeamEventStatusUpdateRequestDto(TeamEventStatusEnum.SUBMITTED),
                        REX_USER_ID
                ))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid status 'SUBMITTED'");

        verifyNoInteractions(teamEventRepository, teamEventStatusRepository, teamEventHistoryRepository);
    }

    @Test
    void updateTeamEventStatus_whenTeamEventDoesNotExist_throwsNotFoundException() {
        when(teamEventApproverRepository.existsByTeamEventIdAndRexUserId(TEAM_EVENT_ID, REX_USER_ID))
                .thenReturn(true);
        when(teamEventRepository.existsById(TEAM_EVENT_ID)).thenReturn(false);

        assertThatThrownBy(() ->
                teamEventStatusUpdateService.updateTeamEventStatus(
                        TEAM_EVENT_ID,
                        new TeamEventStatusUpdateRequestDto(TeamEventStatusEnum.APPROVED),
                        REX_USER_ID
                ))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Team event not found with ID: " + TEAM_EVENT_ID);

        verifyNoInteractions(teamEventStatusRepository, teamEventHistoryRepository);
    }

    @Test
    void updateTeamEventStatus_whenNoActiveHistoryExists_throwsNotFoundException() {
        when(teamEventApproverRepository.existsByTeamEventIdAndRexUserId(TEAM_EVENT_ID, REX_USER_ID))
                .thenReturn(true);
        when(teamEventRepository.existsById(TEAM_EVENT_ID)).thenReturn(true);
        when(teamEventStatusRepository.findByStatusCode(TeamEventStatusEnum.APPROVED))
                .thenReturn(Optional.of(teamEventStatus((short) 5, TeamEventStatusEnum.APPROVED)));
        when(teamEventHistoryRepository.findActiveByTeamEventId(TEAM_EVENT_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                teamEventStatusUpdateService.updateTeamEventStatus(
                        TEAM_EVENT_ID,
                        new TeamEventStatusUpdateRequestDto(TeamEventStatusEnum.APPROVED),
                        REX_USER_ID
                ))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("No active status found for teamEventId=" + TEAM_EVENT_ID);

        verify(teamEventHistoryRepository, never()).deactivateCurrentActiveStatus(TEAM_EVENT_ID);
    }

    @Test
    void updateTeamEventStatus_whenCurrentStatusIsFinal_throwsBadRequestException() {
        TeamEventStatus currentStatus = teamEventStatus((short) 1, TeamEventStatusEnum.SUBMITTED);

        when(teamEventApproverRepository.existsByTeamEventIdAndRexUserId(TEAM_EVENT_ID, REX_USER_ID))
                .thenReturn(true);
        when(teamEventRepository.existsById(TEAM_EVENT_ID)).thenReturn(true);
        when(teamEventStatusRepository.findByStatusCode(TeamEventStatusEnum.REJECTED))
                .thenReturn(Optional.of(teamEventStatus((short) 4, TeamEventStatusEnum.REJECTED)));
        when(teamEventHistoryRepository.findActiveByTeamEventId(TEAM_EVENT_ID))
                .thenReturn(Optional.of(activeHistory(currentStatus)));
        when(teamEventHistoryRepository.findActiveStatusCodeByTeamEventId(TEAM_EVENT_ID))
                .thenReturn(Optional.of(TeamEventStatusEnum.APPROVED));

        assertThatThrownBy(() ->
                teamEventStatusUpdateService.updateTeamEventStatus(
                        TEAM_EVENT_ID,
                        new TeamEventStatusUpdateRequestDto(TeamEventStatusEnum.REJECTED),
                        REX_USER_ID
                ))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Current Team event status is 'APPROVED'");

        verify(teamEventHistoryRepository, never()).deactivateCurrentActiveStatus(TEAM_EVENT_ID);
    }

    @Test
    void updateTeamEventStatus_whenTargetStatusMatchesCurrentStatus_throwsBadRequestException() {
        TeamEventStatus approvedStatus = teamEventStatus((short) 5, TeamEventStatusEnum.APPROVED);

        when(teamEventApproverRepository.existsByTeamEventIdAndRexUserId(TEAM_EVENT_ID, REX_USER_ID))
                .thenReturn(true);
        when(teamEventRepository.existsById(TEAM_EVENT_ID)).thenReturn(true);
        when(teamEventStatusRepository.findByStatusCode(TeamEventStatusEnum.APPROVED))
                .thenReturn(Optional.of(approvedStatus));
        when(teamEventHistoryRepository.findActiveByTeamEventId(TEAM_EVENT_ID))
                .thenReturn(Optional.of(activeHistory(approvedStatus)));
        when(teamEventHistoryRepository.findActiveStatusCodeByTeamEventId(TEAM_EVENT_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                teamEventStatusUpdateService.updateTeamEventStatus(
                        TEAM_EVENT_ID,
                        new TeamEventStatusUpdateRequestDto(TeamEventStatusEnum.APPROVED),
                        REX_USER_ID
                ))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Team event already has status 'APPROVED'");

        verify(teamEventHistoryRepository, never()).deactivateCurrentActiveStatus(TEAM_EVENT_ID);
    }

    private TeamEventHistory activeHistory(TeamEventStatus status) {
        TeamEventHistory history = new TeamEventHistory();
        history.setTeamEventId(TEAM_EVENT_ID);
        history.setStatus(status);
        history.setStatusId(status.getId());
        history.setActive(true);
        return history;
    }

    private TeamEventStatus teamEventStatus(Short id, TeamEventStatusEnum statusEnum) {
        TeamEventStatus status = new TeamEventStatus();
        status.setId(id);
        status.setStatusCode(statusEnum);
        return status;
    }
}
