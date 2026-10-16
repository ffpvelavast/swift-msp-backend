package com.rex.commodis.service;

import com.rex.commodis.dto.events.DashboardTeamEventCountResponseDto;
import com.rex.commodis.model.enums.TeamEventStatusEnum;
import com.rex.commodis.repository.events.TeamEventApproverRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamEventApproverCountServiceImplTest {

    @Mock private TeamEventApproverRepository teamEventApproverRepository;

    @InjectMocks
    private TeamEventApproverCountServiceImpl service;

    private static final Long REX_USER_ID = 1L;

    @Test
    void getTeamEventApproverCount_returnsRepositoryCountAndUsesPendingStatuses() {
        when(teamEventApproverRepository.countApproverTeamEventsByStatuses(
                org.mockito.ArgumentMatchers.eq(REX_USER_ID),
                org.mockito.ArgumentMatchers.anyList()
        )).thenReturn(4L);

        DashboardTeamEventCountResponseDto result =
                service.getTeamEventApproverCount(REX_USER_ID);

        assertThat(result.getTeamEventApprover()).isEqualTo(4L);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<TeamEventStatusEnum>> statusesCaptor =
                ArgumentCaptor.forClass(List.class);
        verify(teamEventApproverRepository).countApproverTeamEventsByStatuses(
                org.mockito.ArgumentMatchers.eq(REX_USER_ID),
                statusesCaptor.capture()
        );
        assertThat(statusesCaptor.getValue()).containsExactly(
                TeamEventStatusEnum.SUBMITTED,
                TeamEventStatusEnum.RESUBMITTED,
                TeamEventStatusEnum.CORRECTION_REQUESTED
        );
    }

    @Test
    void getTeamEventApproverCount_whenRepositoryReturnsZero_returnsZeroCount() {
        when(teamEventApproverRepository.countApproverTeamEventsByStatuses(
                org.mockito.ArgumentMatchers.eq(REX_USER_ID),
                org.mockito.ArgumentMatchers.anyList()
        )).thenReturn(0L);

        DashboardTeamEventCountResponseDto result =
                service.getTeamEventApproverCount(REX_USER_ID);

        assertThat(result.getTeamEventApprover()).isZero();
        verify(teamEventApproverRepository).countApproverTeamEventsByStatuses(
                org.mockito.ArgumentMatchers.eq(REX_USER_ID),
                org.mockito.ArgumentMatchers.anyList()
        );
    }
}
