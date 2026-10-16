/** JPA entity representing the TaxationRun table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.taxation;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "taxation_run", schema = "taxation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxationRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "started_at", nullable = false)
    private OffsetDateTime startedAt;

    @Column(name = "endded_at")
    private OffsetDateTime endedAt;

    @Column(name = "initiator", nullable = false)
    private String initiator;
}
