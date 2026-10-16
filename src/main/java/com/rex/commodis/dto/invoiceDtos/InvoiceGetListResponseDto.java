/** DTO carrying InvoiceGetListResponse data for the invoice listing, details, or delegation flow. */
package com.rex.commodis.dto.invoiceDtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InvoiceGetListResponseDto {
    private InvoiceGetListMetaDataDto meta;
    private List<InvoiceGetListDto> data;

    private InvoiceStatusCountDto statusCounts;
}