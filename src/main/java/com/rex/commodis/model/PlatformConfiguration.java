/** Spring configuration class for PlatformConfiguration; registers beans or properties used across the application. */
package com.rex.commodis.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

import io.hypersistence.utils.hibernate.type.range.PostgreSQLRangeType;
import io.hypersistence.utils.hibernate.type.range.Range;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "platform_configuration", schema = "rexbase")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(PlatformConfigurationId.class) // 🔥 composite key
public class PlatformConfiguration {

@Id
private Long id;

@Id
@Column(name = "activefrom")
private LocalDate activeFrom;

@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "platform_id", nullable = false)
private Platform platform;

@Column(name = "invoice_storage", nullable = false)
private String invoiceStorage;

@Column(name = "invoice_storage_link")
private String invoiceStorageLink;

@Column(name = "receipt_storage", nullable = false)
private String receiptStorage;

@Column(name = "receipt_storage_link")
private String receiptStorageLink;

@Column(name = "keycloak_url")
private String keycloakUrl;

@Column(name = "keycloak_realm")
private String keycloakRealm;

@Column(name = "projectnumber_required_invoices")
private Boolean projectNumberRequiredInvoices;

@Column(name = "projectnumber_required_receipts")
private Boolean projectNumberRequiredReceipts;

@Column(name = "activethrough", nullable = false)
private LocalDate activeThrough;

@Type(PostgreSQLRangeType.class)
@Column(name = "activeperiod", columnDefinition = "daterange", insertable = false, updatable = false)
private Range<LocalDate> activePeriod;

@Column(name = "created_at", nullable = false, updatable = false)
private Instant createdAt;

}
