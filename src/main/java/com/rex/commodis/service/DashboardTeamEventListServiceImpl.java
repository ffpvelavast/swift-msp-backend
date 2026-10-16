package com.rex.commodis.service;

import com.rex.commodis.dto.events.DashboardTeamEventListForApproverDto;
import com.rex.commodis.dto.events.DashboardTeamEventListResponseDto;
import com.rex.commodis.repository.events.TeamEventHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardTeamEventListServiceImpl implements DashboardTeamEventListService {

    private final TeamEventHistoryRepository teamEventHistoryRepository;
    private final RexUserService rexUserService;
    @Override
    @Transactional(readOnly = true)
    public DashboardTeamEventListResponseDto getTeamEventListForApprover(Long rexUserId) {
        String locale = rexUserService.getLocale(rexUserId);
        List<DashboardTeamEventListForApproverDto> items =
                teamEventHistoryRepository.findTeamEventsListForApprover(rexUserId , locale)
                        .stream()
                        .map(p -> DashboardTeamEventListForApproverDto.builder()
                                .eventId(p.getTeamEventId())
                                .eventName(p.getEventName())
                                .eventDate(p.getEventDate())
                                .status(p.getStatus())
                                .build())
                        .toList();

        return DashboardTeamEventListResponseDto.builder()
                .data(items)
                .build();
    }
}