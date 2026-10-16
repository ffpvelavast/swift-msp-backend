package com.rex.commodis.service;

import com.rex.commodis.dto.receipts.ReceiptStatusUpdateRequestDto;
import com.rex.commodis.dto.receipts.ReceiptStatusUpdateResponseDto;
import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.exceptions.ForbiddenException;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.model.RexUser;
import com.rex.commodis.model.enums.ReceiptContextEnum;
import com.rex.commodis.model.enums.ReceiptStatusEnum;
import com.rex.commodis.model.receipts.Receipt;
import com.rex.commodis.model.receipts.ReceiptHistory;
import com.rex.commodis.model.receipts.ReceiptStatus;
import com.rex.commodis.model.receipts.receiptApprovals.ReceiptApprovals;
import com.rex.commodis.repository.RexUserRepository;
import com.rex.commodis.repository.receipts.ReceiptApprovalsRepository;
import com.rex.commodis.repository.receipts.ReceiptApproverRepository;
import com.rex.commodis.repository.receipts.ReceiptHistoryRepository;
import com.rex.commodis.repository.receipts.ReceiptRepository;
import com.rex.commodis.repository.receipts.ReceiptStatusRepository;
import com.rex.commodis.repository.receipts.ReceiptToCorporateEventRepository;
import com.rex.commodis.repository.receipts.ReceiptToTeamEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiptStatusUpdateServiceImplTest {

    @Mock private ReceiptRepository receiptRepository;
    @Mock private ReceiptHistoryRepository receiptHistoryRepository;
    @Mock private ReceiptStatusRepository receiptStatusRepository;
    @Mock private ReceiptApproverRepository receiptApproverRepository;
    @Mock private ReceiptApprovalsRepository receiptApprovalsRepository;
    @Mock private ReceiptToTeamEventRepository receiptToTeamEventRepository;
    @Mock private ReceiptToCorporateEventRepository receiptToCorporateEventRepository;
    @Mock private RexUserRepository rexUserRepository;

    @InjectMocks
    private ReceiptStatusUpdateServiceImpl service;

    private static final Long  RECEIPT_ID         = 100L;
    private static final Long  REX_USER_ID        = 1L;
    private static final Short TARGET_STATUS_ID   = (short) 5;

    @BeforeEach
    void setUp() {
        // Default happy-path stubs for non-final tests; specific tests override what they need.
        lenient().when(receiptRepository.existsById(RECEIPT_ID)).thenReturn(true);
        lenient().when(receiptApproverRepository.existsByReceiptIdAndRexUserId(RECEIPT_ID, REX_USER_ID))
                .thenReturn(true);
        lenient().when(receiptHistoryRepository.findActiveByReceiptId(RECEIPT_ID))
                .thenReturn(Optional.of(new ReceiptHistory()));
        lenient().when(receiptHistoryRepository.findActiveStatusCodeByReceiptId(RECEIPT_ID))
                .thenReturn(Optional.of(ReceiptStatusEnum.SUBMITTED));
        lenient().when(receiptHistoryRepository.deactivateCurrentActiveStatus(RECEIPT_ID)).thenReturn(1);
        lenient().when(receiptHistoryRepository.save(any(ReceiptHistory.class)))
                .thenAnswer(inv -> inv.getArgument(0));
    }

    // ── Happy paths ────────────────────────────────────────────────────────────

    @Test
    void updateReceiptStatus_approved_standaloneReceipt_savesHistoryAndCreatesApproval() {
        stubTargetStatus(ReceiptStatusEnum.APPROVED);
        when(receiptToTeamEventRepository.existsByReceiptId(RECEIPT_ID)).thenReturn(false);
        when(receiptToCorporateEventRepository.existsByReceiptId(RECEIPT_ID)).thenReturn(false);
        when(receiptRepository.getReferenceById(RECEIPT_ID)).thenReturn(new Receipt());
        when(rexUserRepository.getReferenceById(REX_USER_ID)).thenReturn(new RexUser());

        ReceiptStatusUpdateResponseDto response =
                service.updateReceiptStatus(requestOf(ReceiptStatusEnum.APPROVED), RECEIPT_ID, REX_USER_ID);

        assertThat(response.getReceiptId()).isEqualTo(RECEIPT_ID);
        assertThat(response.getCreatorId()).isEqualTo(REX_USER_ID);
        assertThat(response.getStatus()).isEqualTo("APPROVED");

        verify(receiptHistoryRepository).deactivateCurrentActiveStatus(RECEIPT_ID);

        ArgumentCaptor<ReceiptApprovals> approvalCaptor = ArgumentCaptor.forClass(ReceiptApprovals.class);
        verify(receiptApprovalsRepository).save(approvalCaptor.capture());
        ReceiptApprovals approval = approvalCaptor.getValue();
        assertThat(approval.getId().getReceiptId()).isEqualTo(RECEIPT_ID);
        assertThat(approval.getId().getApproverId()).isEqualTo(REX_USER_ID);
        assertThat(approval.getReceiptContext()).isEqualTo(ReceiptContextEnum.STANDALONE.name());
        assertThat(approval.getCreatedAt()).isNotNull();
    }

    @Test
    void updateReceiptStatus_approved_teamEventReceipt_setsTeamEventContext() {
        stubTargetStatus(ReceiptStatusEnum.APPROVED);
        when(receiptToTeamEventRepository.existsByReceiptId(RECEIPT_ID)).thenReturn(true);
        when(receiptRepository.getReferenceById(RECEIPT_ID)).thenReturn(new Receipt());
        when(rexUserRepository.getReferenceById(REX_USER_ID)).thenReturn(new RexUser());

        service.updateReceiptStatus(requestOf(ReceiptStatusEnum.APPROVED), RECEIPT_ID, REX_USER_ID);

        ArgumentCaptor<ReceiptApprovals> captor = ArgumentCaptor.forClass(ReceiptApprovals.class);
        verify(receiptApprovalsRepository).save(captor.capture());
        assertThat(captor.getValue().getReceiptContext()).isEqualTo(ReceiptContextEnum.TEAM_EVENT.name());
        verifyNoInteractions(receiptToCorporateEventRepository);
    }

    @Test
    void updateReceiptStatus_approved_corporateEventReceipt_setsCorporateEventContext() {
        stubTargetStatus(ReceiptStatusEnum.APPROVED);
        when(receiptToTeamEventRepository.existsByReceiptId(RECEIPT_ID)).thenReturn(false);
        when(receiptToCorporateEventRepository.existsByReceiptId(RECEIPT_ID)).thenReturn(true);
        when(receiptRepository.getReferenceById(RECEIPT_ID)).thenReturn(new Receipt());
        when(rexUserRepository.getReferenceById(REX_USER_ID)).thenReturn(new RexUser());

        service.updateReceiptStatus(requestOf(ReceiptStatusEnum.APPROVED), RECEIPT_ID, REX_USER_ID);

        ArgumentCaptor<ReceiptApprovals> captor = ArgumentCaptor.forClass(ReceiptApprovals.class);
        verify(receiptApprovalsRepository).save(captor.capture());
        assertThat(captor.getValue().getReceiptContext()).isEqualTo(ReceiptContextEnum.CORPORATE_EVENT.name());
    }

    @Test
    void updateReceiptStatus_rejected_savesHistoryButDoesNotCreateApproval() {
        stubTargetStatus(ReceiptStatusEnum.REJECTED);

        ReceiptStatusUpdateResponseDto response =
                service.updateReceiptStatus(requestOf(ReceiptStatusEnum.REJECTED), RECEIPT_ID, REX_USER_ID);

        assertThat(response.getStatus()).isEqualTo("REJECTED");
        verify(receiptHistoryRepository).save(any(ReceiptHistory.class));
        verifyNoInteractions(receiptApprovalsRepository,
                receiptToTeamEventRepository, receiptToCorporateEventRepository, rexUserRepository);
    }

    @Test
    void updateReceiptStatus_correctionRequested_savesHistoryButDoesNotCreateApproval() {
        stubTargetStatus(ReceiptStatusEnum.CORRECTION_REQUESTED);

        ReceiptStatusUpdateResponseDto response =
                service.updateReceiptStatus(
                        requestOf(ReceiptStatusEnum.CORRECTION_REQUESTED), RECEIPT_ID, REX_USER_ID);

        assertThat(response.getStatus()).isEqualTo("CORRECTION_REQUESTED");
        verify(receiptHistoryRepository).save(any(ReceiptHistory.class));
        verifyNoInteractions(receiptApprovalsRepository);
    }

    @Test
    void updateReceiptStatus_persistsNewHistoryEntryWithCorrectFields() {
        stubTargetStatus(ReceiptStatusEnum.REJECTED);

        service.updateReceiptStatus(requestOf(ReceiptStatusEnum.REJECTED), RECEIPT_ID, REX_USER_ID);

        ArgumentCaptor<ReceiptHistory> captor = ArgumentCaptor.forClass(ReceiptHistory.class);
        verify(receiptHistoryRepository).save(captor.capture());
        ReceiptHistory saved = captor.getValue();
        assertThat(saved.getReceiptId()).isEqualTo(RECEIPT_ID);
        assertThat(saved.getCreatorId()).isEqualTo(REX_USER_ID);
        assertThat(saved.getReceiptStatusId()).isEqualTo(TARGET_STATUS_ID);
        assertThat(saved.getActive()).isTrue();
        assertThat(saved.getExternalNote()).isNull();
    }

    // ── Error paths ────────────────────────────────────────────────────────────

    @Test
    void updateReceiptStatus_receiptNotFound_throwsNotFoundException() {
        when(receiptRepository.existsById(RECEIPT_ID)).thenReturn(false);

        assertThatThrownBy(() ->
                service.updateReceiptStatus(requestOf(ReceiptStatusEnum.APPROVED), RECEIPT_ID, REX_USER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Receipt not found with ID: " + RECEIPT_ID);

        verify(receiptHistoryRepository, never()).save(any());
        verifyNoInteractions(receiptApprovalsRepository);
    }

    @Test
    void updateReceiptStatus_userIsNotApprover_throwsForbiddenException() {
        when(receiptApproverRepository.existsByReceiptIdAndRexUserId(RECEIPT_ID, REX_USER_ID))
                .thenReturn(false);

        assertThatThrownBy(() ->
                service.updateReceiptStatus(requestOf(ReceiptStatusEnum.APPROVED), RECEIPT_ID, REX_USER_ID))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("does not have permission");

        verify(receiptHistoryRepository, never()).save(any());
    }

    @Test
    void updateReceiptStatus_statusNotConfiguredInDb_throwsNotFoundException() {
        when(receiptStatusRepository.findByStatus(ReceiptStatusEnum.APPROVED)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.updateReceiptStatus(requestOf(ReceiptStatusEnum.APPROVED), RECEIPT_ID, REX_USER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Receipt status 'APPROVED' is not configured in the database.");

        verify(receiptHistoryRepository, never()).save(any());
    }

    @Test
    void updateReceiptStatus_noActiveHistory_throwsNotFoundException() {
        stubTargetStatus(ReceiptStatusEnum.APPROVED);
        when(receiptHistoryRepository.findActiveByReceiptId(RECEIPT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.updateReceiptStatus(requestOf(ReceiptStatusEnum.APPROVED), RECEIPT_ID, REX_USER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("No active status found for receiptId=" + RECEIPT_ID);

        verify(receiptHistoryRepository, never()).save(any());
    }

    @Test
    void updateReceiptStatus_currentStatusIsApproved_throwsBadRequestException() {
        stubTargetStatus(ReceiptStatusEnum.CORRECTION_REQUESTED);
        when(receiptHistoryRepository.findActiveStatusCodeByReceiptId(RECEIPT_ID))
                .thenReturn(Optional.of(ReceiptStatusEnum.APPROVED));

        assertThatThrownBy(() ->
                service.updateReceiptStatus(
                        requestOf(ReceiptStatusEnum.CORRECTION_REQUESTED), RECEIPT_ID, REX_USER_ID))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Current Receipt status is 'APPROVED' and cannot be changed.");

        verify(receiptHistoryRepository, never()).save(any());
        verifyNoInteractions(receiptApprovalsRepository);
    }

    @Test
    void updateReceiptStatus_currentStatusIsRejected_throwsBadRequestException() {
        stubTargetStatus(ReceiptStatusEnum.APPROVED);
        when(receiptHistoryRepository.findActiveStatusCodeByReceiptId(RECEIPT_ID))
                .thenReturn(Optional.of(ReceiptStatusEnum.REJECTED));

        assertThatThrownBy(() ->
                service.updateReceiptStatus(requestOf(ReceiptStatusEnum.APPROVED), RECEIPT_ID, REX_USER_ID))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Current Receipt status is 'REJECTED' and cannot be changed.");

        verify(receiptHistoryRepository, never()).save(any());
    }

    @Test
    void updateReceiptStatus_currentStatusIsSubmitted_doesNotBlockChange() {
        // SUBMITTED is non-final, so moving to APPROVED should proceed without throwing.
        stubTargetStatus(ReceiptStatusEnum.APPROVED);
        when(receiptHistoryRepository.findActiveStatusCodeByReceiptId(RECEIPT_ID))
                .thenReturn(Optional.of(ReceiptStatusEnum.SUBMITTED));
        when(receiptToTeamEventRepository.existsByReceiptId(RECEIPT_ID)).thenReturn(false);
        when(receiptToCorporateEventRepository.existsByReceiptId(RECEIPT_ID)).thenReturn(false);
        when(receiptRepository.getReferenceById(RECEIPT_ID)).thenReturn(new Receipt());
        when(rexUserRepository.getReferenceById(REX_USER_ID)).thenReturn(new RexUser());

        ReceiptStatusUpdateResponseDto response =
                service.updateReceiptStatus(requestOf(ReceiptStatusEnum.APPROVED), RECEIPT_ID, REX_USER_ID);

        assertThat(response.getStatus()).isEqualTo("APPROVED");
        verify(receiptHistoryRepository).save(any(ReceiptHistory.class));
        verify(receiptApprovalsRepository).save(any(ReceiptApprovals.class));
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private ReceiptStatusUpdateRequestDto requestOf(ReceiptStatusEnum status) {
        return new ReceiptStatusUpdateRequestDto(status);
    }

    private void stubTargetStatus(ReceiptStatusEnum statusEnum) {
        ReceiptStatus targetStatus = new ReceiptStatus();
        targetStatus.setId(TARGET_STATUS_ID);
        targetStatus.setStatus(statusEnum);
        when(receiptStatusRepository.findByStatus(statusEnum)).thenReturn(Optional.of(targetStatus));
    }
}
