/** JPA entity representing the ReceiptToGuest table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.receipts.receiptToGuest;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.rex.commodis.model.Guest;
import com.rex.commodis.model.receipts.Receipt;

import java.time.OffsetDateTime;

@Entity
@Table(name = "receipt_to_guest", schema = "receipts")
@IdClass(ReceiptToGuestId.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptToGuest {

    @Id
    @Column(name = "receipt_id", nullable = false)
    private Long receiptId;

    @Id
    @Column(name = "guest_id", nullable = false)
    private Long guestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", insertable = false, updatable = false)
    private Receipt receipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", insertable = false, updatable = false)
    private Guest guest;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}