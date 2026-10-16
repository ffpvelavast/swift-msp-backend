/** DTO carrying InvoiceStatusUpdateRequest data for the invoice listing, details, or delegation flow. */
package com.rex.commodis.dto.invoiceDtos;

import com.rex.commodis.model.enums.InvoiceStatusRequestEnum;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class InvoiceStatusUpdateRequestDto {
    @NotNull
    private InvoiceStatusRequestEnum status;
}