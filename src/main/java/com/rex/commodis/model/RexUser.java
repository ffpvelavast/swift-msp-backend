/** JPA entity representing the RexUser table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import java.sql.Types;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import jakarta.persistence.Table;
import org.hibernate.annotations.*;

import io.hypersistence.utils.hibernate.type.range.PostgreSQLRangeType;
import io.hypersistence.utils.hibernate.type.range.Range;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Table(name = "rexuser", schema = "rexbase")
public class RexUser {

    @Id
    @SequenceGenerator(name = "GEN_RexUser", sequenceName = "rexuser_id_seq", schema = "rexbase", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_RexUser")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "platform_id", nullable = false)
    private Long platformId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id", insertable = false, updatable = false)
    private Platform platform;

    @Column(name = "keycloakid")
    private java.util.UUID keycloakId;

    @Column(name = "emailaddress", columnDefinition = "citext")
    private String email;

    @Column(name = "lastname", columnDefinition = "citext") // nullable in DB
    private String lastname;

    @Column(name = "firstname", columnDefinition = "citext") // nullable in DB
    private String firstname;

    @Column(name = "academictitle")
    private String academictitle;

    @Column(name = "locale", length = 2, nullable = false)
    @JdbcTypeCode(Types.CHAR)
    private String locale;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "activefrom", nullable = false)
    @ColumnDefault("'2020-01-01'")
    private LocalDate activeFrom;

    @Column(name = "activethrough", nullable = false)
    @ColumnDefault("'2999-12-31'")
    private LocalDate activeThrough;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "listname", insertable = false, updatable = false, columnDefinition = "citext")
    private String listname;

    @Column(name = "fullname", insertable = false, updatable = false, columnDefinition = "citext")
    private String fullName;

    @Type(PostgreSQLRangeType.class)
    @Column(name = "activeperiod", columnDefinition = "daterange", insertable = false, updatable = false)
    private Range<LocalDate> activePeriod;

}
