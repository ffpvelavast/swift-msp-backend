/** Service interface defining InvoiceGetListProjectionDto operations; implemented by InvoiceGetListProjectionDtoImpl. */
package com.rex.commodis.dto.invoiceDtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface InvoiceGetListProjectionDto {
    Long getId();
    String getVendor();
    String getDocumentName();
    LocalDate getDocumentDate();
    String getVendorInvoiceNumber();
    BigDecimal getInvoiceTotalGross();
    String getCurrency();
    String getStatus();
}