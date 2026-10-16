package com.rex.commodis.service;

import com.rex.commodis.dto.events.DashboardTeamEventCountResponseDto;
import com.rex.commodis.model.enums.TeamEventStatusEnum;
import com.rex.commodis.repository.events.TeamEventApproverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class TeamEventApproverCountServiceImpl implements TeamEventApproverCountService {


    private static final List<TeamEventStatusEnum>
            PENDING_APPROVER_STATUSES = List.of(
            TeamEventStatusEnum.SUBMITTED,
            TeamEventStatusEnum.RESUBMITTED,
            TeamEventStatusEnum.CORRECTION_REQUESTED
    );

    private final TeamEventApproverRepository teamEventApproverRepository;


    public TeamEventApproverCountServiceImpl(TeamEventApproverRepository teamEventApproverRepository) {
        this.teamEventApproverRepository = teamEventApproverRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardTeamEventCountResponseDto getTeamEventApproverCount(Long rexUserId) {


        long count = teamEventApproverRepository
                .countApproverTeamEventsByStatuses(rexUserId, PENDING_APPROVER_STATUSES);


        return DashboardTeamEventCountResponseDto.builder()
                .teamEventApprover(count)
                .build();
    }
}