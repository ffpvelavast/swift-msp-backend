/** DTO carrying InvoiceDelegateDetails data for the invoice listing, details, or delegation flow. */
package com.rex.commodis.dto.invoiceDtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class InvoiceDelegateDetailsDto {

    private Long id;
    private String vendorName;
    private String invoiceTitle;
    private LocalDate invoiceDate;
    private String invoiceNumber;
    private BigDecimal invoiceTotal;
    private String currency;
    private String status;
    private String delegatedTo;
    private Long delegatedUserId;
}