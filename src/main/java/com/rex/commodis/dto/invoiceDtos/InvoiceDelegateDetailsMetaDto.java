/** DTO carrying InvoiceDelegateDetailsMeta data for the invoice listing, details, or delegation flow. */
package com.rex.commodis.dto.invoiceDtos;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
@Getter
@Builder
public class InvoiceDelegateDetailsMetaDto {
    private Long rexUserId;
    private LocalDate from;
    private LocalDate to;
    private int page;
    private int pageSize;
}