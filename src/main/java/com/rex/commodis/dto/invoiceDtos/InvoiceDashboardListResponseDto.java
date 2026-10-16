package com.rex.commodis.dto.invoiceDtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InvoiceDashboardListResponseDto {
    private List<InvoiceDashboardListDto> data;
}
