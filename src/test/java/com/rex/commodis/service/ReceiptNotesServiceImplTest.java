package com.rex.commodis.service;

import com.rex.commodis.dto.ReceiptUpdateNotesRequestDto;
import com.rex.commodis.dto.ReceiptUpdateNotesResponseDto;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.model.receipts.Receipt;
import com.rex.commodis.model.receipts.ReceiptHistory;
import com.rex.commodis.repository.receipts.ReceiptApproverRepository;
import com.rex.commodis.repository.receipts.ReceiptHistoryRepository;
import com.rex.commodis.repository.receipts.ReceiptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

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
class ReceiptNotesServiceImplTest {

    @Mock private ReceiptRepository receiptRepository;
    @Mock private ReceiptHistoryRepository receiptHistoryRepository;
    @Mock private ReceiptApproverRepository receiptApproverRepository;

    @InjectMocks
    private ReceiptNotesServiceImpl service;

    private static final Long RECEIPT_ID    = 100L;
    private static final Long REX_USER_ID   = 1L;
    private static final Long HISTORY_ID    = 500L;

    private Receipt receipt;
    private ReceiptHistory activeHistory;

    @BeforeEach
    void setUp() {
        receipt = new Receipt();
        receipt.setId(RECEIPT_ID);
        receipt.setInternalNote("old internal");

        activeHistory = new ReceiptHistory();
        activeHistory.setId(HISTORY_ID);
        activeHistory.setReceiptId(RECEIPT_ID);
        activeHistory.setActive(true);
        activeHistory.setExternalNote("old external");

        // Save returns the same instance the service mutated in place.
        lenient().when(receiptRepository.save(any(Receipt.class))).thenAnswer(inv -> inv.getArgument(0));
        lenient().when(receiptHistoryRepository.save(any(ReceiptHistory.class)))
                .thenAnswer(inv -> inv.getArgument(0));
    }

    // ── Happy paths ────────────────────────────────────────────────────────────

    @Test
    void updateReceiptNotes_happyPath_updatesBothNotesAndReturnsThem() {
        stubApproverOk();
        stubReceiptFound();
        stubActiveHistory();
        ReceiptUpdateNotesRequestDto request = request("new external", "new internal");

        ReceiptUpdateNotesResponseDto response =
                service.updateReceiptNotes(RECEIPT_ID, REX_USER_ID, request);

        assertThat(response.getInternalNote()).isEqualTo("new internal");
        assertThat(response.getExternalNote()).isEqualTo("new external");
    }

    @Test
    void updateReceiptNotes_mutatesReceiptInternalNoteBeforeSave() {
        stubApproverOk();
        stubReceiptFound();
        stubActiveHistory();
        ReceiptUpdateNotesRequestDto request = request("ext", "fresh internal");

        service.updateReceiptNotes(RECEIPT_ID, REX_USER_ID, request);

        ArgumentCaptor<Receipt> captor = ArgumentCaptor.forClass(Receipt.class);
        verify(receiptRepository).save(captor.capture());
        assertThat(captor.getValue().getInternalNote()).isEqualTo("fresh internal");
        assertThat(captor.getValue().getId()).isEqualTo(RECEIPT_ID);
    }

    @Test
    void updateReceiptNotes_mutatesActiveHistoryExternalNoteBeforeSave() {
        stubApproverOk();
        stubReceiptFound();
        stubActiveHistory();
        ReceiptUpdateNotesRequestDto request = request("fresh external", "int");

        service.updateReceiptNotes(RECEIPT_ID, REX_USER_ID, request);

        ArgumentCaptor<ReceiptHistory> captor = ArgumentCaptor.forClass(ReceiptHistory.class);
        verify(receiptHistoryRepository).save(captor.capture());
        assertThat(captor.getValue().getExternalNote()).isEqualTo("fresh external");
        assertThat(captor.getValue().getId()).isEqualTo(HISTORY_ID);
    }

    @Test
    void updateReceiptNotes_nullInternalNote_persistsNull() {
        stubApproverOk();
        stubReceiptFound();
        stubActiveHistory();
        ReceiptUpdateNotesRequestDto request = request("ext", null);

        ReceiptUpdateNotesResponseDto response =
                service.updateReceiptNotes(RECEIPT_ID, REX_USER_ID, request);

        assertThat(response.getInternalNote()).isNull();
        assertThat(response.getExternalNote()).isEqualTo("ext");

        ArgumentCaptor<Receipt> receiptCaptor = ArgumentCaptor.forClass(Receipt.class);
        verify(receiptRepository).save(receiptCaptor.capture());
        assertThat(receiptCaptor.getValue().getInternalNote()).isNull();
    }

