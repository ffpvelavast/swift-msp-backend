package com.rex.commodis.model.receipts.receiptApprovals;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptApprovalId implements Serializable {

    @Column(name = "receipt_id")
    private Long receiptId;

    @Column(name = "approver_id")
    private Long approverId;
}