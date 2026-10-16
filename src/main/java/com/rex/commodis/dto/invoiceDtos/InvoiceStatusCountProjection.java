/** Service interface defining InvoiceStatusCountProjection operations; implemented by InvoiceStatusCountProjectionImpl. */
package com.rex.commodis.dto.invoiceDtos;

public interface InvoiceStatusCountProjection {
    String getStatus();
    Long getCount();
}
