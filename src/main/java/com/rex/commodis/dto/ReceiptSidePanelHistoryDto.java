/** Data transfer object carrying ReceiptSidePanelHistory data between the API layer and the service/client. */
package com.rex.commodis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptSidePanelHistoryDto {

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    @JsonProperty("status")
    private String status;

    @JsonProperty("isActive")
    private Boolean isActive;
    
    @JsonProperty("createdBy")
    ReceiptStatusCreatorDto statusCreator;
    
}
