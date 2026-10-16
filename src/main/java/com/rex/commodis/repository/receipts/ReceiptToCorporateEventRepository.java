/** Spring Data JPA repository for ReceiptToCorporateEvent entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.receipts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.receipts.receiptToCorporateEvent.ReceiptToCorporateEvent;
import com.rex.commodis.model.receipts.receiptToCorporateEvent.ReceiptToCorporateEventId;

@Repository
public interface ReceiptToCorporateEventRepository
        extends JpaRepository<ReceiptToCorporateEvent, ReceiptToCorporateEventId> {

    boolean existsByReceiptId(Long receiptId);
}
