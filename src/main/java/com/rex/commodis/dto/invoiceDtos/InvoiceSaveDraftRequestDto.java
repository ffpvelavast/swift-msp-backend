/** DTO carrying InvoiceSaveDraftRequest data for the invoice listing, details, or delegation flow. */
package com.rex.commodis.dto.invoiceDtos;

import lombok.Data;

import java.util.List;

@Data
public class InvoiceSaveDraftRequestDto {

    private Long invoiceId;
    private InvoiceEventDataDto eventData;
    private Boolean inheritanceUserSet;
    private List<Long> beneficiaryIds;
    private List<Long> guestIds;
    private Long taxReasonId;
    private String note;
}
