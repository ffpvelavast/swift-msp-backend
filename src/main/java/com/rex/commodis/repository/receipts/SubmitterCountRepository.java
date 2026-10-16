package com.rex.commodis.repository.receipts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.receipts.ReceiptHistory;

@Repository
public interface SubmitterCountRepository extends JpaRepository<ReceiptHistory, Long> {

    @Query(value = """

        SELECT COUNT(*)
        FROM receipts.receipt_history rh
        JOIN receipts.receipts r ON r.id = rh.receipt_id
        JOIN receipts.receipt_status rs ON rs.id = rh.receiptstatus_id
        WHERE r.rexuser_id = :rexUserId
          AND rh.active = true
          AND rs.status_code IN (
              'SUBMITTED',
              'RESUBMITTED',
              'CORRECTION_REQUESTED'
          )

        """, nativeQuery = true)
    Long getSubmitterReceiptCount(@Param("rexUserId") Long rexUserId);
}
