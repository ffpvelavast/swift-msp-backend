/** Service interface defining InvoiceDelegateDetailsProjection operations; implemented by InvoiceDelegateDetailsProjectionImpl. */
package com.rex.commodis.dto.invoiceDtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface InvoiceDelegateDetailsProjection {

    Long getId();
    String getVendor();
    String getDocumentName();
    LocalDate getDocumentDate();
    String getDocumentNumber();
    BigDecimal getInvoiceTotalGross();
    String getCurrency();
    Long getDelegateId();
    String getStatus();
}