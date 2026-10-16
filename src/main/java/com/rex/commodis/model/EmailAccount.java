/** JPA entity representing the EmailAccount table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "email_accounts", schema = "rexbase")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailAccount {

    @Id
    private Long id;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Column(name = "account_user", nullable = false)
    private String accountUser;

    @Column(name = "account_passwort", nullable = false)
    private String accountPassword;

    @Column(name = "sender_name", nullable = false)
    private String senderName;

    @Column(name = "sender_address", nullable = false)
    private String senderAddress;

    @Column(name = "email_server", nullable = false)
    private String emailServer;

    @Column(name = "email_port", nullable = false)
    private Short emailPort;

    @Column(name = "email_ssl", nullable = false)
    private Boolean emailSsl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
