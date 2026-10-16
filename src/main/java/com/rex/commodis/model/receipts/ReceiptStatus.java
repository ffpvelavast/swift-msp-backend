/** JPA entity representing the ReceiptStatus table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.receipts;

import java.time.OffsetDateTime;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.rex.commodis.model.enums.ReceiptStatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "receipt_status", schema = "receipts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptStatus {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Short id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status_code", nullable = false)
    private ReceiptStatusEnum status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "display_text", nullable = false, columnDefinition = "jsonb")
    private Map<String, String> displayText;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}