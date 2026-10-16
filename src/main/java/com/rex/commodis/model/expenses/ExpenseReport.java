/** JPA entity representing the ExpenseReport table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.expenses;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.rex.commodis.model.RexUser;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "expense_reports", schema = "expenses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseReport {

    @Id
    @SequenceGenerator(name = "GEN_ExpenseReport", sequenceName = "expense_reports_id_seq", schema = "expenses", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_ExpenseReport")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "rexuser_id", nullable = false)
    private Long rexUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rexuser_id", insertable = false, updatable = false)
    private RexUser rexUser;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", insertable = false, updatable = false)
    private RexUser creator;

    @Column(name = "report_title", nullable = false, columnDefinition = "citext")
    private String reportTitle;

    @Column(name = "reportdate_from", nullable = false)
    private LocalDate reportDateFrom;

    @Column(name = "reportdate_through", nullable = false)
    private LocalDate reportDateThrough;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}