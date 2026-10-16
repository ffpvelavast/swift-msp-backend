/** JPA entity representing the InvoiceStatus table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.invoices;

import com.rex.commodis.model.enums.InvoiceStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "invoice_status", schema = "invoices")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "sortid", nullable = false)
    private Short sortId;

    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_status_enum", nullable = false)
    private InvoiceStatusEnum status;

    @Column(name = "display_text", nullable = false, columnDefinition = "jsonb")
    private String displayText;

    @Column(name = "description", nullable = false, columnDefinition = "jsonb")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

}



