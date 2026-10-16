/** DTO carrying InvoiceDelegateDetailsResponse data for the invoice listing, details, or delegation flow. */
package com.rex.commodis.dto.invoiceDtos;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class InvoiceDelegateDetailsResponseDto {

    private InvoiceDelegateDetailsMetaDto meta;
    private List<InvoiceDelegateDetailsDto> data;
}