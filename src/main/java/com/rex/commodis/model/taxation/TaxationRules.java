/** JPA entity representing the TaxationRules table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.taxation;

import com.rex.commodis.model.Platform;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "taxation_rules", schema = "taxation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxationRules {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "platform_id", nullable = false)
    private Long platformId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id", insertable = false, updatable = false)
    private Platform platform;

    @Column(name = "payroll_staff", nullable = false)
    private String payrollStaff;

    @Column(name = "payroll_external", nullable = false)
    private String payrollExternal;

    @Column(name = "vat_deductible", nullable = false)
    private String vatDeductible;

    @Column(name = "opex_deductible", nullable = false)
    private String opexDeductible;

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
