/** InvoicePositionBeneficiary: part of the REX multi-tenant expense management platform. */
//package com.rex.commodis.model;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//import org.hibernate.annotations.ColumnDefault;
//
//import java.time.OffsetDateTime;
//
//@Entity
//@Table(name = "invoice_positions_beneficiary", schema = "invoices")
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class InvoicePositionBeneficiary {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", nullable = false, updatable = false)
//    private Long id;
//
//    @Column(name = "beneficiary_id", nullable = false)
//    private Long beneficiaryId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "beneficiary_id", insertable = false, updatable = false)
//    private Beneficiary beneficiary;
//
//    @Column(name = "invoice_positions_id", nullable = false)
//    private Long invoicePositionId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "invoice_positions_id", insertable = false, updatable = false)
//    @JsonIgnore
//    private InvoicePosition invoicePosition;
//
//    @Column(name = "is_deleted")
//    @ColumnDefault("false")
//    private Boolean isDeleted = false;
//
//    @CreationTimestamp
//    @Column(name = "created_at", nullable = false, updatable = false)
//    private OffsetDateTime createdAt;
//
//    @UpdateTimestamp
//    @Column(name = "updated_at")
//    private OffsetDateTime updatedAt;
//}


