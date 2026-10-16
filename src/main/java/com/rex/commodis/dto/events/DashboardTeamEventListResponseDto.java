package com.rex.commodis.dto.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class DashboardTeamEventListResponseDto {
    @Builder.Default
    private List<DashboardTeamEventListForApproverDto> data = Collections.emptyList();
}
