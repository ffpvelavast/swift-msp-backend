/** JPA entity representing the TeamEvent table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.events;

import com.rex.commodis.model.Platform;
import com.rex.commodis.model.events.TeamEventApprover.TeamEventApprover;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.rex.commodis.model.Picklist;
import com.rex.commodis.model.ProjectNumber;
import com.rex.commodis.model.RexUser;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "team_event", schema = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_event_seq")
    @SequenceGenerator(
            name = "team_event_seq",
            sequenceName = "events.team_event_id_seq",
            allocationSize = 1
    )
    @Column(name = "id", nullable = false)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "internal_note")
    private String internalNote;

    @Column(name = "event_title", nullable = false, columnDefinition = "citext")
    private String eventTitle;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "bookable_through", nullable = false)
    private LocalDate bookableThrough;

    @Column(name = "bookable_by", nullable = false)
    private String bookableBy;

    @Column(name = "picklist_id")
    private Long picklistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "picklist_id", insertable = false, updatable = false)
    private Picklist picklist;

    @Column(name = "rexuser_id", nullable = false)
    private Long rexuserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rexuser_id", insertable = false, updatable = false)
    private RexUser rexUser;

    @Column(name = "teameventtype_id", nullable = false)
    private Long teamEventTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teameventtype_id", insertable = false, updatable = false)
    private TeamEventType teamEventType;

    @Column(name = "projectnumber_id", nullable = false)
    private Long projectnumberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectnumber_id", insertable = false, updatable = false)
    private ProjectNumber projectNumber;

    @Column(name = "platform_id", nullable = false)
    private Long platformId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id", insertable = false, updatable = false)
    private Platform platform;

    @OneToMany(mappedBy = "teamEvent")
    private Set<TeamEventApprover> teamEventApprovers;

    @Column(name = "processing_released_at")
    private OffsetDateTime processingReleasedAt;

    @Column(name = "processing_released_by")
    private String processingReleasedBy;

    @Column(name = "processing_id")
    private Long processingId;
}
