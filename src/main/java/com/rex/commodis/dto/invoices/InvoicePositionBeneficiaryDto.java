/** DTO carrying InvoicePositionBeneficiary data for the invoice listing, details, or delegation flow. */
package com.rex.commodis.dto.invoices;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class InvoicePositionBeneficiaryDto {
    private Long id;
    private Long beneficiaryId;
    private Long invoicePositionId;
    private Boolean isDeleted;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
