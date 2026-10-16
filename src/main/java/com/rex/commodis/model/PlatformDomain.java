/** JPA entity representing the PlatformDomain table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Domain names associated with a Platform.
 */
@Entity
@Table(name = "platform_domain", schema = "rexbase")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformDomain {

    @Id
    @SequenceGenerator(name = "GEN_PlatformDomain", sequenceName = "platform_domain_id_seq", schema = "rexbase", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_PlatformDomain")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "platform_id", nullable = false)
    private Long platformId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id", insertable = false, updatable = false)
    private Platform platform;

    @Column(name = "domainname", unique = true, columnDefinition = "citext")
    private String domainName;
}
