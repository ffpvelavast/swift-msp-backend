package com.rex.commodis.service;

import com.rex.commodis.dto.receipts.CreateReceiptRequestDto;
import com.rex.commodis.dto.receipts.CreateReceiptResponseDto;
import com.rex.commodis.dto.receipts.ReceiptEventRequestDto;
import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.model.ProjectLeader;
import com.rex.commodis.model.receipts.Receipt;
import com.rex.commodis.repository.ProjectLeaderRepository;
import com.rex.commodis.repository.receipts.ReceiptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceImplTest {

    @Mock private ReceiptRepository receiptRepository;
    @Mock private ReceiptNumberGeneratorService receiptNumberGeneratorService;
    @Mock private ReceiptHistoryService receiptHistoryService;
    @Mock private ReceiptFileService receiptFileService;
    @Mock private ReceiptToBeneficiaryService receiptToBeneficiaryService;
    @Mock private ReceiptToGuestService receiptToGuestService;
    @Mock private ReceiptToCorporateEventService receiptToCorporateEventService;
    @Mock private ReceiptToTeamEventService receiptToTeamEventService;
    @Mock private ReceiptDetailsService receiptDetailsService;
    @Mock private ProjectLeaderRepository projectLeaderRepository;

    @InjectMocks
    private ReceiptServiceImpl receiptService;

    private static final Long PLATFORM_ID       = 10L;
    private static final Long REX_USER_ID       = 1L;
    private static final Long RECEIPT_ID        = 200L;
    private static final Long PROJECT_NUMBER_ID = 50L;
    private static final Long APPROVER_ID       = 5L;
    private static final String RECEIPT_NUMBER  = "REC-2024-001";

    private CreateReceiptRequestDto request;
    private List<MultipartFile> files;

    @BeforeEach
    void setUp() {
        request = buildBaseRequest();
        files = List.of(mock(MultipartFile.class));
    }

    // ── Happy paths ────────────────────────────────────────────────────────────

    @Test
    void createReceipt_happyPath_noEvent_orchestratesAllStepsAndReturnsReceiptId() {
        stubReceiptSave();
        stubReceiptNumber();
        stubProjectLeaders(APPROVER_ID);

        CreateReceiptResponseDto response =
                receiptService.createReceipt(request, PLATFORM_ID, REX_USER_ID, files);

        assertThat(response.getReceiptId()).isEqualTo(RECEIPT_ID);

        verify(receiptNumberGeneratorService).generateUniqueReceiptCode();
        verify(receiptRepository).save(any(Receipt.class));
        verify(receiptHistoryService).createReceiptHistory(RECEIPT_ID, REX_USER_ID);
        verify(projectLeaderRepository).findByProjectNumberId(PROJECT_NUMBER_ID);
        verify(receiptDetailsService).createApprovers(RECEIPT_ID, List.of(APPROVER_ID));
        verify(receiptFileService).createReceiptFile(RECEIPT_ID, PLATFORM_ID, files);
        verify(receiptToBeneficiaryService).saveBeneficiaries(RECEIPT_ID, request.getBeneficiaryIds());
        verify(receiptToGuestService).saveGuests(RECEIPT_ID, request.getGuestIds());
        verifyNoInteractions(receiptToCorporateEventService, receiptToTeamEventService);
    }

    @Test
    void createReceipt_persistsReceiptEntityWithAllFieldsFromRequest() {
        stubReceiptSave();
        stubReceiptNumber();
        stubProjectLeaders(APPROVER_ID);

        receiptService.createReceipt(request, PLATFORM_ID, REX_USER_ID, files);

        ArgumentCaptor<Receipt> captor = ArgumentCaptor.forClass(Receipt.class);
        verify(receiptRepository).save(captor.capture());
        Receipt saved = captor.getValue();

        assertThat(saved.getReceiptNumber()).isEqualTo(RECEIPT_NUMBER);
        assertThat(saved.getPlatformId()).isEqualTo(PLATFORM_ID);
        assertThat(saved.getRexUserId()).isEqualTo(REX_USER_ID);
        assertThat(saved.getExpenseReportId()).isEqualTo(request.getExpenseReportId());
        assertThat(saved.getIssuanceRegionId()).isEqualTo(request.getIssuanceRegionId());
        assertThat(saved.getReceiptTitle()).isEqualTo(request.getReceiptTitle());
        assertThat(saved.getReceiptDate()).isEqualTo(request.getReceiptDate());
        assertThat(saved.getReceiptAmount()).isEqualTo(request.getReceiptAmount());
        assertThat(saved.getProjectNumberId()).isEqualTo(PROJECT_NUMBER_ID);
        assertThat(saved.getReceiptCurrency()).isEqualTo(request.getReceiptCurrency());
        assertThat(saved.getGratuity()).isEqualTo(request.getGratuity());
        assertThat(saved.getAmountNetReducedVat()).isEqualTo(request.getAmountNetReducedVat());
        assertThat(saved.getAmountNetFullVat()).isEqualTo(request.getAmountNetFullVat());
        assertThat(saved.getVatRateReduced()).isEqualTo(request.getVatRateReduced());
        assertThat(saved.getVatRateFull()).isEqualTo(request.getVatRateFull());
        assertThat(saved.getDeductTeamBalance()).isTrue();
        assertThat(saved.getTaxReasonClientId()).isEqualTo(request.getTaxReasonClientId());
    }

    @Test
    void createReceipt_deductTeamBalanceNull_defaultsToFalse() {
        request.setDeductTeamBalance(null);
        stubReceiptSave();
        stubReceiptNumber();
        stubProjectLeaders(APPROVER_ID);

        receiptService.createReceipt(request, PLATFORM_ID, REX_USER_ID, files);

        ArgumentCaptor<Receipt> captor = ArgumentCaptor.forClass(Receipt.class);
        verify(receiptRepository).save(captor.capture());
        assertThat(captor.getValue().getDeductTeamBalance()).isFalse();
    }

    @Test
    void createReceipt_multipleApprovers_passesAllApproverIdsToCreateApprovers() {
        stubReceiptSave();
        stubReceiptNumber();
        stubProjectLeaders(5L, 6L, 7L);

        receiptService.createReceipt(request, PLATFORM_ID, REX_USER_ID, files);

        verify(receiptDetailsService).createApprovers(RECEIPT_ID, List.of(5L, 6L, 7L));
    }

    @Test
    void createReceipt_nullFiles_passesNullToFileService() {
        stubReceiptSave();
        stubReceiptNumber();
        stubProjectLeaders(APPROVER_ID);

        receiptService.createReceipt(request, PLATFORM_ID, REX_USER_ID, null);

        verify(receiptFileService).createReceiptFile(RECEIPT_ID, PLATFORM_ID, null);
    }

    // ── Event branching ────────────────────────────────────────────────────────

    @Test
    void createReceipt_corporateEvent_callsCorporateEventService() {
        request.setReceiptEvent(buildEvent("CORPORATE_EVENT", 99L));
        stubReceiptSave();
        stubReceiptNumber();
        stubProjectLeaders(APPROVER_ID);

        receiptService.createReceipt(request, PLATFORM_ID, REX_USER_ID, files);

        verify(receiptToCorporateEventService).saveCorporateEvent(RECEIPT_ID, 99L);
        verifyNoInteractions(receiptToTeamEventService);
    }

    @Test
    void createReceipt_teamEvent_callsTeamEventService() {
        request.setReceiptEvent(buildEvent("TEAM_EVENT", 77L));
        stubReceiptSave();
        stubReceiptNumber();
        stubProjectLeaders(APPROVER_ID);

        receiptService.createReceipt(request, PLATFORM_ID, REX_USER_ID, files);

        verify(receiptToTeamEventService).saveTeamEvent(RECEIPT_ID, 77L);
        verifyNoInteractions(receiptToCorporateEventService);
    }

    @Test
    void createReceipt_eventIsNull_skipsEventServices() {
        request.setReceiptEvent(null);
        stubReceiptSave();
        stubReceiptNumber();
        stubProjectLeaders(APPROVER_ID);

        receiptService.createReceipt(request, PLATFORM_ID, REX_USER_ID, files);

        verifyNoInteractions(receiptToCorporateEventService, receiptToTeamEventService);
    }

    @Test
    void createReceipt_eventSourceIsNull_skipsEventServices() {
        request.setReceiptEvent(buildEvent(null, 99L));
        stubReceiptSave();
        stubReceiptNumber();
        stubProjectLeaders(APPROVER_ID);

        receiptService.createReceipt(request, PLATFORM_ID, REX_USER_ID, files);

        verifyNoInteractions(receiptToCorporateEventService, receiptToTeamEventService);
    }

    @Test
    void createReceipt_eventSourceIsEmpty_skipsEventServices() {
        request.setReceiptEvent(buildEvent("", 99L));
        stubReceiptSave();
        stubReceiptNumber();
        stubProjectLeaders(APPROVER_ID);

        receiptService.createReceipt(request, PLATFORM_ID, REX_USER_ID, files);

        verifyNoInteractions(receiptToCorporateEventService, receiptToTeamEventService);
    }

    @Test
    void createReceipt_eventIdIsZero_skipsEventServices() {
        request.setReceiptEvent(buildEvent("CORPORATE_EVENT", 0L));
        stubReceiptSave();
        stubReceiptNumber();
        stubProjectLeaders(APPROVER_ID);

        receiptService.createReceipt(request, PLATFORM_ID, REX_USER_ID, files);

        verifyNoInteractions(receiptToCorporateEventService, receiptToTeamEventService);
    }

    // ── Error cases ────────────────────────────────────────────────────────────

    @Test
    void createReceipt_noApproversFound_throwsBadRequestException() {
        stubReceiptSave();
        stubReceiptNumber();
        when(projectLeaderRepository.findByProjectNumberId(PROJECT_NUMBER_ID))
                .thenReturn(Collections.emptyList());

        assertThatThrownBy(() ->
                receiptService.createReceipt(request, PLATFORM_ID, REX_USER_ID, files))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("No approvers found for given project");

        verify(receiptDetailsService, never()).createApprovers(anyLong(), anyList());
        verify(receiptFileService, never()).createReceiptFile(anyLong(), anyLong(), anyList());
        verifyNoInteractions(receiptToBeneficiaryService, receiptToGuestService,
                receiptToCorporateEventService, receiptToTeamEventService);
    }

    @Test
    void createReceipt_unknownEventSource_throwsBadRequestException() {
        request.setReceiptEvent(buildEvent("UNKNOWN_SOURCE", 1L));
        stubReceiptSave();
        stubReceiptNumber();
        stubProjectLeaders(APPROVER_ID);

        assertThatThrownBy(() ->
                receiptService.createReceipt(request, PLATFORM_ID, REX_USER_ID, files))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Event Source Name does not match");

        verifyNoInteractions(receiptToCorporateEventService, receiptToTeamEventService);
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private CreateReceiptRequestDto buildBaseRequest() {
        CreateReceiptRequestDto dto = new CreateReceiptRequestDto();
        dto.setExpenseReportId(20L);
        dto.setIssuanceRegionId((short) 1);
        dto.setReceiptTitle("Lunch with client");
        dto.setReceiptDate(LocalDate.of(2024, 6, 15));
        dto.setReceiptAmount(new BigDecimal("125.50"));
        dto.setReceiptCurrency("EUR");
        dto.setProjectNumberId(PROJECT_NUMBER_ID);
        dto.setTaxReasonClientId(7L);
        dto.setGratuity(new BigDecimal("5.00"));
        dto.setAmountNetReducedVat(new BigDecimal("80.00"));
        dto.setAmountNetFullVat(new BigDecimal("40.50"));
        dto.setVatRateReduced((short) 7);
        dto.setVatRateFull((short) 19);
        dto.setDeductTeamBalance(true);
        dto.setBeneficiaryIds(List.of(101L, 102L));
        dto.setGuestIds(List.of(201L));
        dto.setReceiptFileTypeId(1L);
        dto.setReceiptEvent(null);
        return dto;
    }

    private ReceiptEventRequestDto buildEvent(String source, Long id) {
        ReceiptEventRequestDto event = new ReceiptEventRequestDto();
        event.setEventSource(source);
        event.setEventId(id);
        return event;
    }

    private void stubReceiptNumber() {
        when(receiptNumberGeneratorService.generateUniqueReceiptCode()).thenReturn(RECEIPT_NUMBER);
    }

    private void stubReceiptSave() {
        when(receiptRepository.save(any(Receipt.class))).thenAnswer(invocation -> {
            Receipt input = invocation.getArgument(0);
            input.setId(RECEIPT_ID);
            return input;
        });
    }

    private void stubProjectLeaders(Long... rexUserIds) {
        List<ProjectLeader> leaders = java.util.Arrays.stream(rexUserIds)
                .map(id -> {
                    ProjectLeader leader = new ProjectLeader();
                    leader.setRexUserId(id);
                    return leader;
                })
                .toList();
        when(projectLeaderRepository.findByProjectNumberId(PROJECT_NUMBER_ID)).thenReturn(leaders);
    }
}
