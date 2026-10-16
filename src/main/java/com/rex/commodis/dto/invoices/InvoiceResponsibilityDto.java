/** DTO carrying InvoiceResponsibility data for the invoice listing, details, or delegation flow. */
package com.rex.commodis.dto.invoices;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class InvoiceResponsibilityDto {
    private Long id;
    private Long invoiceId;
    private Long rexUserId;
    private Long delegateId;
    private OffsetDateTime delegatedAt;
    private String remarks;
    private OffsetDateTime responsibleFrom;
    private OffsetDateTime responsibleThrough;
    private Boolean isActive;
    private OffsetDateTime createdAt;
}
