package com.rex.commodis.repository.receipts;

import java.math.BigDecimal;

public interface SubmitterReceiptProjection {
    Long getReceiptId();
    String getReceiptTitle();
    String getCategory();
    BigDecimal getAmount();
    String getCurrency();
    String getStatus();
}