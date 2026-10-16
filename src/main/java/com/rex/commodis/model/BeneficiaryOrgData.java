/** JPA entity representing the BeneficiaryOrgData table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */


package com.rex.commodis.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

import io.hypersistence.utils.hibernate.type.range.PostgreSQLRangeType;
import io.hypersistence.utils.hibernate.type.range.Range;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

/**
 * Time-bound organizational details for a BeneficiaryAccount.
 */
@Entity
@Table(name = "beneficiary_orgdata", schema = "rexbase")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class BeneficiaryOrgData {

    @Id
    @SequenceGenerator(name = "GEN_BeneficiaryOrgData", sequenceName = "beneficiary_orgdata_id_seq", schema = "rexbase", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_BeneficiaryOrgData")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "beneficiary_id", nullable = false)
    private Long beneficiaryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiary_id", insertable = false, updatable = false)
    private Beneficiary beneficiary;

    @Column(name = "employer_id", nullable = false)
    private Long employerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", insertable = false, updatable = false)
    private Employer employer;

    @Type(PostgreSQLRangeType.class)
    @Column(name = "validperiod", columnDefinition = "daterange", insertable = false, updatable = false)
    private Range<LocalDate> validPeriod;

    @Column(name = "validfrom", nullable = false)
    private LocalDate validFrom;

    @Column(name = "validthrough")
    private LocalDate validThrough;

    @Column(name = "personnelnumber_system")
    private String personnelNumberSystem;

    @Column(name = "personnelnumber_payroll")
    private String personnelNumberPayroll;

    @Column(name = "personnelnumber_accounting")
    private String personnelNumberAccounting;

    @Column(name = "identity_tag", columnDefinition = "citext")
    private String identityTag;

    @Column(name = "companysite", columnDefinition = "citext")
    private String companySite;

    @Column(name = "orgunit")
    private String orgUnit;

    @Column(name = "orgunit_above")
    private String orgUnitAbove;

    @Column(name = "costcenter", columnDefinition = "citext")
    private String costCenter;

    @Column(name = "companyid_payroll")
    private String companyIdPayroll;

    @Column(name = "companyid_accounting")
    private String companyIdAccounting;

    @Column(name = "manager", nullable = false)
    @ColumnDefault("false")
    private Boolean manager = false;

    @Column(name = "active", nullable = false)
    @ColumnDefault("false")
    private Boolean active = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}

