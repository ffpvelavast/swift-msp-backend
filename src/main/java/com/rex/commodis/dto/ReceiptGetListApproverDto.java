/** Data transfer object carrying ReceiptGetListApprover data between the API layer and the service/client. */
package com.rex.commodis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ReceiptGetListApproverDto{

    @JsonProperty("receipt_id")
    private Long receiptId;

    @JsonProperty("receipt_title")
    private String receiptTitle;

    @JsonProperty("submitted_for")
    private String submittedFor;

    @JsonProperty("receipt_date")
    private LocalDate receiptDate;

    private String category;

    @JsonProperty("receipt_number")
    private String receiptNumber;

    @JsonProperty("receipt_amount")
    private BigDecimal receiptAmount;

    private String currency;

    private String status;
}