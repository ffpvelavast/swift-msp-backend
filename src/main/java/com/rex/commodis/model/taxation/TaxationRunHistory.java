/** JPA entity representing the TaxationRunHistory table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.taxation;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "taxation_run_history", schema = "taxation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxationRunHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "taxation_run_id", nullable = false)
    private Long taxationRunId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxation_run_id", insertable = false, updatable = false)
    private TaxationRun taxationRun;

    @Column(name = "taxation_run_status_id", nullable = false)
    private Long taxationRunStatusId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxation_run_status_id", insertable = false, updatable = false)
    private TaxationRunStatus taxationRunStatus;

    @Column(name = "taxation_run_initiator", nullable = false, columnDefinition = "citext")
    private String taxationRunInitiator;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
