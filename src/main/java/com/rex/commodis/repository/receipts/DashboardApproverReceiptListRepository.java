/** Spring Data JPA repository for DashboardApproverReceiptList entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.receipts;

import com.rex.commodis.dto.receipts.DashboardApproverReceiptListProjection;
import com.rex.commodis.model.receipts.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DashboardApproverReceiptListRepository extends JpaRepository<Receipt, Long> {

    @Query(value = """
        SELECT
            r.id                           AS receiptId,
            r.receipt_title::text          AS receiptTitle,
            r.receipt_amount               AS amount,
            r.receipt_currency::text       AS currency,
            COALESCE(
                rs.display_text ->> lower(:locale),
                rs.display_text ->> 'en'
            ) AS status
        FROM receipts.receipts r
    
        JOIN receipts.receipt_approver ra
            ON ra.receipt_id = r.id
    
        JOIN receipts.receipt_history rh
            ON rh.receipt_id = r.id
           AND rh.active = true
    
        JOIN receipts.receipt_status rs
            ON rs.id = rh.receiptstatus_id
    
        LEFT JOIN taxation.taxreason_client tr
            ON tr.id = r.taxreason_client_id
    
        LEFT JOIN taxation.taxcategory_client tc
            ON tc.id = tr.taxcategory_client_id
    
        WHERE ra.rexuser_id = :rexUserId
            AND NOT EXISTS (
                SELECT 1
                FROM receipts.receipt_to_teamevent rte
                WHERE rte.receipt_id = r.id
            )
            AND rs.status_code IN (
                'SUBMITTED',
                'RESUBMITTED',
                'CORRECTION_REQUESTED'
            )
    
        ORDER BY r.created_at DESC
        LIMIT 5
    """, nativeQuery = true)
    List<DashboardApproverReceiptListProjection> getDashboardApproverReceiptList(
            @Param("rexUserId") Long rexUserId,
            @Param("locale") String locale
    );
}