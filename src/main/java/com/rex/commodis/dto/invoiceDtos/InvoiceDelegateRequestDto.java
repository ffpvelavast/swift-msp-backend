/** DTO carrying InvoiceDelegateRequest data for the invoice listing, details, or delegation flow. */
package com.rex.commodis.dto.invoiceDtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceDelegateRequestDto {

    @NotNull
    private Long delegateId;

}