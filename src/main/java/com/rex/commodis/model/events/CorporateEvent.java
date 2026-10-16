/** JPA entity representing the CorporateEvent table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.events;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.rex.commodis.model.Employer;
import com.rex.commodis.model.Platform;
import com.rex.commodis.model.events.CorporateEventAdmin.CorporateEventAdmin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "corporate_event", schema = "events",
        uniqueConstraints = @UniqueConstraint(name = "corpevent_uq", columnNames = {"platform_id", "event_title"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CorporateEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "corporate_event_seq")
    @SequenceGenerator(
            name = "corporate_event_seq",
            sequenceName = "events.corporate_event_id_seq",
            allocationSize = 1
    )
    @Column(name = "id", nullable = false)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "booking_released_at")
    private OffsetDateTime bookingReleasedAt;

    @Column(name = "booking_released_by")
    private String bookingReleasedBy;

    @Column(name = "processing_released_at")
    private OffsetDateTime processingReleasedAt;

    @Column(name = "processing_released_by")
    private String processingReleasedBy;

    @Column(name = "processing_id")
    private Long processingId;

    @Column(name = "sortid", columnDefinition = "SMALLINT")
    private Short sortId;

    @Column(name = "event_title", nullable = false, columnDefinition = "citext")
    private String eventTitle;

    @Column(name = "event_manager", columnDefinition = "citext")
    private String eventManager;

    @Column(name = "event_budget", precision = 19, scale = 2)
    private BigDecimal eventBudget;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "client_ordernumber")
    private String clientOrderNumber;

    @Column(name = "costcenter")
    private String costCenter;

    @Column(name = "posting_account")
    private String postingAccount;

    @Column(name = "inheritance_mode", nullable = false)
    private String inheritanceMode;

    @Column(name = "inheritance_userset", nullable = false)
    private Boolean inheritanceUserSet = false;

    @Column(name = "bookable_from", nullable = false)
    private LocalDate bookableFrom;

    @Column(name = "bookable_through", nullable = false)
    private LocalDate bookableThrough;

    @Column(name = "platform_id", nullable = false)
    private Long platformId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id", insertable = false, updatable = false)
    private Platform platform;

    @Column(name = "event_employer_id", nullable = false)
    private Long eventEmployerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_employer_id", insertable = false, updatable = false)
    private Employer employer;

    @OneToMany(mappedBy = "corporateEvent", cascade = CascadeType.ALL)
    private List<CorporateEventAdmin> admins;
}
