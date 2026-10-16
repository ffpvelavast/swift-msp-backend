/** DTO carrying InvoiceGetList data for the invoice listing, details, or delegation flow. */
package com.rex.commodis.dto.invoiceDtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@Builder
public class InvoiceGetListDto {
    private Long id;
    private String vendorName;
    private String invoiceTitle;
    private LocalDate invoiceDate;
    private String invoiceNumber;
    private BigDecimal invoiceTotal;
    private String currency;
    private String status;
}