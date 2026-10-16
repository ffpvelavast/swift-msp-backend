/** Spring Data JPA repository for ReceiptApprover entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.receipts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.RexUser;
import com.rex.commodis.model.receipts.receiptApprover.ReceiptApprover;
import com.rex.commodis.model.receipts.receiptApprover.ReceiptApproverId;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiptApproverRepository extends JpaRepository<ReceiptApprover, ReceiptApproverId> {

    @Query("""
            SELECT ru.fullName
            FROM ReceiptApprover ra
            JOIN ra.rexUser ru
            WHERE ra.receiptId = :receiptId
            ORDER BY ra.createdAt DESC
        """)
        List<String> findApproversByReceiptId(@Param("receiptId") Long receiptId);

    Boolean existsByReceiptIdAndRexUserId(Long receiptId, Long rexUserId);
}