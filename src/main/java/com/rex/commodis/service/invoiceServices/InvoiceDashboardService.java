/** Service interface defining InvoiceDashboard operations; implemented by InvoiceDashboardServiceImpl. */
package com.rex.commodis.service.invoiceServices;

import com.rex.commodis.dto.invoiceDtos.InvoiceDashboardCountDto;
import com.rex.commodis.dto.invoiceDtos.InvoiceDashboardListResponseDto;

public interface InvoiceDashboardService {

    InvoiceDashboardCountDto getDashboardInvoicesCount(Long rexUserId);

    InvoiceDashboardListResponseDto getDashboardInvoicesList(Long rexUserId);
}
