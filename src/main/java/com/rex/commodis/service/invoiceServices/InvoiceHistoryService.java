/** Service interface defining InvoiceHistory operations; implemented by InvoiceHistoryServiceImpl. */
package com.rex.commodis.service.invoiceServices;

import com.rex.commodis.model.RexUser;
import com.rex.commodis.model.enums.InvoiceStatusEnum;

public interface InvoiceHistoryService {

    void validateDuplicateStatus(Long invoiceId, InvoiceStatusEnum status);

    void createStatus(Long invoiceId, InvoiceStatusEnum status, String fullName);
}
