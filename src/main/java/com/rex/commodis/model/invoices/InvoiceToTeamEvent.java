/** JPA entity representing the InvoiceToTeamEvent table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.invoices;

import com.rex.commodis.model.events.TeamEvent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "invoice_to_team_event", schema = "invoices")
public class InvoiceToTeamEvent {

    @Id
    @Column(name = "invoice_id")
    private Long invoiceId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", insertable = false, updatable = false)
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_event_id")
    private TeamEvent teamEvent;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;
}


