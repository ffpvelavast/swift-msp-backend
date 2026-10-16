/** JPA entity representing the TeamEventType table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.events;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.rex.commodis.model.Platform;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "team_event_type", schema = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamEventType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_event_type_seq")
    @SequenceGenerator(
            name = "team_event_type_seq",
            sequenceName = "events.team_event_type_id_seq",
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

    @Column(name = "sortid", nullable = false)
    private Short sortId;

    @Column(name = "display_text", columnDefinition = "jsonb", nullable = false)
    private String displayText;

    @Column(name = "description", columnDefinition = "jsonb", nullable = false)
    private String description;

    @Column(name = "apply_law40_2", nullable = false)
    private Boolean applyLaw40_2;

    @Column(name = "platform_id", nullable = false)
    private Long platformId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id", insertable = false, updatable = false)
    private Platform platform;

    @OneToMany(mappedBy = "teamEventType")
    private List<TeamEvent> teamEvents;
}
