/** JPA entity representing the InvoicePosition table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.invoices;

import com.rex.commodis.model.ProjectNumber;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "invoice_positions", schema = "invoices")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoicePosition {

    @Id
    @SequenceGenerator(name = "GEN_InvoicePosition", sequenceName = "invoices.rex_invoice_position_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_InvoicePosition")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "invoice_id", nullable = false)
    private Long invoiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", insertable = false, updatable = false)
    private Invoice invoice;

    @Column(name = "external_position_number", nullable = false)
    private String externalPositionNumber;

    @Column(name = "position_line_number")
    private Integer positionLineNumber;

    @Column(name = "factual_approver", nullable = false)
    private String factualApprover;

    @Column(name = "position_text", nullable = false)
    private String positionText;

    @Column(name = "projectnumber")
    private String projectNumber;

    @Column(name = "position_quantity")
    private Integer positionQuantity;

    @Column(name = "positionamount_gross", precision = 10, scale = 2)
    private BigDecimal positionAmountGross;

    @Column(name = "positionamount_net", precision = 10, scale = 2)
    private BigDecimal positionAmountNet;

    @Column(name = "positionamount_vat", precision = 10, scale = 2)
    private BigDecimal positionAmountVat;

    @Column(name = "position_vat_rate")
    private Integer positionVatRate;

    @Column(name = "project_number_id")
    private Long projectNumberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_number_id", insertable = false, updatable = false)
    private ProjectNumber projectNumberReference;

    @Column(name = "taxreason_client_id")
    private Long taxReasonClientId;

    @Column(name = "payrolltaxtion_required")
    @ColumnDefault("true")
    private Boolean payrollTaxationRequired = true;

//    @OneToMany(mappedBy = "invoicePosition", fetch = FetchType.LAZY)
//    private List<InvoicePositionBeneficiary> beneficiaries;

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

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}



