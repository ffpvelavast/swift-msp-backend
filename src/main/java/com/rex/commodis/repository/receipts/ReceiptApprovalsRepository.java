package com.rex.commodis.repository.receipts;

import com.rex.commodis.model.receipts.receiptApprovals.ReceiptApprovalId;
import com.rex.commodis.model.receipts.receiptApprovals.ReceiptApprovals;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptApprovalsRepository
        extends JpaRepository<ReceiptApprovals, ReceiptApprovalId> {
}