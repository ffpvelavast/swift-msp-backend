/** JPA entity representing the TeamEventApprover table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.events.TeamEventApprover;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

import com.rex.commodis.model.RexUser;
import com.rex.commodis.model.events.TeamEvent;

@Entity
@Table(name = "team_event_approver", schema = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TeamEventApproverId.class)
public class TeamEventApprover {

    @Id
    @Column(name = "teamevent_id", nullable = false)
    private Long teamEventId;

    @Id
    @Column(name = "rexuser_id", nullable = false)
    private Long rexUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamevent_id", insertable = false, updatable = false)
    private TeamEvent teamEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rexuser_id", insertable = false, updatable = false)
    private RexUser rexUser;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}