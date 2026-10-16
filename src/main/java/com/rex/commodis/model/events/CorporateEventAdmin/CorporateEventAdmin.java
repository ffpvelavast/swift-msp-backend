/** JPA entity representing the CorporateEventAdmin table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.events.CorporateEventAdmin;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.rex.commodis.model.RexUser;
import com.rex.commodis.model.events.CorporateEvent;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "corporate_event_admins", schema = "events")
@IdClass(CorporateEventAdminId.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorporateEventAdmin {

    @Id
    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Id
    @Column(name = "rexuser_id", nullable = false)
    private Long rexUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", insertable = false, updatable = false)
    private CorporateEvent corporateEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rexuser_id", insertable = false, updatable = false)
    private RexUser rexUser;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "active_from", nullable = false)
    private LocalDate activeFrom;

    @Column(name = "active_through", nullable = false)
    private LocalDate activeThrough;
}
