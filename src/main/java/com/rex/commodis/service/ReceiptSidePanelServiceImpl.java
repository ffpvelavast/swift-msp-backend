/** Service implementation for ReceiptSidePanel business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rex.commodis.model.taxation.TaxReasonClient;
import com.rex.commodis.repository.taxation.TaxCategoryProjection;
import com.rex.commodis.repository.taxation.TaxCategoryRepository;
import com.rex.commodis.repository.taxation.TaxReasonProjection;
import com.rex.commodis.repository.taxation.TaxReasonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.rex.commodis.dto.ReceiptSidePanelDataDto;
import com.rex.commodis.dto.ReceiptSidePanelHistoryDto;
import com.rex.commodis.dto.ReceiptSidePanelResponseDto;
import com.rex.commodis.dto.ReceiptStatusCreatorDto;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.model.expenses.ExpenseReport;
import com.rex.commodis.model.receipts.Receipt;
import com.rex.commodis.model.receipts.ReceiptHistory;
import com.rex.commodis.repository.expenses.ExpenseReportRepository;
import com.rex.commodis.repository.receipts.IssuanceRegionRepository;
import com.rex.commodis.repository.receipts.ReceiptApproverRepository;
import com.rex.commodis.repository.receipts.ReceiptHistoryRepository;
import com.rex.commodis.repository.receipts.ReceiptRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReceiptSidePanelServiceImpl implements ReceiptSidePanelService {

    private final ReceiptRepository receiptRepository;
    private final ReceiptHistoryRepository receiptHistoryRepository;
    private final ExpenseReportRepository expenseReportRepository;
    private final IssuanceRegionRepository issuanceRegionRepository;
    private final ReceiptApproverRepository receiptApproverRepository;
    private final TaxCategoryRepository taxCategoryRepository;
    private final TaxReasonRepository taxReasonRepository;
    private final RexUserService rexUserService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ReceiptSidePanelResponseDto getReceiptSidePanel(Long receiptId, Long rexUserId) {

        String locale = rexUserService.getLocale(rexUserId);

        // 1. Fetch receipt
        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new NotFoundException(
                        "Receipt not found with ID: " + receiptId
                ));

        // TODO: Check permissions
        if (!receipt.getRexUserId().equals(rexUserId)) {
            throw new AccessDeniedException(
                    "User does not have permission to view this receipt"
            );
        }

        // 2. Receipt amount
        String receiptAmount = receipt.getReceiptAmount() != null
                ? receipt.getReceiptAmount().toPlainString()
                : null;

        // 3. Expense report
        ExpenseReport expenseReport = receipt.getExpenseReportId() != null
                ? expenseReportRepository.findById(receipt.getExpenseReportId()).orElse(null)
                : null;

        // 4. Location (JSONB → already mapped as Map, so direct)

        String location = issuanceRegionRepository.findById(receipt.getIssuanceRegionId())
                .map(region -> region.getDisplayText().get(locale))
                .orElse(null);

        // 5. History (timeline) → MISSING earlier
        List<ReceiptSidePanelHistoryDto> receiptSidePanelHistoryDtos =
                receiptHistoryRepository.findByReceiptId(receiptId)
                        .stream()
                        .map(history -> mapToReceiptSidePanelHistoryDto(history, locale))
                        .toList();

        // 6. Submitted date (FIXED condition)
        OffsetDateTime submittedOn = receipt.getCreatedAt();

        // 7. Approved date (from ACTIVE history)
        ReceiptSidePanelHistoryDto approvedReceiptSidePanelHistoryDto = receiptSidePanelHistoryDtos.stream()
                .filter(historyDto -> Boolean.TRUE.equals(historyDto.getIsActive()))
                .findFirst()
                .orElse(null);

        OffsetDateTime approvedOn = approvedReceiptSidePanelHistoryDto != null
                ? approvedReceiptSidePanelHistoryDto.getCreatedAt()
                : null;

        // 8. Approved by
        String approvedBy =
                approvedReceiptSidePanelHistoryDto != null
                && approvedReceiptSidePanelHistoryDto.getStatusCreator() != null
                ? approvedReceiptSidePanelHistoryDto.getStatusCreator().getFullname()
                : null;

        String expenseTitle = expenseReport != null
                ? expenseReport.getReportTitle()
                : null;


        String expenseCategory = null;
        String expenseNature = null;

        if (receipt.getTaxReasonClientId() != null) {

            TaxReasonClient taxReason = taxReasonRepository
                    .findById(receipt.getTaxReasonClientId())
                    .orElse(null);

            if (taxReason != null) {

                // expenseNature ← taxreason description
                var reasons = taxReasonRepository.findTaxReasons(taxReason.getTaxCategoryClientId(), locale);
                if (reasons != null) {
                    expenseNature = reasons.stream()
                            .filter(r -> r.getTaxReasonId().equals(receipt.getTaxReasonClientId()))
                            .map(TaxReasonProjection::getDisplaytext)
                            .filter(Objects::nonNull)
                            .findFirst()
                            .orElse(null);
                }

                // expenseCategory ← taxcategory description
                var categories = taxCategoryRepository.findTaxCategories(receipt.getPlatformId(), locale);
                if (categories != null) {
                    expenseCategory = categories.stream()
                            .filter(c -> c.getTaxCategoryId().equals(taxReason.getTaxCategoryClientId()))
                            .map(TaxCategoryProjection::getDisplaytext)
                            .filter(Objects::nonNull)
                            .findFirst()
                            .orElse(null);
                }
            }
        }



        // 9. Build data DTO
        ReceiptSidePanelDataDto receiptSidePanelDataDto = ReceiptSidePanelDataDto.builder()
                .receiptAmount(receiptAmount)
                .submittedOn(submittedOn)
                .approvedOn(approvedOn)
                .approvedBy(approvedBy)
                .expenseCategory(expenseCategory)
                .expenseNature(expenseNature)
                .expenseTitle(expenseTitle)
                .location(location) // ✅ fixed (single source)
                .build();

        return ReceiptSidePanelResponseDto.builder()
                .receiptSidePanelDataDto(receiptSidePanelDataDto)
                .receiptSidePanelHistoryDto(receiptSidePanelHistoryDtos)
                .build();
    }
    
    
    private ReceiptSidePanelHistoryDto mapToReceiptSidePanelHistoryDto(ReceiptHistory receiptHistory , String locale) {

        String status = null;
        if (receiptHistory.getReceiptStatus() != null) {
            status = receiptHistory.getReceiptStatus()
                    .getDisplayText()
                    .get(locale);
        }

        ReceiptStatusCreatorDto receiptStatusCreatorDto = receiptHistory.getCreator() != null
                ? ReceiptStatusCreatorDto.builder()
                    .id(receiptHistory.getCreator().getId())
                    .fullname(receiptHistory.getCreator().getFullName())
                    .build()
                : null;

        return ReceiptSidePanelHistoryDto.builder()
                .createdAt(receiptHistory.getCreatedAt())
                .status(status)
                .isActive(receiptHistory.getActive())
                .statusCreator(receiptStatusCreatorDto)
                .build();
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

    
}