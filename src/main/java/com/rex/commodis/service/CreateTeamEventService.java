/** Service interface defining CreateTeamEvent operations; implemented by CreateTeamEventServiceImpl. */
//package com.rex.commodis.service;
//
//
//import com.rex.commodis.dto.events.CreateTeamEventRequestDto;
//import com.rex.commodis.dto.events.CreateTeamEventResponseDto;
//
//public interface CreateTeamEventService {
//
//    /**
//     * Creates a new team event and seeds its initial SUBMITTED history entry.
//     *
//     * @param requestDto  validated request payload from the controller
//     * @param rexUserId   authenticated user ID extracted from JWT context
//     * @param platformId  platform ID extracted from JWT context
//     * @return            response DTO containing the generated team_event_id
//     */
//    CreateTeamEventResponseDto createTeamEvent(
//            CreateTeamEventRequestDto requestDto,
//            Long rexUserId,
//            Long platformId
//    );
//}