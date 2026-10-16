/** Service implementation for ReceiptDetails business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rex.commodis.dto.BeneficiaryNamesDto;
import com.rex.commodis.dto.receipts.ReceiptDetailResponseDto;
import com.rex.commodis.dto.receipts.ReceiptFileResponseDto;
import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.model.expenses.ExpenseReport;
import com.rex.commodis.model.receipts.Receipt;
import com.rex.commodis.model.receipts.ReceiptFile;
import com.rex.commodis.model.receipts.ReceiptHistory;
import com.rex.commodis.model.receipts.receiptApprover.ReceiptApprover;
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiptDetailsServiceImpl implements ReceiptDetailsService {

    private final ReceiptRepository receiptRepository;
    private final ReceiptHistoryRepository receiptHistoryRepository;
    private final IssuanceRegionRepository issuanceRegionRepository;
    private final ExpenseReportRepository expenseReportRepository;
    private final ReceiptToBeneficiaryRepository receiptToBeneficiaryRepository;
    private final ReceiptToGuestRepository receiptToGuestRepository;
    private final RexUserRepository rexUserRepository;
    private final ReceiptApproverRepository receiptApproverRepository;
    private final TaxCategoryRepository taxCategoryRepository;
    private final TaxReasonRepository taxReasonRepository;
    private final ReceiptFileRepository receiptFileRepository;
    private final PlatformConfigurationService platformConfigurationService;
    private final RexUserService rexUserService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional(readOnly = true)
    public ReceiptDetailResponseDto getReceiptDetails(Long receiptId, Long rexUserId) {

        String locale = rexUserService.getLocale(rexUserId);

        // Fetch receipt base data
        Receipt dbReceipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new NotFoundException(
                        "Receipt not found with ID: " + receiptId
                ));

        //TODO check if the rexUser has permissions to access this receipt
        if (!receiptApproverRepository.existsByReceiptIdAndRexUserId(receiptId, rexUserId)) {
            throw new AccessDeniedException(
                    "User does not have permission to change this receipt"
            );
        }

        ReceiptHistory currentDbReceiptHistory = receiptHistoryRepository
                .findActiveByReceiptId(receiptId)
                .orElse(null);

        String status = null;
        if (currentDbReceiptHistory != null && currentDbReceiptHistory.getReceiptStatus() != null) {
            status = currentDbReceiptHistory.getReceiptStatus()
                    .getDisplayText()
                    .get(locale);
        }

        String location = issuanceRegionRepository.findById(dbReceipt.getIssuanceRegionId())
                .map(region -> region.getDisplayText().get(locale))
                .orElse(null);


        //  Resolve expense report (category & nature)
        ExpenseReport expenseReport = resolveExpenseReport(dbReceipt.getExpenseReportId());

        //  Resolve submitted_for from rexuser firstname and lastname
        String submittedForFullName = resolveSubmittedFor(dbReceipt.getRexUserId());

        //  Fetch internal beneficiary names (receipt_to_beneficiary)
        List<String> internalNames = receiptToBeneficiaryRepository
                .findBeneficiaryNamesByReceiptId(receiptId);

        //  Fetch external beneficiary names (receipt_to_guest)
        List<String> externalNames = receiptToGuestRepository
                .findGuestNamesByReceiptId(receiptId);

        // ── Resolve expenseCategory and expenseNature ─────────────────────────
        String expenseCategory = null;
        String expenseNature = null;

        Long taxReasonClientId = dbReceipt.getTaxReasonClientId();
        
        if (taxReasonClientId != null) {

            TaxReasonClient taxReasonClient = taxReasonRepository.findById(taxReasonClientId)
                    .orElse(null);

            if (taxReasonClient != null) {
                Long taxCategoryClientId = taxReasonClient.getTaxCategoryClientId();

                // expenseCategory ← filter findTaxCategories list by taxCategoryClientId
                var categories = taxCategoryRepository.findTaxCategories(dbReceipt.getPlatformId(), locale);
                if (categories != null) {
                    expenseCategory = categories.stream()
                            .filter(c -> c.getTaxCategoryId().equals(taxCategoryClientId))
                            .map(TaxCategoryProjection::getDisplaytext)
                            .filter(Objects::nonNull)
                            .findFirst()
                            .orElse(null);
                }

                // expenseNature ← filter findTaxReasons list by taxReasonClientId
                var reasons = taxReasonRepository.findTaxReasons(taxCategoryClientId, locale);
                if (reasons != null) {
                    expenseNature = reasons.stream()
                            .filter(r -> Objects.equals(r.getTaxReasonId(), taxReasonClientId))
                            .map(TaxReasonProjection::getDisplaytext)
                            .filter(Objects::nonNull)
                            .findFirst()
                            .orElse(null);
                }
                
                if (expenseNature == null) {
                    log.warn(
                        "Missing description for taxReasonClientId={} platformId={}",
                        taxReasonClientId,
                        dbReceipt.getPlatformId()
                    );
                }
            }
        }
// ─────────────────────────────────────────────────────────────────────

        //  Assemble and return the response DTO
        return ReceiptDetailResponseDto.builder()
                .receiptId(dbReceipt.getId())
                .receiptTitle(dbReceipt.getReceiptTitle())
                .receiptDate(dbReceipt.getReceiptDate())
                .expenseCategory(expenseCategory)
                .expenseNature(expenseNature)
                .location(location)
                //   .teamBalanceDeduct(dbReceipt.getTeamBalanceDeduct())
                .teamBalanceDeduct(null)
                .receiptAmount(dbReceipt.getReceiptAmount() != null
                        ? dbReceipt.getReceiptAmount().toPlainString()
                        : null)
                .status(status)
                .submittedFor(submittedForFullName)

                .receiptToBeneficiaries(BeneficiaryNamesDto.builder()
                        .fullnames(internalNames)
                        .build())
                .receiptToGuests(BeneficiaryNamesDto.builder()
                        .fullnames(externalNames)
                        .build())
                .internalNote(dbReceipt.getInternalNote())
                .externalNote(
                        currentDbReceiptHistory != null
                                ? currentDbReceiptHistory.getExternalNote()
                                : null
                )
                .files(getReceiptFiles(receiptId))
                .build();
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private ExpenseReport resolveExpenseReport(Long expenseReportId) {
        return expenseReportRepository.findById(expenseReportId).orElse(null);
    }

    private String resolveSubmittedFor(Long rexUserId) {
        return rexUserRepository.findById(rexUserId)
                .map(u -> u.getFirstname() + " " + u.getLastname())
                .orElse(null);
    }

    private Map<String, String> parseDisplayText(String jsonbText) {
        if (jsonbText == null) return Collections.emptyMap();

        try {
            return objectMapper.readValue(
                    jsonbText,
                    new TypeReference<Map<String, String>>() {}
            );
        } catch (Exception e) {
            log.warn("Failed to parse display_text JSONB: {}", jsonbText);
            return Map.of("raw", jsonbText);
        }
    }

    public List<ReceiptFileResponseDto> getReceiptFiles(Long receiptId) {

        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new NotFoundException("Receipt not found"));

        Long platformId = receipt.getPlatformId();

        String baseUrl = platformConfigurationService.getReceiptStorageBaseUrl(platformId);

        List<ReceiptFile> files =receiptFileRepository.findByReceiptIdOrderByReceiptNumberSeq(receiptId);

        return files.stream()
                .map(file -> ReceiptFileResponseDto.builder()
                        .sequence(file.getReceiptNumberSeq())
                        .fileUrl(baseUrl + "/" + file.getFilename())
                        .mediaType(file.getMediaType())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public void createApprovers(Long receiptId, List<Long> rexUserIds) {

        if (receiptId == null || rexUserIds == null || rexUserIds.isEmpty()) {
            throw new BadRequestException("ReceiptId and approver list must not be null/empty");
        }

        List<ReceiptApprover> approvers = rexUserIds.stream()
                .distinct()
                .map(rexUserId -> {
                    ReceiptApprover approver = new ReceiptApprover();
                    approver.setReceiptId(receiptId);
                    approver.setRexUserId(rexUserId);
                    approver.setCreatedAt(OffsetDateTime.now());
                    return approver;
                })
                .toList();

        receiptApproverRepository.saveAll(approvers);
    }
}
