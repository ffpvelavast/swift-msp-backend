package com.rex.commodis.dto;

import com.rex.commodis.dto.events.DashboardTeamEventCountResponseDto;
import com.rex.commodis.dto.invoiceDtos.InvoiceDashboardCountDto;
import com.rex.commodis.dto.receipts.DashboardReceiptApproverCountResponseDto;
import com.rex.commodis.dto.receipts.SubmitterCountDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardAllCountsResponseDto {

    private InvoiceDashboardCountDto invoiceCount;

    private DashboardReceiptApproverCountResponseDto approverReceiptCount;

    private SubmitterCountDto submitterReceiptCount;

    private DashboardTeamEventCountResponseDto teamEventCount;
}