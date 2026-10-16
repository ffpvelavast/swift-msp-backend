/** Spring Data JPA repository for ReceiptHistory entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.receipts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.enums.ReceiptStatusEnum;
import com.rex.commodis.model.receipts.ReceiptHistory;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiptHistoryRepository extends JpaRepository<ReceiptHistory, Long> {

    @Query("""
        SELECT rh FROM ReceiptHistory rh
        JOIN FETCH rh.receiptStatus rs
        WHERE rh.receiptId = :receiptId
        ORDER BY rh.createdAt ASC
    """)
    List<ReceiptHistory> findFullHistoryByReceiptId(@Param("receiptId") Long receiptId);

    List<ReceiptHistory> findByReceiptId(Long receiptId);

    Optional<ReceiptHistory> findFirstByReceiptIdAndActiveTrueOrderByCreatedAtDesc(Long receiptId);

    // Fetch status display text directly via JOIN — avoids extra lazy-load query
    @Query("""
            SELECT rh.receiptStatus.displayText
            FROM ReceiptHistory rh
            WHERE rh.receiptId = :receiptId
              AND rh.active = true
            ORDER BY rh.createdAt DESC
            """)
    Optional<String> findActiveStatusDisplayTextByReceiptId(@Param("receiptId") Long receiptId);

    @Query("""
            SELECT rs.status
            FROM ReceiptHistory rh
            JOIN rh.receiptStatus rs
            WHERE rh.receiptId = :receiptId
              AND rh.active = true
            """)
    Optional<ReceiptStatusEnum> findActiveStatusCodeByReceiptId(@Param("receiptId") Long receiptId);

    // -- method for status update --


    @Modifying(clearAutomatically = true, flushAutomatically = true)  // ← THIS IS THE FIX
    @Query("""
            UPDATE ReceiptHistory rh
            SET rh.active = false
            WHERE rh.receiptId = :receiptId
              AND rh.active = true
            """)
    int deactivateCurrentActiveStatus(@Param("receiptId") Long receiptId);
    
    @Query("""
    	       SELECT rh
    	       FROM ReceiptHistory rh
    	       WHERE rh.receiptId = :receiptId
    	         AND rh.active = true
    	       """)
    Optional<ReceiptHistory> findActiveByReceiptId(Long receiptId);

    Optional<ReceiptHistory> findByReceiptIdAndActiveTrue(Long receiptId);

}