package com.rex.commodis.service;

import com.rex.commodis.dto.receipts.SubmitterCountDto;
import com.rex.commodis.repository.receipts.SubmitterCountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubmitterCountServiceImplTest {

    @Mock private SubmitterCountRepository submitterCountRepository;

    @InjectMocks
    private SubmitterCountServiceImpl service;

    private static final Long REX_USER_ID = 1L;

    @Test
    void getSubmitterReceiptCount_returnsRepositoryCount() {
        when(submitterCountRepository.getSubmitterReceiptCount(REX_USER_ID)).thenReturn(6L);

        SubmitterCountDto result = service.getSubmitterReceiptCount(REX_USER_ID);

        assertThat(result.getSubmitterReceiptsCount()).isEqualTo(6L);
        verify(submitterCountRepository).getSubmitterReceiptCount(REX_USER_ID);
    }

    @Test
    void getSubmitterReceiptCount_whenRepositoryReturnsNull_returnsDtoWithNullCount() {
        when(submitterCountRepository.getSubmitterReceiptCount(REX_USER_ID)).thenReturn(null);

        SubmitterCountDto result = service.getSubmitterReceiptCount(REX_USER_ID);

        assertThat(result.getSubmitterReceiptsCount()).isNull();
        verify(submitterCountRepository).getSubmitterReceiptCount(REX_USER_ID);
    }
}
