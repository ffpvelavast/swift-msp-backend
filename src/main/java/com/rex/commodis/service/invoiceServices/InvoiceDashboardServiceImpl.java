/** Service implementation for InvoiceDashboard business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service.invoiceServices;

import com.rex.commodis.dto.invoiceDtos.InvoiceDashboardCountDto;
import com.rex.commodis.dto.invoiceDtos.InvoiceDashboardListDto;
import com.rex.commodis.dto.invoiceDtos.InvoiceDashboardListProjection;
import com.rex.commodis.dto.invoiceDtos.InvoiceDashboardListResponseDto;
import com.rex.commodis.repository.invoices.InvoiceHistoryRepository;
import com.rex.commodis.service.RexUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceDashboardServiceImpl implements InvoiceDashboardService {

    private final InvoiceHistoryRepository invoiceHistoryRepository;
    private final RexUserService rexUserService;

    @Override
    public InvoiceDashboardCountDto getDashboardInvoicesCount(Long rexUserId) {
        Long count = invoiceHistoryRepository.getDashboardInvoicesCount(rexUserId);
        return InvoiceDashboardCountDto.builder()
                .invoicesCount(count != null ? count : 0L)
                .build();
    }

    @Override
    public InvoiceDashboardListResponseDto getDashboardInvoicesList(Long rexUserId) {
        String locale = rexUserService.getLocale(rexUserId);

        List<InvoiceDashboardListProjection> projections =
                invoiceHistoryRepository.getDashboardInvoicesList(rexUserId, locale);

        List<InvoiceDashboardListDto> data = projections.stream()
                .map(p -> InvoiceDashboardListDto.builder()
                        .invoiceId(p.getInvoiceId())
                        .vendorName(p.getVendorName())
                        .invoiceTotal(p.getInvoiceTotal())
                        .currency(p.getCurrency())
                        .status(p.getStatus())
                        .build())
                .toList();

        return InvoiceDashboardListResponseDto.builder()
                .data(data)
                .build();
    }
}