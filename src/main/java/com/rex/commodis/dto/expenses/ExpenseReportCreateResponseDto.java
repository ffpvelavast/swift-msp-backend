/** Response DTO returned by the ExpenseReportCreate API endpoint. */
package com.rex.commodis.dto.expenses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseReportCreateResponseDto {

    @JsonProperty("expense_reports_id")
    private Long expenseReportId;
}
