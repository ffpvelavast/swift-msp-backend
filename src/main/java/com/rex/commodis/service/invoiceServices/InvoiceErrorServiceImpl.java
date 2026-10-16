/** Service implementation for InvoiceError business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service.invoiceServices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.rex.commodis.model.invoices.InvoiceError;
import com.rex.commodis.repository.invoices.InvoiceErrorRepository;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
@RequiredArgsConstructor
public class InvoiceErrorServiceImpl implements InvoiceErrorService{

    private final InvoiceErrorRepository invoiceErrorRepository;

    @Transactional(propagation = REQUIRES_NEW)
    public void logRequiredFieldError(
            String tableName,
            String fieldName,
            String documentId,
            Long invoiceId,
            String message,
            Integer errorCode
    ) {
        InvoiceError error = InvoiceError.builder()
                .tableName(tableName)
                .fieldName(fieldName)
                .documentId(documentId)
                .invoicesId(invoiceId)
                .errorMessage(message)
                .invoiceErrorCodeId(errorCode)
                .build();

        invoiceErrorRepository.save(error);
    }

    @Transactional(propagation = REQUIRES_NEW)
    public void logProjectNumberLookupError(
            String documentId,
            String projectNumber,
            String projectNumberLookupResult,
            String message,
            Integer errorCode
    ) {
        InvoiceError error = InvoiceError.builder()
                .documentId(documentId)
                .projectnumberInput(projectNumber)
                .projectnumberLookupResult(projectNumberLookupResult)
                .errorMessage(message)
                .invoiceErrorCodeId(errorCode)
                .build();

        invoiceErrorRepository.save(error);
    }

    @Transactional(propagation = REQUIRES_NEW)
    public void logRexUserLookupError(
            Long invoiceId,
            String verifiedBy,
            String rexUserLookupResult,
            String message,
            Integer errorCode
    ) {
        InvoiceError error = InvoiceError.builder()
                .invoicesId(invoiceId)
                .verifiedBy(verifiedBy)
                .rexuserLookupResult(rexUserLookupResult)
                .errorMessage(message)
                .invoiceErrorCodeId(errorCode)
                .build();

        invoiceErrorRepository.save(error);
    }
}
