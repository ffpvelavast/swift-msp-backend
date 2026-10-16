/** Response DTO returned by the ReceiptDropDown API endpoint. */
package com.rex.commodis.dto.receipts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDropDownResponseDto {

    private List<BookableEventDto> events;
    private List<ExpenseReportDropDownDto> expenseReports;
    private List<IssuanceRegionDropDownDto> issuanceRegions;
    private List<ExpenseCategoryDropDownDto> expenseCategories;

}
