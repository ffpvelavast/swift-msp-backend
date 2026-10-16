package com.rex.commodis.service;

import com.rex.commodis.dto.receipts.DashboardReceiptApproverCountResponseDto;
import com.rex.commodis.repository.receipts.ReceiptRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardReceiptApproverServiceImplTest {

    @Mock private ReceiptRepository receiptRepository;

    @InjectMocks
    private DashboardReceiptApproverServiceImpl service;

    private static final Long REX_USER_ID = 1L;

    @Test
    void getApproverReceiptCount_returnsRepositoryCount() {
        when(receiptRepository.getApproverReceiptCount(REX_USER_ID)).thenReturn(5L);

        DashboardReceiptApproverCountResponseDto result =
                service.getApproverReceiptCount(REX_USER_ID);

        assertThat(result.getReceipt_approver_count()).isEqualTo(5L);
        verify(receiptRepository).getApproverReceiptCount(REX_USER_ID);
    }

    @Test
    void getApproverReceiptCount_whenRepositoryReturnsNull_returnsZero() {
        when(receiptRepository.getApproverReceiptCount(REX_USER_ID)).thenReturn(null);

        DashboardReceiptApproverCountResponseDto result =
                service.getApproverReceiptCount(REX_USER_ID);

        assertThat(result.getReceipt_approver_count()).isZero();
        verify(receiptRepository).getApproverReceiptCount(REX_USER_ID);
    }
}
