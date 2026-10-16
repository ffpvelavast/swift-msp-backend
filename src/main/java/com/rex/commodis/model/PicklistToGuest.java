/** JPA entity representing the PicklistToGuest table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "picklist_to_guest", schema = "rexbase")
@IdClass(PicklistToGuestId.class)
public class PicklistToGuest {

    @Id
    @Column(name = "picklist_id", nullable = false)
    private Long picklistId;

    @Id
    @Column(name = "guest_id", nullable = false)
    private Long guestId;

    // timestamp WITHOUT time zone -> LocalDateTime. DB default handles value.
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "picklist_id", nullable = false, insertable = false, updatable = false)
    private Picklist picklist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false, insertable = false, updatable = false)
    private Guest guest;
}
