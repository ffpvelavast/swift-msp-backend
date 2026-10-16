package com.rex.commodis.repository.receipts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.receipts.ReceiptHistory;

import java.util.List;

@Repository
public interface SubmitterReceiptRepository extends JpaRepository<ReceiptHistory, Long> {

    @Query(value = """
        SELECT
            r.id                           AS receiptId,
            r.receipt_title::text          AS receiptTitle,
            COALESCE(
                tc.display_text ->> lower(:locale),
                tc.display_text ->> 'en'
            ) AS category,
            r.receipt_amount               AS amount,
            r.receipt_currency::text       AS currency,
            COALESCE(
                rs.display_text ->> lower(:locale),
                rs.display_text ->> 'en'
            ) AS status
        FROM receipts.receipt_history rh
    
        JOIN receipts.receipts r
            ON r.id = rh.receipt_id
    
        JOIN receipts.receipt_status rs
            ON rs.id = rh.receiptstatus_id
    
        JOIN taxation.taxreason_client trc
            ON trc.id = r.taxreason_client_id
    
        JOIN taxation.taxcategory_client tc
            ON tc.id = trc.taxcategory_client_id
    
        WHERE r.rexuser_id = :rexUserId
             AND rh.active = true
             AND rs.status_code IN (
                   'SUBMITTED',
                   'RESUBMITTED',
                   'CORRECTION_REQUESTED'
             )
    
        ORDER BY r.created_at DESC
        LIMIT 5
    """, nativeQuery = true)
    List<SubmitterReceiptProjection> fetchSubmitterReceipts(
            @Param("rexUserId") Long rexUserId,
            @Param("locale") String locale
    );
}