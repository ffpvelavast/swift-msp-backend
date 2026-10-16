/** JPA entity representing the ProjectNumber table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
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
import java.util.List;
import java.util.UUID;

/**
 * Represents a project within a platform.
 */
@Entity
@Table(name = "projectnumbers", schema = "rexbase")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class ProjectNumber {

    @Id
    @SequenceGenerator(name = "GEN_ProjectNumber", sequenceName = "projectnumbers_id_seq", schema = "rexbase", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_ProjectNumber")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "platform_id", nullable = false)
    private Long platformId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id", insertable = false, updatable = false)
    private Platform platform;

    @Column(name = "projectnumberid", nullable = false)
    private UUID projectNumberId;

    @Column(name = "external_projectid")
    private String externalProjectId;

    @Column(name = "projectnumber", nullable = false, length = 25)
    private String projectNumber;

    @Column(name = "projecttitle", nullable = false, columnDefinition = "citext")
    private String projectTitle;

    @Column(name = "projectsubtitle", columnDefinition = "citext")
    private String projectSubtitle;

    @Column(name = "description", columnDefinition = "citext")
    private String description;

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

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<ProjectLeader> projectLeaders;
}
