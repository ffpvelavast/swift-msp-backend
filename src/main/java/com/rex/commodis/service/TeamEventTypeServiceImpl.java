/** Service implementation for TeamEventType business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.dto.events.TeamEventTypeDropDownDto;
import com.rex.commodis.repository.events.TeamEventTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamEventTypeServiceImpl implements TeamEventTypeService {

    private final TeamEventTypeRepository teamEventTypeRepository;
    private final RexUserService rexUserService;

    @Override
    @Transactional(readOnly = true)
    public List<TeamEventTypeDropDownDto> getTeamEventTypes(Long platformId, Long rexUserId) {

        String locale = rexUserService.getLocale(rexUserId);

        return teamEventTypeRepository.findTeamEventTypes(platformId, locale)
                .stream()
                .map(teamEventType -> TeamEventTypeDropDownDto.builder()
                        .teamEventTypeId(teamEventType.getTeamEventTypeId())
                        .displayText(teamEventType.getDisplayText())
                        .description(teamEventType.getDescription())
                        .build()
                )
                .toList();
    }
}