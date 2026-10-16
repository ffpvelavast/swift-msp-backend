/** DTO carrying InvoiceDelegateResponse data for the invoice listing, details, or delegation flow. */
package com.rex.commodis.dto.invoiceDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvoiceDelegateResponseDto {

    private Boolean success;
    private String message;

}