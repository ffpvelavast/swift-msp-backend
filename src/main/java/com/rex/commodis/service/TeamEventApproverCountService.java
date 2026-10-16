package com.rex.commodis.service;

import com.rex.commodis.dto.events.DashboardTeamEventCountResponseDto;
public interface TeamEventApproverCountService {

    DashboardTeamEventCountResponseDto getTeamEventApproverCount(Long rexUserId);
}
