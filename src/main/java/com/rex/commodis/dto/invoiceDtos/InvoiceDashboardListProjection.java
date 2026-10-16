package com.rex.commodis.dto.invoiceDtos;

import java.math.BigDecimal;

public interface InvoiceDashboardListProjection {
    Long getInvoiceId();
    String getVendorName();
    BigDecimal getInvoiceTotal();
    String getCurrency();
    String getStatus();
}