    @Test
    void updateReceiptNotes_nullExternalNote_persistsNull() {
        stubApproverOk();
        stubReceiptFound();
        stubActiveHistory();
        ReceiptUpdateNotesRequestDto request = request(null, "int");

        ReceiptUpdateNotesResponseDto response =
                service.updateReceiptNotes(RECEIPT_ID, REX_USER_ID, request);

        assertThat(response.getExternalNote()).isNull();
        assertThat(response.getInternalNote()).isEqualTo("int");

        ArgumentCaptor<ReceiptHistory> historyCaptor = ArgumentCaptor.forClass(ReceiptHistory.class);
        verify(receiptHistoryRepository).save(historyCaptor.capture());
        assertThat(historyCaptor.getValue().getExternalNote()).isNull();
    }

    @Test
    void updateReceiptNotes_bothNotesNull_persistsBothAsNull() {
        stubApproverOk();
        stubReceiptFound();
        stubActiveHistory();
        ReceiptUpdateNotesRequestDto request = request(null, null);

        ReceiptUpdateNotesResponseDto response =
                service.updateReceiptNotes(RECEIPT_ID, REX_USER_ID, request);

        assertThat(response.getInternalNote()).isNull();
        assertThat(response.getExternalNote()).isNull();
    }

    @Test
    void updateReceiptNotes_savesReceiptAndHistoryExactlyOnce() {
        stubApproverOk();
        stubReceiptFound();
        stubActiveHistory();
        ReceiptUpdateNotesRequestDto request = request("ext", "int");

        service.updateReceiptNotes(RECEIPT_ID, REX_USER_ID, request);

        verify(receiptRepository).save(any(Receipt.class));
        verify(receiptHistoryRepository).save(any(ReceiptHistory.class));
    }

    // ── Error paths ────────────────────────────────────────────────────────────

    @Test
    void updateReceiptNotes_userIsNotApprover_throwsAccessDeniedException() {
        when(receiptApproverRepository.existsByReceiptIdAndRexUserId(RECEIPT_ID, REX_USER_ID))
                .thenReturn(false);

        assertThatThrownBy(() ->
                service.updateReceiptNotes(RECEIPT_ID, REX_USER_ID, request("ext", "int")))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("does not have permission");

        verifyNoInteractions(receiptRepository, receiptHistoryRepository);
    }

    @Test
    void updateReceiptNotes_receiptNotFound_throwsNotFoundException() {
        stubApproverOk();
        when(receiptRepository.findById(RECEIPT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.updateReceiptNotes(RECEIPT_ID, REX_USER_ID, request("ext", "int")))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Receipt not found with ID: " + RECEIPT_ID);

        verify(receiptRepository, never()).save(any());
        verifyNoInteractions(receiptHistoryRepository);
    }

    @Test
    void updateReceiptNotes_noActiveHistory_throwsNotFoundException() {
        stubApproverOk();
        stubReceiptFound();
        when(receiptHistoryRepository.findByReceiptIdAndActiveTrue(RECEIPT_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.updateReceiptNotes(RECEIPT_ID, REX_USER_ID, request("ext", "int")))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("No active history found for receipt ID: " + RECEIPT_ID);

        verify(receiptRepository, never()).save(any());
        verify(receiptHistoryRepository, never()).save(any());
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private void stubApproverOk() {
        when(receiptApproverRepository.existsByReceiptIdAndRexUserId(RECEIPT_ID, REX_USER_ID))
                .thenReturn(true);
    }

    private void stubReceiptFound() {
        when(receiptRepository.findById(RECEIPT_ID)).thenReturn(Optional.of(receipt));
    }

    private void stubActiveHistory() {
        when(receiptHistoryRepository.findByReceiptIdAndActiveTrue(RECEIPT_ID))
                .thenReturn(Optional.of(activeHistory));
    }

    private ReceiptUpdateNotesRequestDto request(String externalNote, String internalNote) {
        return new ReceiptUpdateNotesRequestDto(externalNote, internalNote);
    }
}
