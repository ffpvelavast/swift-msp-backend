/** Data transfer object carrying DashboardApproverReceiptListItem data between the API layer and the service/client. */
package com.rex.commodis.dto.receipts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardApproverReceiptListItemDto {
    private Long receiptId;

    private String receiptTitle;

    private BigDecimal amount;

    private String currency;

    private String status;
}