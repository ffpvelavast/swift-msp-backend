package com.rex.commodis.service;

import com.rex.commodis.dto.ReceiptStatusCountDto;
import com.rex.commodis.dto.ReceiptStatusCountProjection;
import com.rex.commodis.model.enums.ReceiptStatusEnum;
import com.rex.commodis.repository.receipts.ReceiptRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiptCountServiceImplTest {

    @Mock private ReceiptRepository receiptRepository;

    @InjectMocks
    private ReceiptCountServiceImpl receiptCountService;

    private static final Long REX_USER_ID = 1L;

    @Test
    void getSubmitterReceiptCounts_mapsRepositoryCountsAndDefaultsMissingStatusesToZero() {
        when(receiptRepository.getSubmitterStatusCounts(REX_USER_ID))
                .thenReturn(List.of(
                        statusCount(ReceiptStatusEnum.SUBMITTED, 3L),
                        statusCount(ReceiptStatusEnum.APPROVED, 2L),
                        statusCount(ReceiptStatusEnum.REJECTED, null)
                ));

        ReceiptStatusCountDto result = receiptCountService.getSubmitterReceiptCounts(REX_USER_ID);

        assertThat(result.getTotal()).isEqualTo(5L);
        assertThat(result.getByStatus()).containsEntry(ReceiptStatusEnum.SUBMITTED, 3L);
        assertThat(result.getByStatus()).containsEntry(ReceiptStatusEnum.APPROVED, 2L);
        assertThat(result.getByStatus()).containsEntry(ReceiptStatusEnum.REJECTED, 0L);
        assertThat(result.getByStatus()).containsEntry(ReceiptStatusEnum.CORRECTION_REQUESTED, 0L);
        assertThat(result.getByStatus()).containsEntry(ReceiptStatusEnum.RESUBMITTED, 0L);
        assertThat(result.getByStatus()).containsEntry(ReceiptStatusEnum.WITHDRAWN, 0L);
        assertThat(result.getByStatus()).hasSize(ReceiptStatusEnum.values().length);
        verify(receiptRepository).getSubmitterStatusCounts(REX_USER_ID);
    }

    @Test
    void getSubmitterReceiptCounts_whenRepositoryReturnsNoRows_returnsAllStatusesWithZeroTotal() {
        when(receiptRepository.getSubmitterStatusCounts(REX_USER_ID)).thenReturn(List.of());

        ReceiptStatusCountDto result = receiptCountService.getSubmitterReceiptCounts(REX_USER_ID);

        assertThat(result.getTotal()).isZero();
        assertThat(result.getByStatus()).hasSize(ReceiptStatusEnum.values().length);
        assertThat(result.getByStatus().values()).containsOnly(0L);
        verify(receiptRepository).getSubmitterStatusCounts(REX_USER_ID);
    }

    @Test
    void getApproverReceiptCounts_mapsRepositoryCountsAndDefaultsMissingStatusesToZero() {
        when(receiptRepository.getApproverStatusCounts(REX_USER_ID))
                .thenReturn(List.of(
                        statusCount(ReceiptStatusEnum.CORRECTION_REQUESTED, 4L),
                        statusCount(ReceiptStatusEnum.RESUBMITTED, 1L),
                        statusCount(ReceiptStatusEnum.WITHDRAWN, null)
                ));

        ReceiptStatusCountDto result = receiptCountService.getApproverReceiptCounts(REX_USER_ID);

        assertThat(result.getTotal()).isEqualTo(5L);
        assertThat(result.getByStatus()).containsEntry(ReceiptStatusEnum.CORRECTION_REQUESTED, 4L);
        assertThat(result.getByStatus()).containsEntry(ReceiptStatusEnum.RESUBMITTED, 1L);
        assertThat(result.getByStatus()).containsEntry(ReceiptStatusEnum.WITHDRAWN, 0L);
        assertThat(result.getByStatus()).containsEntry(ReceiptStatusEnum.SUBMITTED, 0L);
        assertThat(result.getByStatus()).containsEntry(ReceiptStatusEnum.REJECTED, 0L);
        assertThat(result.getByStatus()).containsEntry(ReceiptStatusEnum.APPROVED, 0L);
        assertThat(result.getByStatus()).hasSize(ReceiptStatusEnum.values().length);
        verify(receiptRepository).getApproverStatusCounts(REX_USER_ID);
    }

    @Test
    void getApproverReceiptCounts_whenRepositoryReturnsNoRows_returnsAllStatusesWithZeroTotal() {
        when(receiptRepository.getApproverStatusCounts(REX_USER_ID)).thenReturn(List.of());

        ReceiptStatusCountDto result = receiptCountService.getApproverReceiptCounts(REX_USER_ID);

        assertThat(result.getTotal()).isZero();
        assertThat(result.getByStatus()).hasSize(ReceiptStatusEnum.values().length);
        assertThat(result.getByStatus().values()).containsOnly(0L);
        verify(receiptRepository).getApproverStatusCounts(REX_USER_ID);
    }

    private ReceiptStatusCountProjection statusCount(ReceiptStatusEnum status, Long count) {
        return new ReceiptStatusCountProjection() {
            @Override
            public String getStatus() {
                return status.name();
            }

            @Override
            public Long getCount() {
                return count;
            }
        };
    }
}
