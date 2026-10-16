/** JPA entity representing the Picklist table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * User-specific picklists (e.g., favorites).
 */
@Entity
@Table(name = "picklist", schema = "rexbase")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Picklist {

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Id
    @SequenceGenerator(name = "GEN_Picklist", sequenceName = "picklist_id_seq", schema = "rexbase", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_Picklist")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    /** Picklist display name */
    @Column(name = "displayname", nullable = false, columnDefinition = "citext")
    private String displayName;

    /** Owner rexuser */
    @Column(name = "rexuser_id", nullable = false)
    private Long rexUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rexuser_id", insertable = false, updatable = false)
    private RexUser rexUser;

    /** Order for display/sorting */
    @Column(name = "sortid", nullable = false, columnDefinition = "SMALLINT")
    private Short sortId;
}
