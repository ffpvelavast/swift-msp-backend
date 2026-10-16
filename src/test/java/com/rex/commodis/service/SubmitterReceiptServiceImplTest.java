package com.rex.commodis.service;

import com.rex.commodis.dto.receipts.SubmitterReceiptDto;
import com.rex.commodis.repository.receipts.SubmitterReceiptProjection;
import com.rex.commodis.repository.receipts.SubmitterReceiptRepository;
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
class SubmitterReceiptServiceImplTest {

    @Mock private SubmitterReceiptRepository submitterReceiptRepository;
    @Mock private RexUserService rexUserService;

    @InjectMocks
    private SubmitterReceiptServiceImpl service;

    private static final Long REX_USER_ID = 1L;
    private static final String LOCALE = "en_US";

    @Test
    void getSubmitterReceipts_mapsRepositoryProjectionsToDtoList() {
        SubmitterReceiptProjection firstReceipt = receipt(
                100L,
                "Taxi receipt",
                "Travel",
                new BigDecimal("42.50"),
                "EUR",
                "SUBMITTED"
        );
        SubmitterReceiptProjection secondReceipt = receipt(
                101L,
                "Dinner receipt",
                "Meals",
                new BigDecimal("80.00"),
                "USD",
                "RESUBMITTED"
        );
        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(submitterReceiptRepository.fetchSubmitterReceipts(REX_USER_ID, LOCALE))
                .thenReturn(List.of(firstReceipt, secondReceipt));

        List<SubmitterReceiptDto> result = service.getSubmitterReceipts(REX_USER_ID);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getReceiptId()).isEqualTo(100L);
        assertThat(result.get(0).getReceiptTitle()).isEqualTo("Taxi receipt");
        assertThat(result.get(0).getCategory()).isEqualTo("Travel");
        assertThat(result.get(0).getAmount()).isEqualByComparingTo("42.50");
        assertThat(result.get(0).getCurrency()).isEqualTo("EUR");
        assertThat(result.get(0).getStatus()).isEqualTo("SUBMITTED");
        assertThat(result.get(1).getReceiptId()).isEqualTo(101L);
        assertThat(result.get(1).getReceiptTitle()).isEqualTo("Dinner receipt");
        assertThat(result.get(1).getCategory()).isEqualTo("Meals");
        assertThat(result.get(1).getAmount()).isEqualByComparingTo("80.00");
        assertThat(result.get(1).getCurrency()).isEqualTo("USD");
        assertThat(result.get(1).getStatus()).isEqualTo("RESUBMITTED");
        verify(rexUserService).getLocale(REX_USER_ID);
        verify(submitterReceiptRepository).fetchSubmitterReceipts(REX_USER_ID, LOCALE);
    }

    @Test
    void getSubmitterReceipts_whenRepositoryReturnsNoRows_returnsEmptyList() {
        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(submitterReceiptRepository.fetchSubmitterReceipts(REX_USER_ID, LOCALE))
                .thenReturn(List.of());

        List<SubmitterReceiptDto> result = service.getSubmitterReceipts(REX_USER_ID);

        assertThat(result).isEmpty();
        verify(rexUserService).getLocale(REX_USER_ID);
        verify(submitterReceiptRepository).fetchSubmitterReceipts(REX_USER_ID, LOCALE);
    }

    private SubmitterReceiptProjection receipt(
            Long receiptId,
            String receiptTitle,
            String category,
            BigDecimal amount,
            String currency,
            String status
    ) {
        return new SubmitterReceiptProjection() {
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
                return category;
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
