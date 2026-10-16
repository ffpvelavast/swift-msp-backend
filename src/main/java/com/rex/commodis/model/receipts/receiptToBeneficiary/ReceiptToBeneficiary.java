/** JPA entity representing the ReceiptToBeneficiary table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.receipts.receiptToBeneficiary;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.rex.commodis.model.Beneficiary;
import com.rex.commodis.model.receipts.Receipt;

import java.time.OffsetDateTime;

@Entity
@Table(name = "receipt_to_beneficiary", schema = "receipts")
@IdClass(ReceiptToBeneficiaryId.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptToBeneficiary {

    @Id
    @Column(name = "receipt_id", nullable = false)
    private Long receiptId;

    @Id
    @Column(name = "beneficiary_id", nullable = false)
    private Long beneficiaryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", insertable = false, updatable = false)
    private Receipt receipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiary_id", insertable = false, updatable = false)
    private Beneficiary beneficiary;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}