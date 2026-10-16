/** Response DTO returned by the ReceiptSidePanel API endpoint. */
package com.rex.commodis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptSidePanelResponseDto {

    @JsonProperty("receiptData")
    private ReceiptSidePanelDataDto receiptSidePanelDataDto;

    @JsonProperty("receiptHistory")
    private List<ReceiptSidePanelHistoryDto> receiptSidePanelHistoryDto;
}
