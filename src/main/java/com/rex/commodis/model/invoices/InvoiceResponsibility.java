/** JPA entity representing the InvoiceResponsibility table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.invoices;

import com.rex.commodis.model.RexUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import io.hypersistence.utils.hibernate.type.range.PostgreSQLRangeType;
import io.hypersistence.utils.hibernate.type.range.Range;
import org.hibernate.annotations.Type;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "invoice_responsibility", schema = "invoices")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponsibility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "invoice_id", nullable = false)
    private Long invoiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", insertable = false, updatable = false)
    private Invoice invoice;

    @Column(name = "rexuser_id", nullable = false)
    private Long rexUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rexuser_id", insertable = false, updatable = false)
    private RexUser rexUser;

    @Column(name = "delegate_id")
    private Long delegateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delegate_id", insertable = false, updatable = false)
    private RexUser delegate;

    @Column(name = "delegated_at")
    private OffsetDateTime delegatedAt;

    @Column(name = "remarks", columnDefinition = "text")
    private String remarks;

    @Column(name = "responsible_from", nullable = false)
    private OffsetDateTime responsibleFrom;

    @Column(name = "responsible_through", nullable = false)
    private OffsetDateTime responsibleThrough;

    @Type(PostgreSQLRangeType.class)
    @Column(name = "responsible_period", columnDefinition = "tstzrange", insertable = false, updatable = false)
    private Range<ZonedDateTime> responsiblePeriod;

    @Column(name = "is_active", insertable = false)
    private Boolean isActive;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}



