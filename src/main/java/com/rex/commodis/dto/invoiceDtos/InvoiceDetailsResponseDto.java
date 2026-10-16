/** DTO carrying InvoiceDetailsResponse data for the invoice listing, details, or delegation flow. */
package com.rex.commodis.dto.invoiceDtos;

import com.rex.commodis.dto.invoices.InvoiceEventDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailsResponseDto {

    private Long invoiceId;
    private String invoiceName;
    private String invoiceNumber;
    private String vendor;
    private String currency;

    private List<InvoicePositionResponseDto> invoicePositions;

    private BigDecimal totalAmountNet;
    private BigDecimal totalVat;
    private BigDecimal totalAmountGross;
    private String status;
    private String statusUpdatedBy;
    private OffsetDateTime statusDate;
    private InvoiceEventDto event;
    private String note;

    //  PROJECTION CONSTRUCTOR
    public InvoiceDetailsResponseDto(
            Long invoiceId,
            String invoiceName,
            String invoiceNumber,
            String vendor,
            String currency,
            BigDecimal totalAmountNet,
            BigDecimal totalVat,
            BigDecimal totalAmountGross,
            String status,
            String statusUpdatedBy,
            OffsetDateTime statusDate,
            String note
    ) {

        this.invoiceId = invoiceId;
        this.invoiceName = invoiceName;
        this.invoiceNumber = invoiceNumber;
        this.vendor = vendor;
        this.currency = currency;
        this.totalAmountNet = totalAmountNet;
        this.totalVat = totalVat;
        this.totalAmountGross = totalAmountGross;
        this.status = status;
        this.statusUpdatedBy = statusUpdatedBy;
        this.statusDate = statusDate;
        this.note = note;
    }
}