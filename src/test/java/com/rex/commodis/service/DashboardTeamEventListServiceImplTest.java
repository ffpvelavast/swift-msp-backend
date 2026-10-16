package com.rex.commodis.service;

import com.rex.commodis.dto.events.DashboardTeamEventListResponseDto;
import com.rex.commodis.repository.events.DashboardTeamEventListProjection;
import com.rex.commodis.repository.events.TeamEventHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardTeamEventListServiceImplTest {

    @Mock private TeamEventHistoryRepository teamEventHistoryRepository;
    @Mock private RexUserService rexUserService;

    @InjectMocks
    private DashboardTeamEventListServiceImpl service;

    private static final Long REX_USER_ID = 1L;
    private static final String LOCALE = "en_US";

    @Test
    void getTeamEventListForApprover_resolvesLocaleAndMapsRepositoryProjectionsToResponseData() {
        DashboardTeamEventListProjection firstEvent = teamEvent(
                100L,
                "Quarterly dinner",
                LocalDate.of(2024, 6, 10),
                "SUBMITTED"
        );
        DashboardTeamEventListProjection secondEvent = teamEvent(
                101L,
                "Project offsite",
                LocalDate.of(2024, 6, 20),
                "RESUBMITTED"
        );

        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(teamEventHistoryRepository.findTeamEventsListForApprover(REX_USER_ID, LOCALE))
                .thenReturn(List.of(firstEvent, secondEvent));

        DashboardTeamEventListResponseDto result =
                service.getTeamEventListForApprover(REX_USER_ID);

        assertThat(result.getData()).hasSize(2);
        assertThat(result.getData().get(0).getEventId()).isEqualTo(100L);
        assertThat(result.getData().get(0).getEventName()).isEqualTo("Quarterly dinner");
        assertThat(result.getData().get(0).getEventDate()).isEqualTo(LocalDate.of(2024, 6, 10));
        assertThat(result.getData().get(0).getStatus()).isEqualTo("SUBMITTED");
        assertThat(result.getData().get(1).getEventId()).isEqualTo(101L);
        assertThat(result.getData().get(1).getEventName()).isEqualTo("Project offsite");
        assertThat(result.getData().get(1).getEventDate()).isEqualTo(LocalDate.of(2024, 6, 20));
        assertThat(result.getData().get(1).getStatus()).isEqualTo("RESUBMITTED");

        verify(rexUserService).getLocale(REX_USER_ID);
        verify(teamEventHistoryRepository).findTeamEventsListForApprover(REX_USER_ID, LOCALE);
    }

    @Test
    void getTeamEventListForApprover_whenRepositoryReturnsNoRows_returnsEmptyData() {
        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(teamEventHistoryRepository.findTeamEventsListForApprover(REX_USER_ID, LOCALE))
                .thenReturn(List.of());

        DashboardTeamEventListResponseDto result =
                service.getTeamEventListForApprover(REX_USER_ID);

        assertThat(result.getData()).isEmpty();
        verify(rexUserService).getLocale(REX_USER_ID);
        verify(teamEventHistoryRepository).findTeamEventsListForApprover(REX_USER_ID, LOCALE);
    }

    private DashboardTeamEventListProjection teamEvent(
            Long teamEventId,
            String eventName,
            LocalDate eventDate,
            String status
    ) {
        return new DashboardTeamEventListProjection() {
            @Override
            public Long getTeamEventId() {
                return teamEventId;
            }

            @Override
            public String getEventName() {
                return eventName;
            }

            @Override
            public LocalDate getEventDate() {
                return eventDate;
            }

            @Override
            public String getStatus() {
                return status;
            }
        };
    }
}
