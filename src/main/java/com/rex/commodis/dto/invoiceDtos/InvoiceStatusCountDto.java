/** DTO carrying InvoiceStatusCount data for the invoice listing, details, or delegation flow. */
package com.rex.commodis.dto.invoiceDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

import com.rex.commodis.model.enums.InvoiceStatusRequestEnum;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceStatusCountDto {

    private Long total;
    private Map<InvoiceStatusRequestEnum, Long> byStatus;
    private Long delegatedInvoiceCount;
}
