/** Service implementation for ReceiptHistory business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rex.commodis.dto.receipts.CreateReceiptRequestDto;
import com.rex.commodis.model.receipts.ReceiptHistory;
import com.rex.commodis.repository.receipts.ReceiptHistoryRepository;


@Service
@RequiredArgsConstructor
@Transactional
public class ReceiptHistoryServiceImpl implements ReceiptHistoryService{

    private final ReceiptHistoryRepository receiptHistoryRepository;

    public void createReceiptHistory(Long receiptId, Long rexUserId) {

        // Create record in receipt_history
        ReceiptHistory receiptHistory= new ReceiptHistory();
        receiptHistory.setReceiptId(receiptId);
        receiptHistory.setCreatorId(rexUserId);
        receiptHistory.setReceiptStatusId((short) 1);
        receiptHistory.setActive(true);
        receiptHistoryRepository.save(receiptHistory);
    }
}