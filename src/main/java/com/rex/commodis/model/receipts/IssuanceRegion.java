/** JPA entity representing the IssuanceRegion table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.receipts;

import java.time.OffsetDateTime;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "issuance_region", schema = "receipts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssuanceRegion {

    @Id
    @SequenceGenerator(
            name = "GEN_IssuanceRegion",
            sequenceName = "issuance_region_id_seq",
            schema = "receipts",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_IssuanceRegion")
    @Column(name = "id", nullable = false, updatable = false)
    private Short id;

    @Column(name = "region_code", nullable = false)
    private String regionCode;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "display_text", nullable = false, columnDefinition = "jsonb")
    private Map<String, String> displayText;

    @Column(name = "description", columnDefinition = "jsonb")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}