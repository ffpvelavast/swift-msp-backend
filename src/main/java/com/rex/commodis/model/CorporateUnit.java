/** JPA entity representing the CorporateUnit table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Type;

import io.hypersistence.utils.hibernate.type.range.PostgreSQLRangeType;
import io.hypersistence.utils.hibernate.type.range.Range;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "corporate_unit", schema = "masterdata")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class CorporateUnit {

    @Id
    @SequenceGenerator(name = "GEN_CorporateUnit", sequenceName = "corporate_unit_id_seq", schema = "masterdata", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_CorporateUnit")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "sortid")
    private Integer sortId;

    @Column(name = "corporate_division_id", nullable = false)
    private Long corporateDivisionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corporate_division_id", insertable = false, updatable = false)
    private CorporateDivision corporateDivision;

    @Column(name = "unit_name", columnDefinition = "jsonb", nullable = false)
    private String unitName;

    @Column(name = "activefrom", nullable = false)
    @ColumnDefault("'2020-01-01'")
    private LocalDate activeFrom;

    @Column(name = "activethrough", nullable = false)
    @ColumnDefault("'2999-12-31'")
    private LocalDate activeThrough;

    @Type(PostgreSQLRangeType.class)
    @Column(name = "activeperiod", columnDefinition = "daterange", insertable = false, updatable = false)
    private Range<LocalDate> activePeriod;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
