/** JPA entity representing the ProjectLeader table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;
import io.hypersistence.utils.hibernate.type.range.PostgreSQLRangeType;
import io.hypersistence.utils.hibernate.type.range.Range;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Links a user to a project as a project leader.
 */
@Entity
@Table(name = "projectleader", schema = "rexbase")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class ProjectLeader {

    @Id
    @SequenceGenerator(name = "GEN_ProjectLeader", sequenceName = "projectleader_id_seq", schema = "rexbase", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_ProjectLeader")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "projectnumber_id", nullable = false)
    private Long projectNumberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectnumber_id", insertable = false, updatable = false)
    private ProjectNumber project;

    @Column(name = "rexuser_id", nullable = false)
    private Long rexUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rexuser_id", insertable = false, updatable = false)
    private RexUser rexUser;

    @Column(name = "activefrom", nullable = false)
    @ColumnDefault("'2020-01-01'")
    private LocalDate activeFrom;

    @Column(name = "activethrough", nullable = false)
    @ColumnDefault("'2999-12-31'")
    private LocalDate activeThrough;

    @Type(PostgreSQLRangeType.class)
    @Column(name = "activeperiod", columnDefinition = "daterange", insertable = false, updatable = false)
    private Range<LocalDate> activeRange;

    @Column(name = "created_at", nullable = false, updatable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
