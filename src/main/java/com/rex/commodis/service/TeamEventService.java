/** Service interface defining TeamEvent operations; implemented by TeamEventServiceImpl. */
package com.rex.commodis.service;

import java.time.LocalDate;
import java.util.List;

import com.rex.commodis.dto.ListResponseDto;
import com.rex.commodis.dto.TeamEventGetListDto;
import com.rex.commodis.dto.TeamEventGetListRequestDto;
import com.rex.commodis.dto.events.*;
import com.rex.commodis.dto.receipts.ReceiptEventRequestDto;
import com.rex.commodis.dto.receipts.BookableEventDto;
import com.rex.commodis.model.enums.TeamEventSortBy;
import com.rex.commodis.model.enums.TeamEventStatusEnum;
import com.rex.commodis.model.events.TeamEvent;

public interface TeamEventService {
	
	public TeamEvent getTeamEvent(ReceiptEventRequestDto eventDto);
	
    ListResponseDto<TeamEventGetListDto, TeamEventGetListRequestDto>
    getTeamEvents(Long rexUserId,
            TeamEventStatusEnum status,
            String eventTitle,
            String submittedBy,
            String projectNumber,
            LocalDate fromDate,
            LocalDate toDate,
            int pageNo,
            int pageSize,
            TeamEventSortBy sortBy,
            String sortOrder
    );

    List<BookableEventDto> getBookableEvents(Long rexUserId);

    TeamEventNotesResponseDto updateTeamEventNotes(
            Long teamEventId,
            TeamEventNotesRequestDto requestDto,
            Long rexUserId
    );

    TeamEventDetailsResponseDto getTeamEventDetails(
            Long teamEventId,
            Long rexUserId
    );

    TeamEventResponseDto createTeamEvent(
            CreateTeamEventRequestDto requestDto,
            Long rexUserId,
            Long platformId
    );

	public boolean existsByProjectNumber(Long projectNumberId);

    public void processAndReleasePastTeamEvents();

    public EditTeamEventResponseDto editTeamEvent(
            Long teamEventId,
            EditTeamEventRequestDto requestDto,
            Long rexUserId
    );

    TeamEventResponseDto closeTeamEvent(Long teamEventId, Long rexUserId);

    ListResponseDto<MySubmittedTeamEventGetListDto,MySubmittedTeamEventGetListRequestDto>
    getMySubmittedTeamEvents(
            Long rexUserId,
            TeamEventStatusEnum status,
            String eventTitle,
            String projectNumber,
            LocalDate fromDate,
            LocalDate toDate,
            int pageNo,
            int pageSize,
            TeamEventSortBy sortBy,
            String sortOrder
    );
}

