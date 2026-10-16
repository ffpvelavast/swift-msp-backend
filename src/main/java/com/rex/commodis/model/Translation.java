/** JPA entity representing the Translation table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "translations", schema = "rexbase")
@Getter
@Setter
public class Translation {
    @Id
    @SequenceGenerator(name = "GEN_Translation", sequenceName = "translations_id_seq", schema = "rexbase", allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_Translation")
    private Long id;

    @Column(nullable = false)
    private String component;

    @Column(nullable = false)
    private String key;

    @Column(columnDefinition = "jsonb", nullable = false)
    private String translations;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
