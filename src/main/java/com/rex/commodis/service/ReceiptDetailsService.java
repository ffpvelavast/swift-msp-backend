/** Service interface defining ReceiptDetails operations; implemented by ReceiptDetailsServiceImpl. */
package com.rex.commodis.service;

import com.rex.commodis.dto.receipts.ReceiptDetailResponseDto;

import java.util.List;

public interface ReceiptDetailsService {

    /**
     * Fetches and aggregates all receipt data for the approver's individual receipt screen.
     *
     * @param receiptId the ID of the receipt to retrieve
     * @param rexUserId 
     * @return fully populated ReceiptApproverDetailResponseDto
     */
    ReceiptDetailResponseDto getReceiptDetails(Long receiptId, Long rexUserId);

    void createApprovers(Long receiptId, List<Long> rexUserIds);
}
