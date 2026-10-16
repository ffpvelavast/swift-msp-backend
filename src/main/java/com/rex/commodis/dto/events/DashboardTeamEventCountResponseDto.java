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
@Schema(description = "Dashboard count of team events pending approver action")
public class DashboardTeamEventCountResponseDto {

    @JsonProperty("approver_team_events_count")
    @Schema(
            description = "Count of team events assigned to the logged-in approver ",
            example = "12"
    )
    private long teamEventApprover;
}