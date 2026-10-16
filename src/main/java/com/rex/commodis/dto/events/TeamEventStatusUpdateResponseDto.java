/** Response DTO returned by the TeamEventStatusUpdate API endpoint. */
package com.rex.commodis.dto.events;

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
public class TeamEventStatusUpdateResponseDto {

    @JsonProperty("teamEventId")
    @Schema(description = "ID of the updated team event", example = "10")
    private Long teamEventId;

    @JsonProperty("status")
    @Schema(description = "The new active status of the team event", example = "APPROVED")
    private String status;
}