package com.rex.commodis.service;

import com.rex.commodis.dto.events.DashboardTeamEventListResponseDto;

public interface DashboardTeamEventListService {

    DashboardTeamEventListResponseDto getTeamEventListForApprover(Long rexUserId);
}
