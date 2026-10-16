package com.rex.commodis.controller;

import com.rex.commodis.dto.*;
import com.rex.commodis.dto.receipts.*;
import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.model.enums.ReceiptSortApproverBy;
import com.rex.commodis.model.enums.ReceiptSortSubmittedBy;
import com.rex.commodis.model.enums.ReceiptStatusEnum;
import com.rex.commodis.security.user.RexUserContext;
import com.rex.commodis.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceiptsControllerTest {

    @Mock private ReceiptService receiptService;
    @Mock private ReceiptDetailsService receiptDetailsService;
    @Mock private ReceiptSidePanelService receiptSidePanelService;
    @Mock private ReceiptStatusUpdateService receiptStatusUpdateService;
    @Mock private ReceiptDropDownService receiptDropDownService;
    @Mock private ReceiptNotesService receiptNotesService;
    @Mock private ReceiptsListingTableService receiptsService;
    @Mock private ReceiptCountService receiptCountService;

    @InjectMocks
    private ReceiptsController controller;

    private RexUserContext userContext;

    private static final Long USER_ID     = 1L;
    private static final Long PLATFORM_ID = 10L;
    private static final Long RECEIPT_ID  = 100L;

    @BeforeEach
    void setUp() {
        userContext = new RexUserContext(USER_ID, PLATFORM_ID, "test@rex.com");
    }

    // ── createReceipt ──────────────────────────────────────────────────────────

    @Test
    void createReceipt_withFiles_delegatesToServiceAndReturnsReceiptId() {
        CreateReceiptRequestDto request = new CreateReceiptRequestDto();
        List<MultipartFile> files = List.of(mock(MultipartFile.class));
        CreateReceiptResponseDto expected = new CreateReceiptResponseDto(42L);

        when(receiptService.createReceipt(request, PLATFORM_ID, USER_ID, files)).thenReturn(expected);

        ResponseEntity<CreateReceiptResponseDto> response = controller.createReceipt(request, files, userContext);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expected);
        verify(receiptService).createReceipt(request, PLATFORM_ID, USER_ID, files);
    }

    @Test
    void createReceipt_withoutFiles_passesNullFilesToService() {
        CreateReceiptRequestDto request = new CreateReceiptRequestDto();
        CreateReceiptResponseDto expected = new CreateReceiptResponseDto(43L);

        when(receiptService.createReceipt(request, PLATFORM_ID, USER_ID, null)).thenReturn(expected);

        ResponseEntity<CreateReceiptResponseDto> response = controller.createReceipt(request, null, userContext);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expected);
        verify(receiptService).createReceipt(request, PLATFORM_ID, USER_ID, null);
    }

    // ── getReceiptDetails ──────────────────────────────────────────────────────

    @Test
    void getReceiptDetails_delegatesWithReceiptIdAndUserId() {
        ReceiptDetailResponseDto expected = mock(ReceiptDetailResponseDto.class);
        when(receiptDetailsService.getReceiptDetails(RECEIPT_ID, USER_ID)).thenReturn(expected);

        ResponseEntity<ReceiptDetailResponseDto> response = controller.getReceiptDetails(RECEIPT_ID, userContext);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expected);
        verify(receiptDetailsService).getReceiptDetails(RECEIPT_ID, USER_ID);
    }

    // ── getSidePanel ───────────────────────────────────────────────────────────

    @Test
    void getSidePanel_delegatesWithReceiptIdAndUserId() {
        ReceiptSidePanelResponseDto expected = mock(ReceiptSidePanelResponseDto.class);
        when(receiptSidePanelService.getReceiptSidePanel(RECEIPT_ID, USER_ID)).thenReturn(expected);

        ResponseEntity<ReceiptSidePanelResponseDto> response = controller.getSidePanel(RECEIPT_ID, userContext);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expected);
        verify(receiptSidePanelService).getReceiptSidePanel(RECEIPT_ID, USER_ID);
    }

    // ── updateReceiptStatus ────────────────────────────────────────────────────

    @Test
    void updateReceiptStatus_approved_delegatesCorrectStatusAndIds() {
        ReceiptStatusUpdateRequestDto requestDto = new ReceiptStatusUpdateRequestDto(ReceiptStatusEnum.APPROVED);
        ReceiptStatusUpdateResponseDto expected = mock(ReceiptStatusUpdateResponseDto.class);

        when(receiptStatusUpdateService.updateReceiptStatus(requestDto, RECEIPT_ID, USER_ID)).thenReturn(expected);

        ResponseEntity<ReceiptStatusUpdateResponseDto> response =
                controller.updateReceiptStatus(RECEIPT_ID, requestDto, userContext);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expected);
        verify(receiptStatusUpdateService).updateReceiptStatus(requestDto, RECEIPT_ID, USER_ID);
    }

    @Test
    void updateReceiptStatus_rejected_delegatesCorrectStatus() {
        ReceiptStatusUpdateRequestDto requestDto = new ReceiptStatusUpdateRequestDto(ReceiptStatusEnum.REJECTED);
        ReceiptStatusUpdateResponseDto expected = mock(ReceiptStatusUpdateResponseDto.class);

        when(receiptStatusUpdateService.updateReceiptStatus(requestDto, RECEIPT_ID, USER_ID)).thenReturn(expected);

        ResponseEntity<ReceiptStatusUpdateResponseDto> response =
                controller.updateReceiptStatus(RECEIPT_ID, requestDto, userContext);

        assertThat(response.getBody()).isEqualTo(expected);
        verify(receiptStatusUpdateService).updateReceiptStatus(requestDto, RECEIPT_ID, USER_ID);
    }

    @Test
    void updateReceiptStatus_correctionRequested_delegatesCorrectStatus() {
        ReceiptStatusUpdateRequestDto requestDto =
                new ReceiptStatusUpdateRequestDto(ReceiptStatusEnum.CORRECTION_REQUESTED);
        ReceiptStatusUpdateResponseDto expected = mock(ReceiptStatusUpdateResponseDto.class);

        when(receiptStatusUpdateService.updateReceiptStatus(requestDto, RECEIPT_ID, USER_ID)).thenReturn(expected);

        ResponseEntity<ReceiptStatusUpdateResponseDto> response =
                controller.updateReceiptStatus(RECEIPT_ID, requestDto, userContext);

        assertThat(response.getBody()).isEqualTo(expected);
    }

    // ── updateReceiptNotes ─────────────────────────────────────────────────────

    @Test
    void updateReceiptNotes_delegatesWithReceiptIdUserIdAndRequest() {
        ReceiptUpdateNotesRequestDto notesRequest = mock(ReceiptUpdateNotesRequestDto.class);
        ReceiptUpdateNotesResponseDto expected = mock(ReceiptUpdateNotesResponseDto.class);

        when(receiptNotesService.updateReceiptNotes(RECEIPT_ID, USER_ID, notesRequest)).thenReturn(expected);

        ResponseEntity<ReceiptUpdateNotesResponseDto> response =
                controller.updateReceiptNotes(RECEIPT_ID, notesRequest, userContext);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expected);
        verify(receiptNotesService).updateReceiptNotes(RECEIPT_ID, USER_ID, notesRequest);
    }

    // ── getReceiptsDropdownData ────────────────────────────────────────────────

    @Test
    void getReceiptsDropdownData_delegatesWithUserIdAndPlatformId() {
        ReceiptDropDownResponseDto expected = mock(ReceiptDropDownResponseDto.class);
        when(receiptDropDownService.getDropDowns(USER_ID, PLATFORM_ID)).thenReturn(expected);

        ResponseEntity<ReceiptDropDownResponseDto> response = controller.getReceiptsDropdownData(userContext);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expected);
        verify(receiptDropDownService).getDropDowns(USER_ID, PLATFORM_ID);
    }

    // ── getSubmitterReceiptCounts ──────────────────────────────────────────────

    @Test
    void getSubmitterReceiptCounts_delegatesWithUserIdAndReturnsCounts() {
        ReceiptStatusCountDto expected = ReceiptStatusCountDto.builder()
                .total(5L)
                .byStatus(Map.of(ReceiptStatusEnum.SUBMITTED, 3L, ReceiptStatusEnum.APPROVED, 2L))
                .build();
        when(receiptCountService.getSubmitterReceiptCounts(USER_ID)).thenReturn(expected);

        ResponseEntity<ReceiptStatusCountDto> response = controller.getSubmitterReceiptCounts(userContext);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expected);
        assertThat(response.getBody().getTotal()).isEqualTo(5L);
        verify(receiptCountService).getSubmitterReceiptCounts(USER_ID);
    }

    // ── getApproverReceiptCounts ───────────────────────────────────────────────

    @Test
    void getApproverReceiptCounts_delegatesWithUserIdAndReturnsCounts() {
        ReceiptStatusCountDto expected = ReceiptStatusCountDto.builder()
                .total(3L)
                .byStatus(Map.of(ReceiptStatusEnum.SUBMITTED, 3L))
                .build();
        when(receiptCountService.getApproverReceiptCounts(USER_ID)).thenReturn(expected);

        ResponseEntity<ReceiptStatusCountDto> response = controller.getApproverReceiptCounts(userContext);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expected);
        assertThat(response.getBody().getTotal()).isEqualTo(3L);
        verify(receiptCountService).getApproverReceiptCounts(USER_ID);
    }

    // ── getSubmitterReceipts ───────────────────────────────────────────────────

    @Test
    void getSubmitterReceipts_withAllFilters_delegatesAllArgsToService() {
        LocalDate from   = LocalDate.of(2024, 1, 1);
        LocalDate to     = LocalDate.of(2024, 12, 31);
        BigDecimal amount = BigDecimal.valueOf(150.00);
        ReceiptGetListSubmitterResponseDto expected = mock(ReceiptGetListSubmitterResponseDto.class);

        when(receiptsService.getSubmitterReceipts(
                USER_ID, ReceiptStatusEnum.SUBMITTED, "Hotel", "Alice",
                from, to, "Meals", amount, 1, 20,
                ReceiptSortSubmittedBy.RECEIPT_DATE, "ASC"))
                .thenReturn(expected);

        ResponseEntity<ReceiptGetListSubmitterResponseDto> response = controller.getSubmitterReceipts(
                ReceiptStatusEnum.SUBMITTED, "Hotel", "Alice", from, to,
                "Meals", amount, 1, 20,
                ReceiptSortSubmittedBy.RECEIPT_DATE, "ASC", userContext);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expected);
        verify(receiptsService).getSubmitterReceipts(
                USER_ID, ReceiptStatusEnum.SUBMITTED, "Hotel", "Alice",
                from, to, "Meals", amount, 1, 20,
                ReceiptSortSubmittedBy.RECEIPT_DATE, "ASC");
    }

    @Test
    void getSubmitterReceipts_withNoFilters_delegatesNullsToService() {
        ReceiptGetListSubmitterResponseDto expected = mock(ReceiptGetListSubmitterResponseDto.class);

        when(receiptsService.getSubmitterReceipts(
                USER_ID, null, null, null, null, null, null, null, 1, 20, null, null))
                .thenReturn(expected);

        ResponseEntity<ReceiptGetListSubmitterResponseDto> response = controller.getSubmitterReceipts(
                null, null, null, null, null, null, null, 1, 20, null, null, userContext);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    void getSubmitterReceipts_onlyFromDateProvided_throwsBadRequest() {
        assertThatThrownBy(() -> controller.getSubmitterReceipts(
                null, null, null,
                LocalDate.of(2024, 1, 1), null,
                null, null, 1, 20, null, null, userContext))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("receipt_from_date");
    }

    @Test
    void getSubmitterReceipts_onlyToDateProvided_throwsBadRequest() {
        assertThatThrownBy(() -> controller.getSubmitterReceipts(
                null, null, null,
                null, LocalDate.of(2024, 12, 31),
                null, null, 1, 20, null, null, userContext))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("receipt_from_date");
    }

    @Test
    void getSubmitterReceipts_bothDatesProvided_doesNotThrow() {
        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to   = LocalDate.of(2024, 12, 31);

        when(receiptsService.getSubmitterReceipts(
                USER_ID, null, null, null, from, to, null, null, 1, 20, null, null))
                .thenReturn(mock(ReceiptGetListSubmitterResponseDto.class));

        ResponseEntity<ReceiptGetListSubmitterResponseDto> response = controller.getSubmitterReceipts(
                null, null, null, from, to, null, null, 1, 20, null, null, userContext);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    // ── getApproverReceipts ────────────────────────────────────────────────────

    @Test
    void getApproverReceipts_withAllFilters_delegatesAllArgsToService() {
        LocalDate from    = LocalDate.of(2024, 3, 1);
        LocalDate to      = LocalDate.of(2024, 9, 30);
        BigDecimal amount = BigDecimal.valueOf(500.00);
        ReceiptGetListApproverResponseDto expected = mock(ReceiptGetListApproverResponseDto.class);

        when(receiptsService.getApproverReceipts(
                USER_ID, ReceiptStatusEnum.REJECTED, "Taxi", "Bob",
                from, to, "Travel", amount, 2, 10,
                ReceiptSortApproverBy.RECEIPT_AMOUNT, "DESC"))
                .thenReturn(expected);

        ResponseEntity<ReceiptGetListApproverResponseDto> response = controller.getApproverReceipts(
                ReceiptStatusEnum.REJECTED, "Taxi", "Bob", from, to,
                "Travel", amount, 2, 10,
                ReceiptSortApproverBy.RECEIPT_AMOUNT, "DESC", userContext);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expected);
        verify(receiptsService).getApproverReceipts(
                USER_ID, ReceiptStatusEnum.REJECTED, "Taxi", "Bob",
                from, to, "Travel", amount, 2, 10,
                ReceiptSortApproverBy.RECEIPT_AMOUNT, "DESC");
    }

    @Test
    void getApproverReceipts_withNoFilters_delegatesNullsToService() {
        ReceiptGetListApproverResponseDto expected = mock(ReceiptGetListApproverResponseDto.class);

        when(receiptsService.getApproverReceipts(
                USER_ID, null, null, null, null, null, null, null, 1, 20, null, null))
                .thenReturn(expected);

        ResponseEntity<ReceiptGetListApproverResponseDto> response = controller.getApproverReceipts(
                null, null, null, null, null, null, null, 1, 20, null, null, userContext);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    void getApproverReceipts_onlyFromDateProvided_throwsBadRequest() {
        assertThatThrownBy(() -> controller.getApproverReceipts(
                null, null, null,
                LocalDate.of(2024, 1, 1), null,
                null, null, 1, 20, null, null, userContext))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("receipt_from_date");
    }

    @Test
    void getApproverReceipts_onlyToDateProvided_throwsBadRequest() {
        assertThatThrownBy(() -> controller.getApproverReceipts(
                null, null, null,
                null, LocalDate.of(2024, 12, 31),
                null, null, 1, 20, null, null, userContext))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("receipt_from_date");
    }

    @Test
    void getApproverReceipts_bothDatesProvided_doesNotThrow() {
        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to   = LocalDate.of(2024, 12, 31);

        when(receiptsService.getApproverReceipts(
                USER_ID, null, null, null, from, to, null, null, 1, 20, null, null))
                .thenReturn(mock(ReceiptGetListApproverResponseDto.class));

        ResponseEntity<ReceiptGetListApproverResponseDto> response = controller.getApproverReceipts(
                null, null, null, from, to, null, null, 1, 20, null, null, userContext);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }
}
