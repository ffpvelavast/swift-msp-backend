/** JPA entity representing the Receipt table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.receipts;

import com.rex.commodis.model.Platform;
import com.rex.commodis.model.taxation.TaxReasonClient;
import com.rex.commodis.model.taxation.TaxationRun;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.rex.commodis.model.ProjectNumber;
import com.rex.commodis.model.RexUser;
import com.rex.commodis.model.events.CorporateEvent;
import com.rex.commodis.model.events.TeamEvent;
import com.rex.commodis.model.expenses.ExpenseReport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "receipts", schema = "receipts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {

    @Id
    @SequenceGenerator(
            name = "GEN_Receipt",
            sequenceName = "receipt_id_seq",
            schema = "receipts",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_Receipt")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "platform_id", nullable = false)
    private Long platformId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id", insertable = false, updatable = false)
    private Platform platform;

    @Column(name = "receiptnumber", nullable = false, unique = true)
    private String receiptNumber;

    @Column(name = "rexuser_id", nullable = false)
    private Long rexUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rexuser_id", insertable = false, updatable = false)
    private RexUser rexUser;

    @Column(name = "expensereport_id", nullable = false)
    private Long expenseReportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expensereport_id", insertable = false, updatable = false)
    private ExpenseReport expenseReport;

    @Column(name = "projectnumber_id")
    private Long projectNumberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectnumber_id", insertable = false, updatable = false)
    private ProjectNumber projectNumber;

    @Column(name = "issuance_region_id", nullable = false)
    private Short issuanceRegionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issuance_region_id", insertable = false, updatable = false)
    private IssuanceRegion issuanceRegion;

    @Column(name = "receipt_title", nullable = false, columnDefinition = "citext")
    private String receiptTitle;

    @Column(name = "receipt_date", nullable = false)
    private LocalDate receiptDate;

    @Column(name = "receipt_amount", nullable = false, precision = 8, scale = 2)
    private BigDecimal receiptAmount;

    @Column(name = "receipt_currency", nullable = false, columnDefinition = "bpchar(3)")
    private String receiptCurrency = "EUR";

    @Column(name = "gratuity", precision = 8, scale = 2)
    private BigDecimal gratuity;

    @Column(name = "amountnet_reducedvat", precision = 8, scale = 2)
    private BigDecimal amountNetReducedVat;

    @Column(name = "amountnet_fullvat", precision = 8, scale = 2)
    private BigDecimal amountNetFullVat;

    @Column(name = "vatrate_reduced")
    private Short vatRateReduced;

    @Column(name = "vatrate_full")
    private Short vatRateFull;

    @Column(name = "internal_note")
    private String internalNote;

    @Column(name = "deduct_team_balance", nullable = false)
    private Boolean deductTeamBalance = false;

    @Column(name = "taxreason_client_id")
    private Long taxReasonClientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxreason_client_id", insertable = false, updatable = false)
    private TaxReasonClient taxReasonClient;

    @Column(name = "taxation_run_id")
    private Long taxationRunId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxation_run_id", insertable = false, updatable = false)
    private TaxationRun taxationRun;

    @Column(name = "processing_released_at")
    private OffsetDateTime processingReleasedAt;

    @Column(name = "processing_released_by")
    private String processingReleasedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}

