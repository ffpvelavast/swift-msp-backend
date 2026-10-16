/** JPA entity representing the ReceiptToCorporateEvent table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.receipts.receiptToCorporateEvent;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.rex.commodis.model.events.CorporateEvent;
import com.rex.commodis.model.receipts.Receipt;

import java.time.OffsetDateTime;

@Entity
@Table(name = "receipt_to_corporateevent", schema = "receipts")
@IdClass(ReceiptToCorporateEventId.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptToCorporateEvent {

    @Id
    @Column(name = "receipt_id", nullable = false)
    private Long receiptId;

    @Id
    @Column(name = "corporate_event_id", nullable = false)
    private Long corporateEventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", insertable = false, updatable = false)
    private Receipt receipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corporate_event_id", insertable = false, updatable = false)
    private CorporateEvent corporateEvent;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
