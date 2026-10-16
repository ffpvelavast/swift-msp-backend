/** DTO representing the InvoiceTransferLineItemDto data structure received from the D-velop document management webhook. */
package com.rex.commodis.dto.dvelopDtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InvoiceTransferLineItemDto {

    private String internalId;

    private Integer lineNo;

    private InvoiceTransferUserDto verifiedBy;

    private BigDecimal grossAmount;

    private BigDecimal netAmount;

    private BigDecimal vatAmount;

    private InvoiceTransferTaxCodeDto taxCode;

    private InvoiceTransferQuantityDto quantity;

    private String description;

    private String custom1;
    private String custom2;
    private String custom3;
    private String custom4;
    private String custom5;
    private String custom6;
    private String custom7;
    private String custom8;
    private String custom9;
    private String custom10;
    private String custom11;
    private String custom12;
    private String custom13;
    private String custom14;
    private String custom15;
    private String custom16;
    private String custom17;
    private String custom18;
    private String custom19;
    private String custom20;
}
