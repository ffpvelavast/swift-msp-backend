/** JPA entity representing the UserRight table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import io.hypersistence.utils.hibernate.type.range.PostgreSQLRangeType;
import io.hypersistence.utils.hibernate.type.range.Range;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Table(name = "userright", schema = "rexbase")
public class UserRight {

    @Id
    @SequenceGenerator(name = "GEN_UserRight", sequenceName = "userright_id_seq", schema = "rexbase", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_UserRight")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "right_enum", nullable = false)
    private String rightEnum;

    @Column(name = "display_text", nullable = false, columnDefinition = "jsonb")
    private String displayText;

    @Column(name = "activefrom", nullable = false)
    @ColumnDefault("'2020-01-01'")
    private LocalDate activeFrom;

    @Column(name = "activethrough", nullable = false)
    @ColumnDefault("'2999-12-31'")
    private LocalDate activeThrough;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    @ColumnDefault("now()")
    private OffsetDateTime createdAt;

    @Type(PostgreSQLRangeType.class)
    @Column(name = "activeperiod", columnDefinition = "daterange", insertable = false, updatable = false)
    private Range<LocalDate> activeRange;
}
