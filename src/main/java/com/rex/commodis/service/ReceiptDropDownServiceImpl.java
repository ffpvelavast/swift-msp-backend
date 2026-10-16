/** Service implementation for ReceiptDropDown business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.repository.events.TeamEventRepository;
import com.rex.commodis.repository.taxation.TaxCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.rex.commodis.dto.receipts.*;
import com.rex.commodis.repository.expenses.ExpenseReportRepository;
import com.rex.commodis.repository.receipts.IssuanceRegionRepository;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptDropDownServiceImpl implements ReceiptDropDownService {

    private final ExpenseReportRepository expenseReportRepository;
    private final IssuanceRegionRepository issuanceRegionRepository;
    private final TeamEventRepository teamEventRepository;
    private final TaxCategoryRepository taxCategoryRepository;
    private final RexUserService rexUserService;

    public ReceiptDropDownResponseDto getDropDowns(Long rexUserId, Long platformId){

        String locale = rexUserService.getLocale(rexUserId);

        //Run function bookable_events, map output of query to projection,
        //map output of projection to DTO
        List<BookableEventDto> bookableEventDtos = teamEventRepository.findBookableEvents(rexUserId)
                .stream()
                .map(event -> new BookableEventDto(
                        event.getEventSource(),
                        event.getEventId(),
                        event.getEventTitle(),
                        event.getInheritParticipants()
                ))
                .toList();

        //Run function select_expense_reports, map output of query to projection,
        //map output of projection to DTO
        List<ExpenseReportDropDownDto> expenseReportDropDownDtos = expenseReportRepository.findExpenseReports(rexUserId)
                .stream()
                .map(expenseReport -> new ExpenseReportDropDownDto(
                        expenseReport.getId(),
                        expenseReport.getReportTitle(),
                        expenseReport.getReportDateFrom(),
                        expenseReport.getReportDateThrough()
                ))
                .toList();

        //Run function select_receipt_issuanceregion, map output of query to projection,
        //map output of projection to DTO
        List<IssuanceRegionDropDownDto> issuanceRegionDropDownDtos = issuanceRegionRepository
                .findIssuanceRegions(platformId, locale) //Need to remove hardcoding once locale becomes a feature
                .stream()
                .map(issuanceRegion -> new IssuanceRegionDropDownDto(
                        issuanceRegion.getIssuanceRegionId(),
                        issuanceRegion.getRegionCode(),
                        issuanceRegion.getDisplayText(),
                        issuanceRegion.getDescription()
                ))
                .toList();

        //Run function select_taxcategory, map output of query to projection,
        //map output of projection to DTO
        List<ExpenseCategoryDropDownDto> expenseCategoryDropDownDtos = taxCategoryRepository.findTaxCategories(platformId, locale)
                .stream()
                .filter(taxCategory ->
                        StringUtils.hasText(taxCategory.getDisplaytext()))
                .map(taxCategory -> new ExpenseCategoryDropDownDto(
                        taxCategory.getTaxCategoryId(),
                        taxCategory.getDisplaytext(),
                        taxCategory.getDescription()
                )).toList();

        return new ReceiptDropDownResponseDto(bookableEventDtos, expenseReportDropDownDtos,
                issuanceRegionDropDownDtos, expenseCategoryDropDownDtos);
    }
}
