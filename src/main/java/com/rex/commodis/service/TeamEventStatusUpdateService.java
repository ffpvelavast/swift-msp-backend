/** Service interface defining TeamEventStatusUpdate operations; implemented by TeamEventStatusUpdateServiceImpl. */
package com.rex.commodis.service;

import com.rex.commodis.dto.events.TeamEventStatusUpdateRequestDto;
import com.rex.commodis.dto.events.TeamEventStatusUpdateResponseDto;


public interface TeamEventStatusUpdateService {

    TeamEventStatusUpdateResponseDto updateTeamEventStatus(
            Long teamEventId,
            TeamEventStatusUpdateRequestDto requestDto,
            Long rexUserId
    );
}