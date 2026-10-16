/** JPA entity representing the InvoiceError table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.invoices;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "invoice_errors", schema = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "document_id")
    private String documentId;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "field_name")
    private String fieldName;

    @Column(name = "invoices_id")
    private Long invoicesId;

    @Column(name = "verified_by")
    private String verifiedBy;

    @Column(name = "rexuser_lookup_result")
    private String rexuserLookupResult;

    @Column(name = "projectnumber_input")
    private String projectnumberInput;

    @Column(name = "projectnumber_lookup_result")
    private String projectnumberLookupResult;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "invoice_errorcode_id")
    private Integer invoiceErrorCodeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_errorcode_id", insertable = false, updatable = false)
    private InvoiceErrorCode invoiceErrorCode;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}

