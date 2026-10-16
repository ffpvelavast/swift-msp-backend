/** Request DTO for the ExpenseReportCreate operation; validated and passed from the controller to the service layer. */
package com.rex.commodis.dto.expenses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseReportCreateRequestDto {

    @NotBlank(message = "Report title is required")
    @JsonProperty("report_title")
    private String reportTitle;

    @NotNull(message = "Report from date is required")
    @JsonProperty("reportdate_from")
    private LocalDate reportDateFrom;

    @NotNull(message = "Report through date is required")
    @JsonProperty("reportdate_through")
    private LocalDate reportDateThrough;
}