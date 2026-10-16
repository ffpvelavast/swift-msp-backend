package com.rex.commodis.service.invoiceServices;

import com.rex.commodis.dto.invoiceDtos.InvoiceDashboardCountDto;
import com.rex.commodis.dto.invoiceDtos.InvoiceDashboardListProjection;
import com.rex.commodis.dto.invoiceDtos.InvoiceDashboardListResponseDto;
import com.rex.commodis.repository.invoices.InvoiceHistoryRepository;
import com.rex.commodis.service.RexUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceDashboardServiceImplTest {

    @Mock private InvoiceHistoryRepository invoiceHistoryRepository;
    @Mock private RexUserService rexUserService;

    @InjectMocks
    private InvoiceDashboardServiceImpl invoiceDashboardService;

    private static final Long REX_USER_ID = 1L;
    private static final String LOCALE = "en_US";

    @Test
    void getDashboardInvoicesCount_returnsRepositoryCount() {
        when(invoiceHistoryRepository.getDashboardInvoicesCount(REX_USER_ID)).thenReturn(7L);

        InvoiceDashboardCountDto result =
                invoiceDashboardService.getDashboardInvoicesCount(REX_USER_ID);

        assertThat(result.getInvoicesCount()).isEqualTo(7L);
        verify(invoiceHistoryRepository).getDashboardInvoicesCount(REX_USER_ID);
    }

    @Test
    void getDashboardInvoicesCount_whenRepositoryReturnsNull_returnsZero() {
        when(invoiceHistoryRepository.getDashboardInvoicesCount(REX_USER_ID)).thenReturn(null);

        InvoiceDashboardCountDto result =
                invoiceDashboardService.getDashboardInvoicesCount(REX_USER_ID);

        assertThat(result.getInvoicesCount()).isZero();
        verify(invoiceHistoryRepository).getDashboardInvoicesCount(REX_USER_ID);
    }

    @Test
    void getDashboardInvoicesList_mapsRepositoryProjectionsToResponseData() {
        InvoiceDashboardListProjection firstInvoice = invoice(
                100L,
                "Acme GmbH",
                new BigDecimal("125.50"),
                "EUR",
                "PENDING"
        );
        InvoiceDashboardListProjection secondInvoice = invoice(
                101L,
                "Globex AG",
                new BigDecimal("250.00"),
                "USD",
                "DRAFT"
        );
        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(invoiceHistoryRepository.getDashboardInvoicesList(REX_USER_ID, LOCALE))
                .thenReturn(List.of(firstInvoice, secondInvoice));

        InvoiceDashboardListResponseDto result =
                invoiceDashboardService.getDashboardInvoicesList(REX_USER_ID);

        assertThat(result.getData()).hasSize(2);
        assertThat(result.getData().get(0).getInvoiceId()).isEqualTo(100L);
        assertThat(result.getData().get(0).getVendorName()).isEqualTo("Acme GmbH");
        assertThat(result.getData().get(0).getInvoiceTotal()).isEqualByComparingTo("125.50");
        assertThat(result.getData().get(0).getCurrency()).isEqualTo("EUR");
        assertThat(result.getData().get(0).getStatus()).isEqualTo("PENDING");
        assertThat(result.getData().get(1).getInvoiceId()).isEqualTo(101L);
        assertThat(result.getData().get(1).getVendorName()).isEqualTo("Globex AG");
        assertThat(result.getData().get(1).getInvoiceTotal()).isEqualByComparingTo("250.00");
        assertThat(result.getData().get(1).getCurrency()).isEqualTo("USD");
        assertThat(result.getData().get(1).getStatus()).isEqualTo("DRAFT");
        verify(rexUserService).getLocale(REX_USER_ID);
        verify(invoiceHistoryRepository).getDashboardInvoicesList(REX_USER_ID, LOCALE);
    }

    @Test
    void getDashboardInvoicesList_whenRepositoryReturnsNoRows_returnsEmptyData() {
        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(invoiceHistoryRepository.getDashboardInvoicesList(REX_USER_ID, LOCALE))
                .thenReturn(List.of());

        InvoiceDashboardListResponseDto result =
                invoiceDashboardService.getDashboardInvoicesList(REX_USER_ID);

        assertThat(result.getData()).isEmpty();
        verify(rexUserService).getLocale(REX_USER_ID);
        verify(invoiceHistoryRepository).getDashboardInvoicesList(REX_USER_ID, LOCALE);
    }

    private InvoiceDashboardListProjection invoice(
            Long invoiceId,
            String vendorName,
            BigDecimal invoiceTotal,
            String currency,
            String status
    ) {
        return new InvoiceDashboardListProjection() {
            @Override
            public Long getInvoiceId() {
                return invoiceId;
            }

            @Override
            public String getVendorName() {
                return vendorName;
            }

            @Override
            public BigDecimal getInvoiceTotal() {
                return invoiceTotal;
            }

            @Override
            public String getCurrency() {
                return currency;
            }

            @Override
            public String getStatus() {
                return status;
            }
        };
    }
}
