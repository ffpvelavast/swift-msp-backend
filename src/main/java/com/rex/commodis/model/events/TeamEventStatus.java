/** JPA entity representing the TeamEventStatus table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.events;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

import com.rex.commodis.model.enums.TeamEventStatusEnum;

@Getter
@Setter
@Entity
@Table(name = "team_event_status", schema = "events")
public class TeamEventStatus {
    @Id
    private Short id;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "display_text", nullable = false, columnDefinition = "jsonb")
    private String displayText;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_code", nullable = false, length = 50)
    private TeamEventStatusEnum statusCode;
}