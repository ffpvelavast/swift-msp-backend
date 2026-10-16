package com.rex.commodis.model.invoices;

import com.rex.commodis.model.Beneficiary;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "invoice_positions_beneficiary", schema = "invoices")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoicePositionsBeneficiary {

    @Id
    @SequenceGenerator(
            name = "GEN_InvoicePositionBeneficiary",
            sequenceName = "invoices.invoice_positions_beneficiary_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "GEN_InvoicePositionBeneficiary"
    )
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "beneficiary_id", nullable = false)
    private Long beneficiaryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiary_id", insertable = false, updatable = false)
    private Beneficiary beneficiary;

    @Column(name = "invoice_position_id", nullable = false)
    private Long invoicePositionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_position_id", insertable = false, updatable = false)
    private InvoicePosition invoicePosition;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
