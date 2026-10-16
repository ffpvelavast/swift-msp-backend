/** JPA entity representing the GuestType table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Types of guests for event or picklist notifications.
 */
@Entity
@Table(name = "guest_type", schema = "rexbase")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestType {

    @Id
    @SequenceGenerator(name = "GEN_GuestType", sequenceName = "guesttype_id_seq", schema = "rexbase", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_GuestType")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "guest_enum", nullable = false)
    private String guestEnum;

    // display_text is jsonb in DB
    @Column(name = "display_text", nullable = false, columnDefinition = "jsonb")
    private String displayText;

    @Column(name = "description", columnDefinition = "jsonb")
    private String description;

    @Column(name = "business_guest")
    private Boolean businessGuest;
}
