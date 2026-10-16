/** Service interface defining InvoicePayload operations; implemented by InvoicePayloadServiceImpl. */
package com.rex.commodis.service.invoiceServices;


public interface InvoicePayloadService {

    void saveTransfer(String rawJson);
}
