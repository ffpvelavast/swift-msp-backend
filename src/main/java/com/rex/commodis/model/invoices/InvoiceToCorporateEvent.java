/** JPA entity representing the InvoiceToCorporateEvent table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.invoices;

import com.rex.commodis.model.events.CorporateEvent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "invoice_to_corporate_event", schema = "invoices")
public class InvoiceToCorporateEvent {

    @Id
    @Column(name = "invoice_id")
    private Long invoiceId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", insertable = false, updatable = false)
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corporate_event_id")
    private CorporateEvent corporateEvent;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;
}


