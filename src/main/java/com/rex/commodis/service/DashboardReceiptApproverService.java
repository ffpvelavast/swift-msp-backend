package com.rex.commodis.service;

import com.rex.commodis.dto.receipts.DashboardReceiptApproverCountResponseDto;

public interface DashboardReceiptApproverService {

    DashboardReceiptApproverCountResponseDto getApproverReceiptCount(Long rexUserId);
}
