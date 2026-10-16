/** JPA entity representing the Beneficiary table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
// Beneficiary.java
package com.rex.commodis.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "beneficiary", schema = "rexbase")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Beneficiary {

    @Id
    @SequenceGenerator(name = "GEN_Beneficiary", sequenceName = "beneficiary_id_seq", schema = "rexbase", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_Beneficiary")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id")
    private Platform platform;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rexuser_id")
    private RexUser rexUser;

    /** Business UUID for the person */
    @Column(name = "beneficiaryid", nullable = false)
    private java.util.UUID beneficiaryId;

    @Column(name = "commodisid", nullable = false)
    private java.util.UUID commodisId;

    @Column(name = "firstname", nullable = false, columnDefinition = "citext")
    private String firstName;

    @Column(name = "lastname", nullable = false, columnDefinition = "citext")
    private String lastName;

    @Column(name = "academictitle", columnDefinition = "citext")
    private String academicTitle;

    @Column(name = "listname", insertable = false, updatable = false, columnDefinition = "citext")
    private String listName;   
    
    @Column(name = "fullname", insertable = false, updatable = false, columnDefinition = "citext")
    private String fullName;

    @Column(name = "gender", columnDefinition = "char(1)")
    @org.hibernate.annotations.JdbcTypeCode(java.sql.Types.CHAR)
    private String gender;

    @Column(name = "emailaddress", columnDefinition = "citext")
    private String emailAddress;

    /** when this person leaves the organization */
    @Column(name = "leavingdate")
    private LocalDate leavingDate;
    
    @Column(name = "externalsystemid")
    private String externalSystemId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
