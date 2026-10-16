/** Service implementation for ReceiptToCorporateEvent business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.model.receipts.receiptToCorporateEvent.ReceiptToCorporateEvent;
import com.rex.commodis.repository.receipts.ReceiptToCorporateEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceiptToCorporateEventServiceImpl implements ReceiptToCorporateEventService {

    private final ReceiptToCorporateEventRepository receiptToCorporateEventRepository;

    public void saveCorporateEvent(Long receiptId, Long corporateEventId) {

        ReceiptToCorporateEvent receiptToCorporateEvent = ReceiptToCorporateEvent.builder()
                .receiptId(receiptId)
                .corporateEventId(corporateEventId)
                .build();

        receiptToCorporateEventRepository.save(receiptToCorporateEvent);
    }
}
