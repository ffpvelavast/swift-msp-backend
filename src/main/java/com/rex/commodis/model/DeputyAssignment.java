/** JPA entity representing the DeputyAssignment table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */


package com.rex.commodis.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Assignment of a deputy for a beneficiary account, valid over a date range.
 */
@Entity
@Table(name = "deputy_assignment", schema = "rexbase", uniqueConstraints = @UniqueConstraint(name = "deputy_assignment_unique", columnNames = {
        "rexuser_id", "deputy_id" }))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class DeputyAssignment {

    @Id
    @SequenceGenerator(name = "GEN_DeputyAssignment", sequenceName = "deputy_assignment_id_seq", schema = "rexbase", allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_DeputyAssignment")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "activefrom", nullable = false)
    @ColumnDefault("'2020-01-01'")
    private LocalDate activeFrom;

    @Column(name = "activethrough", nullable = false)
    @ColumnDefault("'2999-12-31'")
    private LocalDate activeThrough;

    @Column(name = "rexuser_id", nullable = false)
    private Long rexuserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rexuser_id", insertable = false, updatable = false)
    private RexUser rexUser;

    @Column(name = "deputy_id", nullable = false)
    private Long deputyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deputy_id", insertable = false, updatable = false)
    private RexUser deputy;
}

