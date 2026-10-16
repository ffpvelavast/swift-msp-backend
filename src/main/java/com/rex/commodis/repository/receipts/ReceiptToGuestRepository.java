/** Spring Data JPA repository for ReceiptToGuest entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.receipts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.receipts.receiptToGuest.ReceiptToGuest;
import com.rex.commodis.model.receipts.receiptToGuest.ReceiptToGuestId;

import java.util.List;

@Repository
public interface ReceiptToGuestRepository extends JpaRepository<ReceiptToGuest, ReceiptToGuestId> {
    List<ReceiptToGuest> findByReceiptId(Long receiptId);

    // Fetch external guest display names in one JOIN query
    @Query("""
            SELECT g.fullName
            FROM ReceiptToGuest rtg
            JOIN rtg.guest g
            WHERE rtg.receiptId = :receiptId
            """)
    List<String> findGuestNamesByReceiptId(@Param("receiptId") Long receiptId);
}