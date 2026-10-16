package com.rex.commodis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rex.commodis.dto.ReceiptSidePanelHistoryDto;
import com.rex.commodis.dto.ReceiptSidePanelResponseDto;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.model.RexUser;
import com.rex.commodis.model.enums.ReceiptStatusEnum;
import com.rex.commodis.model.expenses.ExpenseReport;
import com.rex.commodis.model.receipts.IssuanceRegion;
import com.rex.commodis.model.receipts.Receipt;
import com.rex.commodis.model.receipts.ReceiptHistory;
import com.rex.commodis.model.receipts.ReceiptStatus;
import com.rex.commodis.model.taxation.TaxReasonClient;
import com.rex.commodis.repository.expenses.ExpenseReportRepository;
import com.rex.commodis.repository.receipts.IssuanceRegionRepository;
import com.rex.commodis.repository.receipts.ReceiptApproverRepository;
import com.rex.commodis.repository.receipts.ReceiptHistoryRepository;
import com.rex.commodis.repository.receipts.ReceiptRepository;
import com.rex.commodis.repository.taxation.TaxCategoryProjection;
import com.rex.commodis.repository.taxation.TaxCategoryRepository;
import com.rex.commodis.repository.taxation.TaxReasonProjection;
import com.rex.commodis.repository.taxation.TaxReasonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiptSidePanelServiceImplTest {

    @Mock private ReceiptRepository receiptRepository;
    @Mock private ReceiptHistoryRepository receiptHistoryRepository;
    @Mock private ExpenseReportRepository expenseReportRepository;
    @Mock private IssuanceRegionRepository issuanceRegionRepository;
    @Mock private ReceiptApproverRepository receiptApproverRepository;
    @Mock private TaxCategoryRepository taxCategoryRepository;
    @Mock private TaxReasonRepository taxReasonRepository;
    @Mock private RexUserService rexUserService;

    @InjectMocks
    private ReceiptSidePanelServiceImpl service;

    private static final Long  RECEIPT_ID             = 100L;
    private static final Long  REX_USER_ID            = 1L;
    private static final Long  PLATFORM_ID            = 10L;
    private static final Long  EXPENSE_REPORT_ID      = 20L;
    private static final Short REGION_ID              = (short) 5;
    private static final Long  TAX_REASON_CLIENT_ID   = 7L;
    private static final Long  TAX_CATEGORY_CLIENT_ID = 3L;
    private static final String LOCALE                = "en_US";

    private static final OffsetDateTime SUBMITTED_AT = OffsetDateTime.of(2024, 6, 1, 10, 0, 0, 0, ZoneOffset.UTC);
    private static final OffsetDateTime APPROVED_AT  = OffsetDateTime.of(2024, 6, 5, 14, 30, 0, 0, ZoneOffset.UTC);
    private static final OffsetDateTime PRIOR_AT     = OffsetDateTime.of(2024, 6, 3, 9, 0, 0, 0, ZoneOffset.UTC);

    private Receipt receipt;

    @BeforeEach
    void setUp() {
        // ObjectMapper is @Autowired (field injection), not constructor-injected.
        ReflectionTestUtils.setField(service, "objectMapper", new ObjectMapper());

        receipt = new Receipt();
        receipt.setId(RECEIPT_ID);
        receipt.setRexUserId(REX_USER_ID);
        receipt.setPlatformId(PLATFORM_ID);
        receipt.setReceiptAmount(new BigDecimal("125.50"));
        receipt.setExpenseReportId(EXPENSE_REPORT_ID);
        receipt.setIssuanceRegionId(REGION_ID);
        receipt.setTaxReasonClientId(TAX_REASON_CLIENT_ID);
        receipt.setCreatedAt(SUBMITTED_AT);
    }

    // ── Happy path ─────────────────────────────────────────────────────────────

    @Test
    void getReceiptSidePanel_fullData_returnsFullyPopulatedResponse() {
        stubLocale();
        stubReceiptFound();
        stubExpenseReport("Q2 Travel");
        stubRegion();
        stubTaxCategoryAndReason("Meals", "Business meal");

        ReceiptHistory active = buildHistory(APPROVED_AT, true, ReceiptStatusEnum.APPROVED, buildUser(99L, "John Doe"));
        ReceiptHistory prior  = buildHistory(PRIOR_AT, false, ReceiptStatusEnum.SUBMITTED, buildUser(50L, "Jane Roe"));
        when(receiptHistoryRepository.findByReceiptId(RECEIPT_ID)).thenReturn(List.of(active, prior));

        ReceiptSidePanelResponseDto result = service.getReceiptSidePanel(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getReceiptSidePanelHistoryDto()).hasSize(2);
        assertThat(result.getReceiptSidePanelDataDto().getReceiptAmount()).isEqualTo("125.50");
        assertThat(result.getReceiptSidePanelDataDto().getSubmittedOn()).isEqualTo(SUBMITTED_AT);
        assertThat(result.getReceiptSidePanelDataDto().getApprovedOn()).isEqualTo(APPROVED_AT);
        assertThat(result.getReceiptSidePanelDataDto().getApprovedBy()).isEqualTo("John Doe");
        assertThat(result.getReceiptSidePanelDataDto().getExpenseCategory()).isEqualTo("Meals");
        assertThat(result.getReceiptSidePanelDataDto().getExpenseNature()).isEqualTo("Business meal");
        assertThat(result.getReceiptSidePanelDataDto().getExpenseTitle()).isEqualTo("Q2 Travel");
        assertThat(result.getReceiptSidePanelDataDto().getLocation()).isEqualTo("Germany");
    }

    // ── Error paths ────────────────────────────────────────────────────────────

    @Test
    void getReceiptSidePanel_receiptNotFound_throwsNotFoundException() {
        stubLocale();
        when(receiptRepository.findById(RECEIPT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getReceiptSidePanel(RECEIPT_ID, REX_USER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Receipt not found with ID: " + RECEIPT_ID);
    }

    @Test
    void getReceiptSidePanel_callerIsNotSubmitter_throwsAccessDeniedException() {
        stubLocale();
        receipt.setRexUserId(999L); // different from REX_USER_ID
        stubReceiptFound();

        assertThatThrownBy(() -> service.getReceiptSidePanel(RECEIPT_ID, REX_USER_ID))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("does not have permission");
    }

    // ── Data DTO branches ──────────────────────────────────────────────────────

    @Test
    void getReceiptSidePanel_receiptAmountNull_amountStringIsNull() {
        receipt.setReceiptAmount(null);
        stubMinimalHappyPath();

        ReceiptSidePanelResponseDto result = service.getReceiptSidePanel(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getReceiptSidePanelDataDto().getReceiptAmount()).isNull();
    }

    @Test
    void getReceiptSidePanel_expenseReportIdNull_expenseTitleIsNull() {
        receipt.setExpenseReportId(null);
        stubMinimalHappyPath();

        ReceiptSidePanelResponseDto result = service.getReceiptSidePanel(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getReceiptSidePanelDataDto().getExpenseTitle()).isNull();
    }

    @Test
    void getReceiptSidePanel_expenseReportNotFound_expenseTitleIsNull() {
        stubMinimalHappyPath();
        when(expenseReportRepository.findById(EXPENSE_REPORT_ID)).thenReturn(Optional.empty());

        ReceiptSidePanelResponseDto result = service.getReceiptSidePanel(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getReceiptSidePanelDataDto().getExpenseTitle()).isNull();
    }

    @Test
    void getReceiptSidePanel_issuanceRegionNotFound_locationIsNull() {
        stubMinimalHappyPath();
        when(issuanceRegionRepository.findById(REGION_ID)).thenReturn(Optional.empty());

        ReceiptSidePanelResponseDto result = service.getReceiptSidePanel(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getReceiptSidePanelDataDto().getLocation()).isNull();
    }

    // ── History / approval branches ────────────────────────────────────────────

    @Test
    void getReceiptSidePanel_noHistory_approvedFieldsAreNull() {
        stubMinimalHappyPath();
        when(receiptHistoryRepository.findByReceiptId(RECEIPT_ID)).thenReturn(List.of());

        ReceiptSidePanelResponseDto result = service.getReceiptSidePanel(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getReceiptSidePanelHistoryDto()).isEmpty();
        assertThat(result.getReceiptSidePanelDataDto().getApprovedOn()).isNull();
        assertThat(result.getReceiptSidePanelDataDto().getApprovedBy()).isNull();
    }

    @Test
    void getReceiptSidePanel_noActiveHistoryRow_approvedFieldsAreNull() {
        stubMinimalHappyPath();
        ReceiptHistory inactive = buildHistory(PRIOR_AT, false, ReceiptStatusEnum.SUBMITTED, buildUser(7L, "Jane Roe"));
        when(receiptHistoryRepository.findByReceiptId(RECEIPT_ID)).thenReturn(List.of(inactive));

        ReceiptSidePanelResponseDto result = service.getReceiptSidePanel(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getReceiptSidePanelHistoryDto()).hasSize(1);
        assertThat(result.getReceiptSidePanelDataDto().getApprovedOn()).isNull();
        assertThat(result.getReceiptSidePanelDataDto().getApprovedBy()).isNull();
    }

    @Test
    void getReceiptSidePanel_activeHistoryWithNullCreator_approvedByIsNull() {
        stubMinimalHappyPath();
        ReceiptHistory active = buildHistory(APPROVED_AT, true, ReceiptStatusEnum.APPROVED, null);
        when(receiptHistoryRepository.findByReceiptId(RECEIPT_ID)).thenReturn(List.of(active));

        ReceiptSidePanelResponseDto result = service.getReceiptSidePanel(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getReceiptSidePanelDataDto().getApprovedOn()).isEqualTo(APPROVED_AT);
        assertThat(result.getReceiptSidePanelDataDto().getApprovedBy()).isNull();
    }

    @Test
    void getReceiptSidePanel_historyEntryWithNullReceiptStatus_mapsStatusDtoToNull() {
        stubMinimalHappyPath();
        ReceiptHistory active = new ReceiptHistory();
        active.setActive(true);
        active.setCreatedAt(APPROVED_AT);
        active.setReceiptStatus(null);
        active.setCreator(buildUser(99L, "John Doe"));
        when(receiptHistoryRepository.findByReceiptId(RECEIPT_ID)).thenReturn(List.of(active));

        ReceiptSidePanelResponseDto result = service.getReceiptSidePanel(RECEIPT_ID, REX_USER_ID);

        ReceiptSidePanelHistoryDto dto = result.getReceiptSidePanelHistoryDto().get(0);
        assertThat(dto.getStatus()).isNull();
        assertThat(dto.getStatusCreator()).isNotNull();
        assertThat(dto.getStatusCreator().getFullname()).isEqualTo("John Doe");
    }

    // ── Tax reason / category branches ─────────────────────────────────────────

    @Test
    void getReceiptSidePanel_taxReasonClientIdNull_categoryAndNatureAreNull() {
        receipt.setTaxReasonClientId(null);
        stubMinimalHappyPath();

        ReceiptSidePanelResponseDto result = service.getReceiptSidePanel(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getReceiptSidePanelDataDto().getExpenseCategory()).isNull();
        assertThat(result.getReceiptSidePanelDataDto().getExpenseNature()).isNull();
    }

    @Test
    void getReceiptSidePanel_taxReasonNotFound_categoryAndNatureAreNull() {
        stubMinimalHappyPath();
        when(taxReasonRepository.findById(TAX_REASON_CLIENT_ID)).thenReturn(Optional.empty());

        ReceiptSidePanelResponseDto result = service.getReceiptSidePanel(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getReceiptSidePanelDataDto().getExpenseCategory()).isNull();
        assertThat(result.getReceiptSidePanelDataDto().getExpenseNature()).isNull();
    }

    @Test
    void getReceiptSidePanel_noMatchingTaxReasonOrCategory_bothNull() {
        stubMinimalHappyPath();
        TaxCategoryProjection mismatchedCategory = taxCategory(999L, "Other category");
        TaxReasonProjection mismatchedReason     = taxReason(999L, "Other reason");
        when(taxReasonRepository.findById(TAX_REASON_CLIENT_ID)).thenReturn(Optional.of(buildTaxReasonClient()));
        when(taxCategoryRepository.findTaxCategories(PLATFORM_ID, LOCALE))
                .thenReturn(List.of(mismatchedCategory));
        when(taxReasonRepository.findTaxReasons(TAX_CATEGORY_CLIENT_ID, LOCALE))
                .thenReturn(List.of(mismatchedReason));

        ReceiptSidePanelResponseDto result = service.getReceiptSidePanel(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getReceiptSidePanelDataDto().getExpenseCategory()).isNull();
        assertThat(result.getReceiptSidePanelDataDto().getExpenseNature()).isNull();
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private void stubLocale() {
        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
    }

    private void stubReceiptFound() {
        when(receiptRepository.findById(RECEIPT_ID)).thenReturn(Optional.of(receipt));
    }

    private void stubExpenseReport(String title) {
        ExpenseReport report = new ExpenseReport();
        report.setReportTitle(title);
        when(expenseReportRepository.findById(EXPENSE_REPORT_ID)).thenReturn(Optional.of(report));
    }

    private void stubRegion() {
        IssuanceRegion region = new IssuanceRegion();
        region.setId(REGION_ID);
        region.setRegionCode("DE");
        region.setDisplayText(Map.of("en_US", "Germany"));
        when(issuanceRegionRepository.findById(REGION_ID)).thenReturn(Optional.of(region));
    }

    private void stubTaxCategoryAndReason(String categoryDesc, String reasonDesc) {
        TaxCategoryProjection categoryProjection = taxCategory(TAX_CATEGORY_CLIENT_ID, categoryDesc);
        TaxReasonProjection   reasonProjection   = taxReason(TAX_REASON_CLIENT_ID, reasonDesc);
        when(taxReasonRepository.findById(TAX_REASON_CLIENT_ID)).thenReturn(Optional.of(buildTaxReasonClient()));
        when(taxCategoryRepository.findTaxCategories(PLATFORM_ID, LOCALE))
                .thenReturn(List.of(categoryProjection));
        when(taxReasonRepository.findTaxReasons(TAX_CATEGORY_CLIENT_ID, LOCALE))
                .thenReturn(List.of(reasonProjection));
    }

    /** Sets up the bare minimum for a successful call. Individual tests override what they need. */
    private void stubMinimalHappyPath() {
        stubLocale();
        stubReceiptFound();
        lenient().when(expenseReportRepository.findById(EXPENSE_REPORT_ID))
                .thenReturn(Optional.of(buildReport("Q2 Travel")));
        lenient().when(issuanceRegionRepository.findById(REGION_ID))
                .thenReturn(Optional.of(buildRegion()));
        lenient().when(receiptHistoryRepository.findByReceiptId(RECEIPT_ID))
                .thenReturn(List.of());
        lenient().when(taxReasonRepository.findById(TAX_REASON_CLIENT_ID))
                .thenReturn(Optional.empty());
    }

    private ExpenseReport buildReport(String title) {
        ExpenseReport report = new ExpenseReport();
        report.setReportTitle(title);
        return report;
    }

    private IssuanceRegion buildRegion() {
        IssuanceRegion region = new IssuanceRegion();
        region.setId(REGION_ID);
        region.setRegionCode("DE");
        region.setDisplayText(Map.of("en_US", "Germany"));
        return region;
    }

    private TaxReasonClient buildTaxReasonClient() {
        TaxReasonClient trc = new TaxReasonClient();
        trc.setId(TAX_REASON_CLIENT_ID);
        trc.setTaxCategoryClientId(TAX_CATEGORY_CLIENT_ID);
        return trc;
    }

    private RexUser buildUser(Long id, String fullName) {
        RexUser user = new RexUser();
        user.setId(id);
        user.setFullName(fullName);
        return user;
    }

    private ReceiptHistory buildHistory(OffsetDateTime createdAt, boolean active,
                                        ReceiptStatusEnum statusEnum, RexUser creator) {
        ReceiptStatus status = new ReceiptStatus();
        status.setId((short) 1);
        status.setStatus(statusEnum);
        status.setDisplayText(Map.of("en_US", statusEnum.name()));

        ReceiptHistory history = new ReceiptHistory();
        history.setReceiptId(RECEIPT_ID);
        history.setCreatedAt(createdAt);
        history.setActive(active);
        history.setReceiptStatus(status);
        history.setCreator(creator);
        return history;
    }

    private TaxCategoryProjection taxCategory(Long id, String displaytext) {
        TaxCategoryProjection p = mock(TaxCategoryProjection.class);
        when(p.getTaxCategoryId()).thenReturn(id);
        lenient().when(p.getDisplaytext()).thenReturn(displaytext);
        lenient().when(p.getDescription()).thenReturn(displaytext);
        return p;
    }

    private TaxReasonProjection taxReason(Long id, String displaytext) {
        TaxReasonProjection p = mock(TaxReasonProjection.class);
        when(p.getTaxReasonId()).thenReturn(id);
        lenient().when(p.getDisplaytext()).thenReturn(displaytext);
        lenient().when(p.getDescription()).thenReturn(displaytext);
        return p;
    }
}
