/** DTO carrying InvoicePositionResponse data for the invoice listing, details, or delegation flow. */
package com.rex.commodis.dto.invoiceDtos;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoicePositionResponseDto {

    private Long id;
    private String externalPositionNumber;
    private Integer positionLineNumber;
    private String positionText;
    private Integer positionQuantity;
    private Integer positionVatRate;
    private BigDecimal positionAmountNet;
    private BigDecimal positionAmountVat;
    private BigDecimal positionAmountGross;
    private String factualApprover;
    private Boolean payrollTaxationRequired;
    private Long taxationId;
    private String customField1;
    private String customField2;
    private String customField3;
    private String customField4;
    private String customField5;
}