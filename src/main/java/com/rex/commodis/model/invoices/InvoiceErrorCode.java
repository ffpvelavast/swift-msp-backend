/** JPA entity representing the InvoiceErrorCode table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.invoices;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "invoice_errorcodes", schema = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceErrorCode {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "invoice_error_de", nullable = false)
    private String invoiceErrorDe;

    @Column(name = "invoice_error_en", nullable = false)
    private String invoiceErrorEn;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
