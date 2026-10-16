/** Service implementation for InvoicePayload business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service.invoiceServices;

import com.rex.commodis.model.invoices.InvoicePayload;
import com.rex.commodis.repository.invoices.InvoicePayloadRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
@RequiredArgsConstructor
public class InvoicePayloadServiceImpl implements InvoicePayloadService{

    private final InvoicePayloadRepository invoicePayloadRepository;

    @Transactional(propagation = REQUIRES_NEW)
    public void saveTransfer(String rawJson) {
        InvoicePayload invoicePayload = InvoicePayload.builder()
                .payload(rawJson)
                .build();

        invoicePayloadRepository.save(invoicePayload);
    }
}
