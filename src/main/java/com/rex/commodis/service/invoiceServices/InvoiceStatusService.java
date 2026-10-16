package com.rex.commodis.service.invoiceServices;

import com.rex.commodis.model.invoices.InvoiceStatus;

public interface InvoiceStatusService {

    InvoiceStatus getByStatus(String status);

    void createOrUpdateDraftStatus(Long invoiceId, Long rexUserId);
}
