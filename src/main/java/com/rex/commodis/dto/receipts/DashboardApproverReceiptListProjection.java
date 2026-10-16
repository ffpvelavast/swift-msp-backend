/** Service interface defining DashboardApproverReceiptListProjection operations; implemented by DashboardApproverReceiptListProjectionImpl. */
package com.rex.commodis.dto.receipts;

import java.math.BigDecimal;

public interface DashboardApproverReceiptListProjection {

    Long getReceiptId();

    String getReceiptTitle();

    String getCategory();

    BigDecimal getAmount();

    String getCurrency();

    String getStatus();
}