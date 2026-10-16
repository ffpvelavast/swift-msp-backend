/** JPA entity representing the ReceiptHistory table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.receipts;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.rex.commodis.model.RexUser;

import java.time.OffsetDateTime;

@Entity
@Table(name = "receipt_history", schema = "receipts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptHistory {

    @Id
    @SequenceGenerator(
            name = "GEN_ReceiptHistory",
            sequenceName = "receipt_history_id_seq",
            schema = "receipts",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_ReceiptHistory")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "receipt_id", nullable = false)
    private Long receiptId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", insertable = false, updatable = false)
    private Receipt receipt;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", insertable = false, updatable = false)
    private RexUser creator;

    @Column(name = "receiptstatus_id", nullable = false)
    private Short receiptStatusId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiptstatus_id", insertable = false, updatable = false)
    private ReceiptStatus receiptStatus;

    @Column(name = "external_note")
    private String externalNote;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}