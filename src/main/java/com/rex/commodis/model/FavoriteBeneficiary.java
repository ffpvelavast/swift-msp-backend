/** JPA entity representing the FavoriteBeneficiary table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "beneficiary_favorites", schema = "rexbase")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteBeneficiary {

    @Id
    @SequenceGenerator(name = "GEN_FavoriteBeneficiary", sequenceName = "beneficiary_favorites_id_seq", schema = "rexbase", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_FavoriteBeneficiary")
    @Column(name = "id", nullable = false)
    private Long id;


    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "sortid", nullable = false)
    private Short sortId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rexuser_id", nullable = false)
    private RexUser rexUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiary_id", nullable = false)
    private Beneficiary beneficiary;

    @Override
    public String toString() {
        return "FavoriteBeneficiary(id=" + id + ", sortId=" + sortId + ")";
    }
}