/** JPA entity representing the PlatformKeycloakClients table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.ClientType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "platform_keycloakclients", schema = "rexbase")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(PlatformKeycloakClientsId.class)
public class PlatformKeycloakClients {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Id
    @Column(name = "activefrom", nullable = false)
    private LocalDate activeFrom;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "platform_id", nullable = false)
    private Platform platform;

    @Column(name = "clientid", nullable = false)
    private String clientId;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Enumerated(EnumType.STRING)
    @Column(name = "client_type")
    private ClientType clientType;

    @Column(name = "client_description")
    private String clientDescription;

    @Column(name = "activethrough", nullable = false)
    private LocalDate activeThrough;

    @Column(name = "activeperiod", insertable = false, updatable = false, columnDefinition = "daterange")
    private String activePeriod;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}