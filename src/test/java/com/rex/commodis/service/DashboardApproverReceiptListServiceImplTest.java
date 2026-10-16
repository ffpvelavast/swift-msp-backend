package com.rex.commodis.service;

import com.rex.commodis.dto.receipts.DashboardApproverReceiptListProjection;
import com.rex.commodis.dto.receipts.DashboardApproverReceiptListResponseDto;
import com.rex.commodis.repository.receipts.DashboardApproverReceiptListRepository;
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
class DashboardApproverReceiptListServiceImplTest {

    @Mock private DashboardApproverReceiptListRepository repository;
    @Mock private RexUserService rexUserService;

    @InjectMocks
    private DashboardApproverReceiptListServiceImpl service;

    private static final Long REX_USER_ID = 1L;
    private static final String LOCALE = "en_US";

    @Test
    void getDashboardApproverReceiptList_mapsRepositoryProjectionsToResponseData() {
        DashboardApproverReceiptListProjection firstReceipt = receipt(
                100L,
                "Taxi receipt",
                new BigDecimal("42.50"),
                "EUR",
                "SUBMITTED"
        );
        DashboardApproverReceiptListProjection secondReceipt = receipt(
                101L,
                "Dinner receipt",
                new BigDecimal("80.00"),
                "USD",
                "RESUBMITTED"
        );
        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(repository.getDashboardApproverReceiptList(REX_USER_ID, LOCALE))
                .thenReturn(List.of(firstReceipt, secondReceipt));

        DashboardApproverReceiptListResponseDto result =
                service.getDashboardApproverReceiptList(REX_USER_ID);

        assertThat(result.getData()).hasSize(2);
        assertThat(result.getData().get(0).getReceiptId()).isEqualTo(100L);
        assertThat(result.getData().get(0).getReceiptTitle()).isEqualTo("Taxi receipt");
        assertThat(result.getData().get(0).getAmount()).isEqualByComparingTo("42.50");
        assertThat(result.getData().get(0).getCurrency()).isEqualTo("EUR");
        assertThat(result.getData().get(0).getStatus()).isEqualTo("SUBMITTED");
        assertThat(result.getData().get(1).getReceiptId()).isEqualTo(101L);
        assertThat(result.getData().get(1).getReceiptTitle()).isEqualTo("Dinner receipt");
        assertThat(result.getData().get(1).getAmount()).isEqualByComparingTo("80.00");
        assertThat(result.getData().get(1).getCurrency()).isEqualTo("USD");
        assertThat(result.getData().get(1).getStatus()).isEqualTo("RESUBMITTED");
        verify(rexUserService).getLocale(REX_USER_ID);
        verify(repository).getDashboardApproverReceiptList(REX_USER_ID, LOCALE);
    }

    @Test
    void getDashboardApproverReceiptList_whenRepositoryReturnsNoRows_returnsEmptyData() {
        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(repository.getDashboardApproverReceiptList(REX_USER_ID, LOCALE)).thenReturn(List.of());

        DashboardApproverReceiptListResponseDto result =
                service.getDashboardApproverReceiptList(REX_USER_ID);

        assertThat(result.getData()).isEmpty();
        verify(rexUserService).getLocale(REX_USER_ID);
        verify(repository).getDashboardApproverReceiptList(REX_USER_ID, LOCALE);
    }

    private DashboardApproverReceiptListProjection receipt(
            Long receiptId,
            String receiptTitle,
            BigDecimal amount,
            String currency,
            String status
    ) {
        return new DashboardApproverReceiptListProjection() {
            @Override
            public Long getReceiptId() {
                return receiptId;
            }

            @Override
            public String getReceiptTitle() {
                return receiptTitle;
            }

            @Override
            public String getCategory() {
                return "Travel";
            }

            @Override
            public BigDecimal getAmount() {
                return amount;
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
