/** Response DTO returned by the CreateReceipt API endpoint. */
package com.rex.commodis.dto.receipts;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateReceiptResponseDto {

    private Long receiptId;
}
