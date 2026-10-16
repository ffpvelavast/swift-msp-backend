/** Response DTO returned by the TeamEventDetails API endpoint. */
package com.rex.commodis.dto.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamEventDetailsResponseDto {

    @JsonProperty("team_event")
    private TeamEventDetailsHeaderResponseDto teamEvent;

    @JsonProperty("data")
    private List<TeamEventReceiptDto> data;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;
}