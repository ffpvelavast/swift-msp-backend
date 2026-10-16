/** JPA entity representing the Guest table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "guest", schema = "rexbase")
public class Guest {

    @Id
    @SequenceGenerator(name = "GEN_Guest", sequenceName = "guest_id_seq", schema = "rexbase", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_Guest")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "platform_id", nullable = false)
    private Long platformId;

    @Column(name = "organisationwide_guest", nullable = false)
    private Boolean organisationwideGuest;

    @Column(name = "creator_rexuser_id", nullable = false)
    private Long creatorRexUserId;

    @Column(name = "firstname", nullable = false, columnDefinition = "citext")
    private String firstname;

    @Column(name = "lastname", nullable = false, columnDefinition = "citext")
    private String lastname;

    @Column(name = "organization", nullable = false, columnDefinition = "citext")
    private String organization;

    @Column(name = "jobtitle", nullable = false, columnDefinition = "citext")
    private String jobTitle;

    @Column(name = "listname", insertable = false, updatable = false, columnDefinition = "citext")
    private String listname;

    @Column(name = "fullname", insertable = false, updatable = false, columnDefinition = "citext")
    private String fullName;

    @Column(name = "taxation_abroad", nullable = false)
    private Boolean taxationAbroad;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_guesttype_id", nullable = false)
    private GuestType defaultGuestType;

    @Override
    public String toString() {
        return "Guest(id=" + id + ", listname=" + listname + ")";
    }
}
