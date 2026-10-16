/** EmployerIdentNumbers: part of the REX multi-tenant expense management platform. */
//package com.rex.commodis.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//import java.time.Instant;
//
///**
// * Entity mapping for employer identification numbers (payroll/accounting).
// */
//@Entity
//@Table(name = "employer_identnumbers", schema = "rexbase")
//@IdClass(EmployerIdentNumbersId.class)
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class EmployerIdentNumbers {
//
//    @Id
//    @Column(name = "employer_id", nullable = false)
//    private Long employerId;
//
//    @Id
//    @Column(name = "companyid_payroll")
//    private String companyIdPayroll;
//
//    @Column(name = "companyid_accounting")
//    private String companyIdAccounting;
//
//    @CreationTimestamp
//    @Column(name = "created_at", nullable = false, updatable = false)
//    private Instant createdAt;
//
//    @Column(name = "platform_id")
//    private Long platformId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "employer_id", insertable = false, updatable = false)
//    private Employer employer;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "platform_id", insertable = false, updatable = false)
//    private Platform platform;
//}



