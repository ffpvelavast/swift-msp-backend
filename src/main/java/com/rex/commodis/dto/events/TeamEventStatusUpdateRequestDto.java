/** Request DTO for the TeamEventStatusUpdate operation; validated and passed from the controller to the service layer. */
package com.rex.commodis.dto.events;

import com.rex.commodis.model.enums.TeamEventStatusEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamEventStatusUpdateRequestDto {
    @NotNull(message = "status is required")
    @Schema(description = "Status triggered by approver UI button")
    private TeamEventStatusEnum status;
}