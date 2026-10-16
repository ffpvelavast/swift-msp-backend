/** DTO carrying InvoicePosition data for the invoice listing, details, or delegation flow. */
package com.rex.commodis.dto.invoices;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
public class InvoicePositionDto {
    private Long id;
    private Long invoiceId;
    private String externalPositionNumber;
    private Integer positionLineNumber;
    private String factualApprover;
    private String positionText;
    private Integer positionQuantity;
    private BigDecimal positionAmountGross;
    private BigDecimal positionAmountNet;
    private BigDecimal positionAmountVat;
    private Integer positionVatRate;
    private Boolean payrollTaxationRequired;
    private Long taxationId;

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

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
