/** JPA entity representing the TaxReasonClient table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.taxation;

import com.rex.commodis.model.Platform;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "taxreason_client", schema = "taxation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxReasonClient {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sortid", nullable = false)
    private Short sortId;

    @Column(name = "platform_id", nullable = false)
    private Long platformId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id", insertable = false, updatable = false)
    private Platform platform;

    @Column(name = "taxcategory_client_id", nullable = false)
    private Long taxCategoryClientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxcategory_client_id", insertable = false, updatable = false)
    private TaxCategoryClient taxCategoryClient;

    @Column(name = "display_text", nullable = false, columnDefinition = "jsonb")
    private String displayText;

    @Column(name = "help_text", columnDefinition = "jsonb")
    private String helpText;

    @Column(name = "description", columnDefinition = "jsonb")
    private String description;

    @Column(name = "eligible_invoice", nullable = false)
    private Boolean eligibleInvoice;

    @Column(name = "eligible_receipt", nullable = false)
    private Boolean eligibleReceipt;

    @Column(name = "permitted_value", precision = 8, scale = 2)
    private BigDecimal permittedValue;

    @Column(name = "activefrom", nullable = false)
    private LocalDate activeFrom;

    @Column(name = "activethrough", nullable = false)
    private LocalDate activeThrough;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
