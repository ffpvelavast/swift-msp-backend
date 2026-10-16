/** Response DTO returned by the ReceiptStatusUpdate API endpoint. */
package com.rex.commodis.dto.receipts;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptStatusUpdateResponseDto {

    @JsonProperty("receiptId")
    @Schema(description = "ID of the updated receipt", example = "42")
    private Long receiptId;

    @JsonProperty("creatorId")
    @Schema(description = "ID of the approver who performed the action", example = "7")
    private Long creatorId;


    @JsonProperty("status")
    @Schema(description = "The new active status of the receipt", example = "APPROVED")
    private String status;
}