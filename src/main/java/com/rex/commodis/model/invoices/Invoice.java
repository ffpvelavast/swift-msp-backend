/** JPA entity representing the Invoice table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.invoices;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rex.commodis.model.*;
import com.rex.commodis.model.ProjectNumber;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "invoices", schema = "invoices")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @SequenceGenerator(name = "GEN_Invoice", sequenceName = "invoices.rex_invoice_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_Invoice")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "document_number", nullable = false)
    private String documentNumber;

    @Column(name = "document_link")
    private String documentLink;

    @Column(name = "document_type", nullable = false)
    private String documentType;

    @Column(name = "document_name", nullable = false, columnDefinition = "citext")
    private String documentName;

    @Column(name = "supply_date")
    private LocalDate supplyDate;

    @Column(name = "supply_month", insertable = false, updatable = false)
    private Integer supplyMonth;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "accounting_text", nullable = false, columnDefinition = "citext")
    private String accountingText;

    @Column(name = "recipient_number")
    private String recipientNumber;

    @Column(name = "recipient", nullable = false, columnDefinition = "citext")
    private String recipient;

    @Column(name = "recipient_invoice_number")
    private String recipientInvoiceNumber;

    @Column(name = "vendor_number")
    private String vendorNumber;

    @Column(name = "vendor", nullable = false, columnDefinition = "citext")
    private String vendor;

    @Column(name = "vendor_invoice_number")
    private String vendorInvoiceNumber;

    @Column(name = "invoicetotal_gross", precision = 10, scale = 2)
    private BigDecimal invoiceTotalGross;

    @Column(name = "invoicetotal_net", precision = 10, scale = 2)
    private BigDecimal invoiceTotalNet;

    @Column(name = "invoicetotal_vat", precision = 10, scale = 2)
    private BigDecimal invoiceTotalVat;

    @Column(name = "currency", length = 5, columnDefinition = "bpchar(5)")
    private String currency;

    @Column(name = "payrolltaxtion_required")
    @ColumnDefault("true")
    private Boolean payrollTaxationRequired = true;

    // Custom fields 1 to 20
    @Column(name = "customfield_1") private String customField1;
    @Column(name = "customfield_2") private String customField2;
    @Column(name = "customfield_3") private String customField3;
    @Column(name = "customfield_4") private String customField4;
    @Column(name = "customfield_5") private String customField5;
    @Column(name = "customfield_6") private String customField6;
    @Column(name = "customfield_7") private String customField7;
    @Column(name = "customfield_8") private String customField8;
    @Column(name = "customfield_9") private String customField9;
    @Column(name = "customfield_10") private String customField10;
    @Column(name = "customfield_11") private String customField11;
    @Column(name = "customfield_12") private String customField12;
    @Column(name = "customfield_13") private String customField13;
    @Column(name = "customfield_14") private String customField14;
    @Column(name = "customfield_15") private String customField15;
    @Column(name = "customfield_16") private String customField16;
    @Column(name = "customfield_17") private String customField17;
    @Column(name = "customfield_18") private String customField18;
    @Column(name = "customfield_19") private String customField19;
    @Column(name = "customfield_20") private String customField20;

    @Column(name = "project_number_id")
    private Long projectNumberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_number_id", insertable = false, updatable = false)
    private ProjectNumber projectNumber;

    @Column(name = "released_at")
    private OffsetDateTime releasedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "note")
    private String note;

    @Column(name = "document_date")
    private LocalDate documentDate;

    @Column(name = "taxreason_client_id")
    private Long taxReasonClientId;

    @Column(name = "taxation_run_id")
    private Long taxationRunId;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<InvoicePosition> positions;
}



