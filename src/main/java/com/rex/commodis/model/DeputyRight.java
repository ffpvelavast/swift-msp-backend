/** DeputyRight: part of the REX multi-tenant expense management platform. */
//package com.rex.commodis.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//
///**
// * Rights that can be assigned to a deputy.
// */
//@Entity
//@Table(name = "deputy_right", schema = "rexbase")
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class DeputyRight {
//
//    @Id
//    @SequenceGenerator(name = "GEN_DeputyRight", sequenceName = "deputyright_id_seq", schema = "rexbase", allocationSize = 50)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_DeputyRight")
//    @Column(name = "id", nullable = false, updatable = false)
//    private Long id;
//
//    /**
//     * German description of the right
//     */
//    @Column(name = "deputy_right_de", length = 100)
//    private String deputyRightDe;
//
//    /**
//     * English description of the right
//     */
//    @Column(name = "deputy_right_en", length = 100)
//    private String deputyRightEn;
//}



