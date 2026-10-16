package com.rex.commodis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rex.commodis.dto.receipts.ReceiptDetailResponseDto;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.model.RexUser;
import com.rex.commodis.model.receipts.IssuanceRegion;
import com.rex.commodis.model.receipts.Receipt;
import com.rex.commodis.model.receipts.ReceiptFile;
import com.rex.commodis.model.receipts.ReceiptHistory;
import com.rex.commodis.model.receipts.ReceiptStatus;
import com.rex.commodis.model.taxation.TaxReasonClient;
import com.rex.commodis.repository.RexUserRepository;
import com.rex.commodis.repository.expenses.ExpenseReportRepository;
import com.rex.commodis.repository.receipts.IssuanceRegionRepository;
import com.rex.commodis.repository.receipts.ReceiptApproverRepository;
import com.rex.commodis.repository.receipts.ReceiptFileRepository;
import com.rex.commodis.repository.receipts.ReceiptHistoryRepository;
import com.rex.commodis.repository.receipts.ReceiptRepository;
import com.rex.commodis.repository.receipts.ReceiptToBeneficiaryRepository;
import com.rex.commodis.repository.receipts.ReceiptToGuestRepository;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiptDetailsServiceImplTest {

    @Mock private ReceiptRepository receiptRepository;
    @Mock private ReceiptHistoryRepository receiptHistoryRepository;
    @Mock private IssuanceRegionRepository issuanceRegionRepository;
    @Mock private ExpenseReportRepository expenseReportRepository;
    @Mock private ReceiptToBeneficiaryRepository receiptToBeneficiaryRepository;
    @Mock private ReceiptToGuestRepository receiptToGuestRepository;
    @Mock private RexUserRepository rexUserRepository;
    @Mock private ReceiptApproverRepository receiptApproverRepository;
    @Mock private TaxCategoryRepository taxCategoryRepository;
    @Mock private TaxReasonRepository taxReasonRepository;
    @Mock private ReceiptFileRepository receiptFileRepository;
    @Mock private PlatformConfigurationService platformConfigurationService;
    @Mock private RexUserService rexUserService;

    @InjectMocks
    private ReceiptDetailsServiceImpl service;

    private static final Long  RECEIPT_ID            = 100L;
    private static final Long  REX_USER_ID           = 1L;
    private static final Long  PLATFORM_ID           = 10L;
    private static final Long  SUBMITTER_ID          = 2L;
    private static final Long  TAX_REASON_CLIENT_ID  = 7L;
    private static final Long  TAX_CATEGORY_CLIENT_ID = 3L;
    private static final Short REGION_ID             = (short) 5;
    private static final String LOCALE               = "en_US";
    private static final String BASE_URL             = "https://files.rex.com";

    private Receipt receipt;

    @BeforeEach
    void setUp() {
        // ObjectMapper is @Autowired (field injection), not via @RequiredArgsConstructor,
        // so we set it manually after Mockito constructs the service.
        ReflectionTestUtils.setField(service, "objectMapper", new ObjectMapper());

        receipt = new Receipt();
        receipt.setId(RECEIPT_ID);
        receipt.setPlatformId(PLATFORM_ID);
        receipt.setRexUserId(SUBMITTER_ID);
        receipt.setReceiptTitle("Client lunch");
        receipt.setReceiptDate(LocalDate.of(2024, 6, 15));
        receipt.setReceiptAmount(new BigDecimal("125.50"));
        receipt.setIssuanceRegionId(REGION_ID);
        receipt.setTaxReasonClientId(TAX_REASON_CLIENT_ID);
        receipt.setExpenseReportId(20L);
        receipt.setInternalNote("internal note");
    }

    // ── Happy path ─────────────────────────────────────────────────────────────

    @Test
    void getReceiptDetails_fullData_returnsFullyPopulatedResponse() {
        stubApproverOk();
        stubReceiptFound();
        stubLocale();

        ReceiptHistory history    = mock(ReceiptHistory.class);
        ReceiptStatus  status     = mock(ReceiptStatus.class);
        IssuanceRegion region     = buildRegion();
        TaxReasonClient taxReason = buildTaxReasonClient();

        when(history.getReceiptStatus()).thenReturn(status);
        when(history.getExternalNote()).thenReturn("external note");
        when(status.getDisplayText()).thenReturn(Map.of("en_US", "Submitted"));

        TaxCategoryProjection categoryProjection = taxCategory(TAX_CATEGORY_CLIENT_ID, "Meals");
        TaxReasonProjection   reasonProjection   = taxReason(TAX_REASON_CLIENT_ID, "Business meal");

        when(receiptHistoryRepository.findActiveByReceiptId(RECEIPT_ID)).thenReturn(Optional.of(history));
        when(issuanceRegionRepository.findById(REGION_ID)).thenReturn(Optional.of(region));
        when(receiptToBeneficiaryRepository.findBeneficiaryNamesByReceiptId(RECEIPT_ID))
                .thenReturn(List.of("Alice Smith", "Bob Jones"));
        when(receiptToGuestRepository.findGuestNamesByReceiptId(RECEIPT_ID))
                .thenReturn(List.of("Charlie Brown"));
        when(rexUserRepository.findById(SUBMITTER_ID)).thenReturn(Optional.of(buildUser("John", "Doe")));
        when(taxReasonRepository.findById(TAX_REASON_CLIENT_ID)).thenReturn(Optional.of(taxReason));
        when(taxCategoryRepository.findTaxCategories(PLATFORM_ID, LOCALE))
                .thenReturn(List.of(categoryProjection));
        when(taxReasonRepository.findTaxReasons(TAX_CATEGORY_CLIENT_ID, LOCALE))
                .thenReturn(List.of(reasonProjection));

        UUID fileUuid = UUID.randomUUID();
        stubFileLookup(buildFile(1, fileUuid, "image/png"));

        ReceiptDetailResponseDto result = service.getReceiptDetails(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getReceiptId()).isEqualTo(RECEIPT_ID);
        assertThat(result.getReceiptTitle()).isEqualTo("Client lunch");
        assertThat(result.getReceiptDate()).isEqualTo(LocalDate.of(2024, 6, 15));
        assertThat(result.getReceiptAmount()).isEqualTo("125.50");
        assertThat(result.getExpenseCategory()).isEqualTo("Meals");
        assertThat(result.getExpenseNature()).isEqualTo("Business meal");
        assertThat(result.getSubmittedFor()).isEqualTo("John Doe");
        assertThat(result.getInternalNote()).isEqualTo("internal note");
        assertThat(result.getExternalNote()).isEqualTo("external note");
        assertThat(result.getStatus()).isEqualTo("Submitted");
        assertThat(result.getLocation()).isEqualTo("Germany");
        assertThat(result.getReceiptToBeneficiaries().getFullnames()).containsExactly("Alice Smith", "Bob Jones");
        assertThat(result.getReceiptToGuests().getFullnames()).containsExactly("Charlie Brown");
        assertThat(result.getFiles()).hasSize(1);
        assertThat(result.getFiles().get(0).getFileUrl())
                .isEqualTo(BASE_URL + "/" + PLATFORM_ID + "/" + fileUuid);
        assertThat(result.getFiles().get(0).getMediaType()).isEqualTo("image/png");
        assertThat(result.getFiles().get(0).getSequence()).isEqualTo(1);
    }

    // ── Error paths ────────────────────────────────────────────────────────────

    @Test
    void getReceiptDetails_receiptNotFound_throwsNotFoundException() {
        stubLocale();
        when(receiptRepository.findById(RECEIPT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getReceiptDetails(RECEIPT_ID, REX_USER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Receipt not found with ID: " + RECEIPT_ID);
    }

    @Test
    void getReceiptDetails_userIsNotApprover_throwsAccessDeniedException() {
        stubLocale();
        stubReceiptFound();
        when(receiptApproverRepository.existsByReceiptIdAndRexUserId(RECEIPT_ID, REX_USER_ID))
                .thenReturn(false);

        assertThatThrownBy(() -> service.getReceiptDetails(RECEIPT_ID, REX_USER_ID))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("does not have permission");
    }

    // ── Receipt history / status branches ──────────────────────────────────────

    @Test
    void getReceiptDetails_noActiveHistory_statusAndExternalNoteAreNull() {
        stubMinimalHappyPath();
        when(receiptHistoryRepository.findActiveByReceiptId(RECEIPT_ID)).thenReturn(Optional.empty());

        ReceiptDetailResponseDto result = service.getReceiptDetails(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getStatus()).isNull();
        assertThat(result.getExternalNote()).isNull();
    }

    @Test
    void getReceiptDetails_historyHasNullStatus_statusDtoIsNull() {
        stubMinimalHappyPath();
        ReceiptHistory history = mock(ReceiptHistory.class);
        when(history.getReceiptStatus()).thenReturn(null);
        when(history.getExternalNote()).thenReturn("ext");
        when(receiptHistoryRepository.findActiveByReceiptId(RECEIPT_ID)).thenReturn(Optional.of(history));

        ReceiptDetailResponseDto result = service.getReceiptDetails(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getStatus()).isNull();
        assertThat(result.getExternalNote()).isEqualTo("ext");
    }

    // ── Tax reason / category branches ─────────────────────────────────────────

    @Test
    void getReceiptDetails_taxReasonClientIdIsNull_categoryAndNatureAreNull() {
        receipt.setTaxReasonClientId(null);
        stubMinimalHappyPath();
        when(receiptHistoryRepository.findActiveByReceiptId(RECEIPT_ID)).thenReturn(Optional.empty());

        ReceiptDetailResponseDto result = service.getReceiptDetails(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getExpenseCategory()).isNull();
        assertThat(result.getExpenseNature()).isNull();
    }

    @Test
    void getReceiptDetails_taxReasonClientNotFound_categoryAndNatureAreNull() {
        stubMinimalHappyPath();
        when(receiptHistoryRepository.findActiveByReceiptId(RECEIPT_ID)).thenReturn(Optional.empty());
        when(taxReasonRepository.findById(TAX_REASON_CLIENT_ID)).thenReturn(Optional.empty());

        ReceiptDetailResponseDto result = service.getReceiptDetails(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getExpenseCategory()).isNull();
        assertThat(result.getExpenseNature()).isNull();
    }

    @Test
    void getReceiptDetails_categoryListEmpty_categoryIsNullButNatureResolved() {
        stubMinimalHappyPath();
        when(receiptHistoryRepository.findActiveByReceiptId(RECEIPT_ID)).thenReturn(Optional.empty());
        TaxReasonProjection reasonProjection = taxReason(TAX_REASON_CLIENT_ID, "Business meal");
        when(taxReasonRepository.findById(TAX_REASON_CLIENT_ID)).thenReturn(Optional.of(buildTaxReasonClient()));
        when(taxCategoryRepository.findTaxCategories(PLATFORM_ID, LOCALE)).thenReturn(List.of());
        when(taxReasonRepository.findTaxReasons(TAX_CATEGORY_CLIENT_ID, LOCALE))
                .thenReturn(List.of(reasonProjection));

        ReceiptDetailResponseDto result = service.getReceiptDetails(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getExpenseCategory()).isNull();
        assertThat(result.getExpenseNature()).isEqualTo("Business meal");
    }

    @Test
    void getReceiptDetails_noMatchingTaxReason_natureIsNull() {
        stubMinimalHappyPath();
        TaxCategoryProjection categoryProjection = taxCategory(TAX_CATEGORY_CLIENT_ID, "Meals");
        TaxReasonProjection   reasonProjection   = taxReason(999L, "Other reason"); // different id
        when(receiptHistoryRepository.findActiveByReceiptId(RECEIPT_ID)).thenReturn(Optional.empty());
        when(taxReasonRepository.findById(TAX_REASON_CLIENT_ID)).thenReturn(Optional.of(buildTaxReasonClient()));
        when(taxCategoryRepository.findTaxCategories(PLATFORM_ID, LOCALE))
                .thenReturn(List.of(categoryProjection));
        when(taxReasonRepository.findTaxReasons(TAX_CATEGORY_CLIENT_ID, LOCALE))
                .thenReturn(List.of(reasonProjection));

        ReceiptDetailResponseDto result = service.getReceiptDetails(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getExpenseCategory()).isEqualTo("Meals");
        assertThat(result.getExpenseNature()).isNull();
    }

    // ── Misc field branches ────────────────────────────────────────────────────

    @Test
    void getReceiptDetails_submitterUserNotFound_submittedForIsNull() {
        stubApproverOk();
        stubReceiptFound();
        stubLocale();
        when(receiptHistoryRepository.findActiveByReceiptId(RECEIPT_ID)).thenReturn(Optional.empty());
        when(issuanceRegionRepository.findById(REGION_ID)).thenReturn(Optional.of(buildRegion()));
        when(receiptToBeneficiaryRepository.findBeneficiaryNamesByReceiptId(RECEIPT_ID)).thenReturn(List.of());
        when(receiptToGuestRepository.findGuestNamesByReceiptId(RECEIPT_ID)).thenReturn(List.of());
        when(rexUserRepository.findById(SUBMITTER_ID)).thenReturn(Optional.empty());
        when(taxReasonRepository.findById(TAX_REASON_CLIENT_ID)).thenReturn(Optional.empty());
        stubFileLookup(); // no files

        ReceiptDetailResponseDto result = service.getReceiptDetails(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getSubmittedFor()).isNull();
    }

    @Test
    void getReceiptDetails_issuanceRegionNotFound_locationIsNull() {
        stubMinimalHappyPath();
        when(receiptHistoryRepository.findActiveByReceiptId(RECEIPT_ID)).thenReturn(Optional.empty());
        when(issuanceRegionRepository.findById(REGION_ID)).thenReturn(Optional.empty());

        ReceiptDetailResponseDto result = service.getReceiptDetails(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getLocation()).isNull();
    }

    @Test
    void getReceiptDetails_receiptAmountIsNull_amountStringIsNull() {
        receipt.setReceiptAmount(null);
        stubMinimalHappyPath();
        when(receiptHistoryRepository.findActiveByReceiptId(RECEIPT_ID)).thenReturn(Optional.empty());

        ReceiptDetailResponseDto result = service.getReceiptDetails(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getReceiptAmount()).isNull();
    }

    @Test
    void getReceiptDetails_multipleFiles_mapsAllWithCorrectUrls() {
        stubApproverOk();
        stubReceiptFound();
        stubLocale();
        when(receiptHistoryRepository.findActiveByReceiptId(RECEIPT_ID)).thenReturn(Optional.empty());
        when(issuanceRegionRepository.findById(REGION_ID)).thenReturn(Optional.of(buildRegion()));
        when(receiptToBeneficiaryRepository.findBeneficiaryNamesByReceiptId(RECEIPT_ID)).thenReturn(List.of());
        when(receiptToGuestRepository.findGuestNamesByReceiptId(RECEIPT_ID)).thenReturn(List.of());
        when(rexUserRepository.findById(SUBMITTER_ID)).thenReturn(Optional.of(buildUser("Jane", "Roe")));
        when(taxReasonRepository.findById(TAX_REASON_CLIENT_ID)).thenReturn(Optional.empty());
        UUID uuidA = UUID.randomUUID();
        UUID uuidB = UUID.randomUUID();
        stubFileLookup(
                buildFile(1, uuidA, "image/png"),
                buildFile(2, uuidB, "application/pdf")
        );

        ReceiptDetailResponseDto result = service.getReceiptDetails(RECEIPT_ID, REX_USER_ID);

        assertThat(result.getFiles()).hasSize(2);
        assertThat(result.getFiles().get(0).getFileUrl())
                .isEqualTo(BASE_URL + "/" + PLATFORM_ID + "/" + uuidA);
        assertThat(result.getFiles().get(0).getMediaType()).isEqualTo("image/png");
        assertThat(result.getFiles().get(1).getFileUrl())
                .isEqualTo(BASE_URL + "/" + PLATFORM_ID + "/" + uuidB);
        assertThat(result.getFiles().get(1).getMediaType()).isEqualTo("application/pdf");
    }

    // ── Stub helpers ───────────────────────────────────────────────────────────

    private void stubApproverOk() {
        when(receiptApproverRepository.existsByReceiptIdAndRexUserId(RECEIPT_ID, REX_USER_ID))
                .thenReturn(true);
    }

    private void stubReceiptFound() {
        when(receiptRepository.findById(RECEIPT_ID)).thenReturn(Optional.of(receipt));
    }

    private void stubLocale() {
        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
    }

    private void stubFileLookup(ReceiptFile... files) {
        // platformConfigurationService is invoked from getReceiptFiles(); use lenient
        // so tests that don't reach that step don't fail with UnnecessaryStubbing.
        lenient().when(platformConfigurationService.getReceiptStorageBaseUrl(PLATFORM_ID))
                .thenReturn(BASE_URL);
        when(receiptFileRepository.findByReceiptIdOrderByReceiptNumberSeq(RECEIPT_ID))
                .thenReturn(List.of(files));
    }

    /** Sets up the bare minimum for a successful run: approver ok, receipt found, locale,
     *  empty/null lookups for everything else. Individual tests override what they need. */
    private void stubMinimalHappyPath() {
        stubApproverOk();
        stubReceiptFound();
        stubLocale();
        lenient().when(issuanceRegionRepository.findById(REGION_ID)).thenReturn(Optional.of(buildRegion()));
        lenient().when(receiptToBeneficiaryRepository.findBeneficiaryNamesByReceiptId(RECEIPT_ID))
                .thenReturn(List.of());
        lenient().when(receiptToGuestRepository.findGuestNamesByReceiptId(RECEIPT_ID))
                .thenReturn(List.of());
        lenient().when(rexUserRepository.findById(SUBMITTER_ID))
                .thenReturn(Optional.of(buildUser("Jane", "Roe")));
        lenient().when(taxReasonRepository.findById(TAX_REASON_CLIENT_ID))
                .thenReturn(Optional.empty());
        lenient().when(platformConfigurationService.getReceiptStorageBaseUrl(PLATFORM_ID))
                .thenReturn(BASE_URL);
        lenient().when(receiptFileRepository.findByReceiptIdOrderByReceiptNumberSeq(RECEIPT_ID))
                .thenReturn(List.of());
    }

    // ── Object builders ────────────────────────────────────────────────────────

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

    private RexUser buildUser(String first, String last) {
        RexUser user = new RexUser();
        user.setFirstname(first);
        user.setLastname(last);
        return user;
    }

    private ReceiptFile buildFile(Integer seq, UUID uuid, String mediaType) {
        ReceiptFile file = new ReceiptFile();
        file.setReceiptNumberSeq(seq);
        file.setReceiptFileId(uuid);
        file.setFilename(PLATFORM_ID + "/" + uuid);
        file.setMediaType(mediaType);
        return file;
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
