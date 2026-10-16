/** Service interface defining DashboardApproverReceiptList operations; implemented by DashboardApproverReceiptListServiceImpl. */
package com.rex.commodis.service;

import com.rex.commodis.dto.receipts.DashboardApproverReceiptListResponseDto;

public interface DashboardApproverReceiptListService {

    DashboardApproverReceiptListResponseDto getDashboardApproverReceiptList(Long rexUserId);
}