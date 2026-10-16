package com.rex.commodis.dto.receipts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubmitterReceiptDto {
    @JsonProperty("receipt_id")
    private Long receiptId;

    @JsonProperty("receiptTitle")
    private String receiptTitle;

    @JsonProperty("category")
    private String category;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("status")
    private String status;
}