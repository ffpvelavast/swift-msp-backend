package com.rex.commodis.service;

import com.rex.commodis.dto.events.TeamEventTypeDropDownDto;
import com.rex.commodis.repository.events.TeamEventTypeProjection;
import com.rex.commodis.repository.events.TeamEventTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamEventTypeServiceImplTest {

    @Mock private TeamEventTypeRepository teamEventTypeRepository;
    @Mock private RexUserService rexUserService;

    @InjectMocks
    private TeamEventTypeServiceImpl teamEventTypeService;

    private static final Long PLATFORM_ID = 10L;
    private static final Long REX_USER_ID = 1L;
    private static final String LOCALE = "en_US";

    @Test
    void getTeamEventTypes_resolvesLocaleFetchesTypesAndMapsProjectionToDto() {
        TeamEventTypeProjection lunch = teamEventType(1L, "Lunch", "Team lunch");
        TeamEventTypeProjection workshop = teamEventType(2L, "Workshop", "Training session");

        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(teamEventTypeRepository.findTeamEventTypes(PLATFORM_ID, LOCALE))
                .thenReturn(List.of(lunch, workshop));

        List<TeamEventTypeDropDownDto> result =
                teamEventTypeService.getTeamEventTypes(PLATFORM_ID, REX_USER_ID);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTeamEventTypeId()).isEqualTo(1L);
        assertThat(result.get(0).getDisplayText()).isEqualTo("Lunch");
        assertThat(result.get(0).getDescription()).isEqualTo("Team lunch");
        assertThat(result.get(1).getTeamEventTypeId()).isEqualTo(2L);
        assertThat(result.get(1).getDisplayText()).isEqualTo("Workshop");
        assertThat(result.get(1).getDescription()).isEqualTo("Training session");

        verify(rexUserService).getLocale(REX_USER_ID);
        verify(teamEventTypeRepository).findTeamEventTypes(PLATFORM_ID, LOCALE);
    }

    @Test
    void getTeamEventTypes_whenRepositoryReturnsNoRows_returnsEmptyList() {
        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(teamEventTypeRepository.findTeamEventTypes(PLATFORM_ID, LOCALE))
                .thenReturn(List.of());

        List<TeamEventTypeDropDownDto> result =
                teamEventTypeService.getTeamEventTypes(PLATFORM_ID, REX_USER_ID);

        assertThat(result).isEmpty();
        verify(rexUserService).getLocale(REX_USER_ID);
        verify(teamEventTypeRepository).findTeamEventTypes(PLATFORM_ID, LOCALE);
    }

    private TeamEventTypeProjection teamEventType(
            Long teamEventTypeId,
            String displayText,
            String description
    ) {
        return new TeamEventTypeProjection() {
            @Override
            public Long getTeamEventTypeId() {
                return teamEventTypeId;
            }

            @Override
            public String getDisplayText() {
                return displayText;
            }

            @Override
            public String getDescription() {
                return description;
            }
        };
    }
}
