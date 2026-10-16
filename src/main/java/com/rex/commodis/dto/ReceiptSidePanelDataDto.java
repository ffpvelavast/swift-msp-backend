/** Data transfer object carrying ReceiptSidePanelData data between the API layer and the service/client. */
package com.rex.commodis.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptSidePanelDataDto {

    @JsonProperty("receipt_amount")
    private String receiptAmount;

    @JsonProperty("submitted_on")
    private OffsetDateTime submittedOn;

    @JsonProperty("approved_on")
    private OffsetDateTime approvedOn;

    @JsonProperty("approved_by")
    private String approvedBy;

    @JsonProperty("expense_category")
    private String expenseCategory;

    @JsonProperty("expense_nature")
    private String expenseNature;

    @JsonProperty("expense_title")
    private String expenseTitle;

    @JsonProperty("location")
    private String location;
}
