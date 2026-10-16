/** JPA entity representing the ReceiptToTeamEvent table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.receipts.receiptToTeamEvent;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.rex.commodis.model.events.TeamEvent;
import com.rex.commodis.model.receipts.Receipt;

import java.time.OffsetDateTime;

@Entity
@Table(name = "receipt_to_teamevent", schema = "receipts")
@IdClass(ReceiptToTeamEventId.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptToTeamEvent {

    @Id
    @Column(name = "receipt_id", nullable = false)
    private Long receiptId;

    @Id
    @Column(name = "teamevent_id", nullable = false)
    private Long teamEventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", insertable = false, updatable = false)
    private Receipt receipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamevent_id", insertable = false, updatable = false)
    private TeamEvent teamEvent;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
