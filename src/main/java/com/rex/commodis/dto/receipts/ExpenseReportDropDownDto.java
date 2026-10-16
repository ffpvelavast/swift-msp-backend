/** Data transfer object carrying ExpenseReportDropDown data between the API layer and the service/client. */
package com.rex.commodis.dto.receipts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseReportDropDownDto {

    private Long id;
    private String expenseReportTitle;
    private LocalDate reportDateFrom;
    private LocalDate reportDateThrough;
}
