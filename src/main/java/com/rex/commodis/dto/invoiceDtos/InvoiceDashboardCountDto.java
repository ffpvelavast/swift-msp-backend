/** DTO carrying InvoiceDashboardCount data for the invoice listing, details, or delegation flow. */
package com.rex.commodis.dto.invoiceDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDashboardCountDto {

    @JsonProperty("invoices_count")
    private Long invoicesCount;
}
