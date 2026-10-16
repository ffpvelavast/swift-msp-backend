/** DTO carrying InvoiceEvent data for the invoice listing, details, or delegation flow. */
package com.rex.commodis.dto.invoices;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvoiceEventDto {
    private String eventSource;
    private Long eventId;
    private String eventTitle;
    private String inheritanceMode;
}
