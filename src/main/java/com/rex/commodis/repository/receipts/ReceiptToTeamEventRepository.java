/** Spring Data JPA repository for ReceiptToTeamEvent entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.receipts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.receipts.receiptToTeamEvent.ReceiptToTeamEvent;
import com.rex.commodis.model.receipts.receiptToTeamEvent.ReceiptToTeamEventId;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiptToTeamEventRepository
        extends JpaRepository<ReceiptToTeamEvent, ReceiptToTeamEventId> {


    List<ReceiptToTeamEvent> findByTeamEventId(Long teamEventId);
    boolean existsByReceiptId(Long receiptId);
}
