/** Response DTO returned by the DashboardApproverReceiptList API endpoint. */
package com.rex.commodis.dto.receipts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardApproverReceiptListResponseDto {

    private List<DashboardApproverReceiptListItemDto> data;
}