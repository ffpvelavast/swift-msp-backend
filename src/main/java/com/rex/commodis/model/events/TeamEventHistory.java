/** JPA entity representing the TeamEventHistory table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.events;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;

import com.rex.commodis.model.RexUser;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "team_event_history", schema = "events")
public class TeamEventHistory {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "team_event_history_seq"
    )
    @SequenceGenerator(
            name = "team_event_history_seq",
            sequenceName = "events.team_event_history_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "teamevent_id", nullable = false)
    private Long teamEventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamevent_id", insertable = false, updatable = false)
    private TeamEvent teamEvent;

    @Column(name = "status_id", nullable = false)
    private Short statusId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", insertable = false, updatable = false)
    private TeamEventStatus status;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", insertable = false, updatable = false)
    private RexUser creator;

    @Column(name = "external_note")
    private String externalNote;

    @Column(name = "active", nullable = false)
    private Boolean active;
}