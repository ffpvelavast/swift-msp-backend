/** JPA entity representing the Filetypes table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.receipts;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "filetypes", schema = "receipts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Filetypes {

    @Id
    @SequenceGenerator(
            name = "GEN_Filetypes",
            sequenceName = "filetypes_id_seq",
            schema = "receipts",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_Filetypes")
    @Column(name = "id", nullable = false, updatable = false)
    private Short id;

    @Column(name = "display_text", nullable = false, columnDefinition = "jsonb")
    private String displayText;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}