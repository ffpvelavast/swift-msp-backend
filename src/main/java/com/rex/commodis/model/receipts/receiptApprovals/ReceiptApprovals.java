package com.rex.commodis.model.receipts.receiptApprovals;

import com.rex.commodis.model.RexUser;
import com.rex.commodis.model.receipts.Receipt;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "receipt_approvals", schema = "receipts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptApprovals {

    @EmbeddedId
    private ReceiptApprovalId id;

    @Column(name = "receipt_context")
    private String receiptContext;

    @Column(name = "approval_withdrawn_at")
    private LocalDateTime approvalWithdrawnAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "withdrawer_id")
    private RexUser withdrawer;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("receiptId")
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("approverId")
    @JoinColumn(name = "approver_id")
    private RexUser approver;
}