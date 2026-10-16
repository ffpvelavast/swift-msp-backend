/** Request DTO for the ReceiptStatusUpdate operation; validated and passed from the controller to the service layer. */
package com.rex.commodis.dto.receipts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rex.commodis.model.enums.ReceiptStatusEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptStatusUpdateRequestDto {

    /**
     * Status string sent from the UI button action.
     * Accepted values: APPROVED | REJECTED | CANCELLED | CORRECTION_REQUESTED
     */
    @NotNull(message = "status is required")
    @Schema(
            description = "Status triggered by UI button",
            allowableValues = {"APPROVED", "REJECTED", "CORRECTION_REQUESTED"},
            example = "APPROVED"
    )
    private ReceiptStatusEnum status;
}