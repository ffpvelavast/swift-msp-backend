package com.rex.commodis.dto.invoiceDtos;

import lombok.Data;

@Data
public class InvoiceEventDataDto {
    private String eventSource;

    private Long eventId;
}
