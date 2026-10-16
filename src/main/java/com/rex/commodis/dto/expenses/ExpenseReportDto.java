/** Data transfer object carrying ExpenseReport data between the API layer and the service/client. */
package com.rex.commodis.dto.expenses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
public class ExpenseReportDto {

    @JsonProperty("expensereport_id")
    private Long id;
    private String expenseTitle;
    private LocalDate receiptDate;

    // getters & setters
}
