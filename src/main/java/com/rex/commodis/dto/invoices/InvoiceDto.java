/** DTO carrying Invoice data for the invoice listing, details, or delegation flow. */
package com.rex.commodis.dto.invoices;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@Builder
public class InvoiceDto {
    private Long id;
    private String documentNumber;
    private String documentLink;
    private String documentType;
    private String documentName;
    private LocalDate supplyDate;
    private Integer supplyMonth;
    private LocalDate paymentDate;
    private String accountingText;
    private String recipientNumber;
    private String recipient;
    private String vendorNumber;
    private String vendor;
    private String vendorInvoiceNumber;
    private BigDecimal invoiceTotalGross;
    private BigDecimal invoiceTotalNet;
    private BigDecimal invoiceTotalVat;
    private String currency;
    private Boolean payrollTaxationRequired;
    
    // Custom fields
    private String customField1;
    private String customField2;
    private String customField3;
    private String customField4;
    private String customField5;
    private String customField6;
    private String customField7;
    private String customField8;
    private String customField9;
    private String customField10;
    private String customField11;
    private String customField12;
    private String customField13;
    private String customField14;
    private String customField15;
    private String customField16;
    private String customField17;
    private String customField18;
    private String customField19;
    private String customField20;

    private Long projectNumberId;
    private String note;
    private LocalDate documentDate;
    private Long eventsId;
    private Long occasionsId;
    private Long reasonsId;
    private OffsetDateTime releasedAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
