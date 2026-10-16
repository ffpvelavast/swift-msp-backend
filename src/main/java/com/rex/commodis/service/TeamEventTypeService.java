/** Service interface defining TeamEventType operations; implemented by TeamEventTypeServiceImpl. */
package com.rex.commodis.service;

import com.rex.commodis.dto.events.TeamEventTypeDropDownDto;

import java.util.List;

public interface TeamEventTypeService {

    List<TeamEventTypeDropDownDto> getTeamEventTypes(Long platformId, Long rexUserId);
}