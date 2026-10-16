package com.rex.commodis.service;

import com.rex.commodis.dto.ReceiptStatusCountProjection;
import com.rex.commodis.dto.receipts.DashboardReceiptApproverCountResponseDto;
import com.rex.commodis.repository.receipts.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DashboardReceiptApproverServiceImpl implements DashboardReceiptApproverService {

    private final ReceiptRepository receiptRepository;

    @Override
    public DashboardReceiptApproverCountResponseDto getApproverReceiptCount(Long rexUserId) {

        Long receiptApproverCount =
                receiptRepository.getApproverReceiptCount(rexUserId);

        return DashboardReceiptApproverCountResponseDto.builder()
                .receipt_approver_count(
                        receiptApproverCount != null ? receiptApproverCount : 0L
                )
                .build();
    }
}
