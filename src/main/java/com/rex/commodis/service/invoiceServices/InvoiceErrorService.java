/** Service interface defining InvoiceError operations; implemented by InvoiceErrorServiceImpl. */
package com.rex.commodis.service.invoiceServices;

public interface InvoiceErrorService {

    void logRequiredFieldError(
            String tableName,
            String fieldName,
            String documentId,
            Long invoiceId,
            String message,
            Integer errorCode
    );

    void logProjectNumberLookupError(
            String documentId,
            String projectNumber,
            String projectNumberLookupResult,
            String message,
            Integer errorCode
    );

    void logRexUserLookupError(
            Long invoiceId,
            String verifiedBy,
            String rexUserLookupResult,
            String message,
            Integer errorCode
    );
}
