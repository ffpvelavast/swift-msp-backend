/** JPA entity representing the TaxationRunStatus table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.taxation;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "taxation_run_status", schema = "taxation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxationRunStatus {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sortid", nullable = false)
    private Short sortId;

    @Column(name = "taxation_status_enum", nullable = false)
    private String status;

    @Column(name = "display_text", nullable = false, columnDefinition = "jsonb")
    private String displayText;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
