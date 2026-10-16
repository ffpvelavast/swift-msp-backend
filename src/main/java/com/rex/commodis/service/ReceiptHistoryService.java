/** Service interface defining ReceiptHistory operations; implemented by ReceiptHistoryServiceImpl. */
package com.rex.commodis.service;

import com.rex.commodis.dto.receipts.CreateReceiptRequestDto;

public interface ReceiptHistoryService {

    void createReceiptHistory(Long receiptId, Long rexUserId);
}