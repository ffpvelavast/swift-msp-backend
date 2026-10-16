/** KeycloakSim: part of the REX multi-tenant expense management platform. */
//package com.rex.commodis.model;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//import org.hibernate.annotations.ColumnDefault;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.DynamicInsert;
//
//import java.time.OffsetDateTime;
//import java.util.UUID;
//
//@Getter
//@Setter
//@Entity
//@DynamicInsert
//@Table(name = "keycloak_sim", schema = "rexbase")
//public class KeycloakSim {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", nullable = false)
//    private Long id;
//
//    @Column(name = "keycloak_uuid", nullable = false, unique = true)
//    private UUID keycloakUuid;
//
//    @Column(name = "realm", nullable = false)
//    private String realm;
//
//    @Column(name = "lastname", nullable = false)
//    private String lastname;
//
//    @Column(name = "firstname", nullable = false)
//    private String firstname;
//
//    @Column(name = "organisation", nullable = false)
//    private String organisation;
//
//    @CreationTimestamp
//    @Column(name = "created_at", nullable = false)
//    @ColumnDefault("CURRENT_TIMESTAMP(6)")
//    private OffsetDateTime createdAt;
//
//    @Column(name = "updated_at")
//    private OffsetDateTime updatedAt;
//}



