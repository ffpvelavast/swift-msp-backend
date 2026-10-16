package com.rex.commodis.dto.invoiceDtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.security.PrivateKey;

@Data
@Builder
public class InvoiceDashboardListDto {
    private long invoiceId;
    private String vendorName;
    private BigDecimal invoiceTotal;
    private String currency;
    private String status;
}
