/** DTO carrying InvoiceStatus data for the invoice listing, details, or delegation flow. */
package com.rex.commodis.dto.invoices;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

import com.rex.commodis.model.enums.InvoiceStatusEnum;

@Data
@Builder
public class InvoiceStatusDto {
    private Long id;
    private Long invoiceId;
    private InvoiceStatusEnum status;
    private String statusCreatorName;
    private OffsetDateTime createdAt;
}
