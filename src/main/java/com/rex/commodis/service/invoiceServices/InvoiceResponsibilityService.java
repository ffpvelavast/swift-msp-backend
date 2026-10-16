/** Service interface defining InvoiceResponsibility operations; implemented by InvoiceResponsibilityServiceImpl. */
package com.rex.commodis.service.invoiceServices;

import com.rex.commodis.dto.invoiceDtos.InvoiceDelegateRequestDto;
import com.rex.commodis.dto.invoiceDtos.InvoiceDelegateResponseDto;

import java.time.OffsetDateTime;

public interface InvoiceResponsibilityService {

    void validateUserResponsibility(Long invoiceId, Long rexUserId);

    InvoiceDelegateResponseDto delegateInvoice(Long invoiceId, InvoiceDelegateRequestDto requestDto, Long rexUserId);

    void createResponsibility(Long invoiceId, Long rexUserId, OffsetDateTime now);

}
