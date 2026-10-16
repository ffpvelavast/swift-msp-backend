/** REST controller exposing TeamEvent API endpoints; all queries are scoped to the authenticated tenant via RexUserContext. */
package com.rex.commodis.controller;

import com.rex.commodis.dto.events.*;
import com.rex.commodis.repository.RexUserRepository;
import com.rex.commodis.service.TeamEventTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.rex.commodis.dto.ListResponseDto;
import com.rex.commodis.dto.TeamEventGetListDto;
import com.rex.commodis.dto.TeamEventGetListRequestDto;
import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.security.user.RexUserContext;
import com.rex.commodis.service.TeamEventService;
import com.rex.commodis.service.TeamEventStatusUpdateService;

import java.util.List;

@RestController
@RequestMapping("/team-events")
@RequiredArgsConstructor
@Tag(name = "Team Events", description = "APIs for managing team events")
public class TeamEventController {

    private final TeamEventService teamEventService;
    private final TeamEventStatusUpdateService teamEventStatusUpdateService;
    private final TeamEventTypeService teamEventTypeService;
    private final RexUserRepository rexUserRepository;


    /*** GET /team-events */
    @GetMapping
    public ResponseEntity<ListResponseDto<TeamEventGetListDto, TeamEventGetListRequestDto>> getTeamEvents(
            @ParameterObject @ModelAttribute TeamEventGetListRequestDto request,
            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {

        if ((request.getFromDate() == null) != (request.getToDate() == null)) {
            throw new BadRequestException(
                    "Provide both from & to date or neither"
            );
        }
        Long rexUserId = rexUserContext.getRexUserId();

        return ResponseEntity.ok(
                teamEventService.getTeamEvents(
                        rexUserId,
                        request.getStatus(),
                        request.getEventTitle(),
                        request.getSubmittedBy(),
                        request.getProjectNumber(),
                        request.getFromDate(),
                        request.getToDate(),
                        request.getPageNumber(),
                        request.getPageSize(),
                        request.getSortBy(),
                        request.getDirection()
                )
        );
    }

    /** POST /team-events/{teameventId}/team-event-status */
    @PostMapping("/{teameventId}/team-event-status")
    @Operation(
            summary = "Update team event status",
            description = """
                    Only APPROVED and REJECTED are accepted.
                    All other status values return 400 Bad Request.
                    
                    Internally:
                      1. Validates status is APPROVED or REJECTED
                      2. Deactivates the current active team_event_history row
                      3. Inserts a new row with active = true and the new status
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status or team event already has this status"),
            @ApiResponse(responseCode = "404", description = "Team event not found or status not configured in DB")
    })
    public ResponseEntity<TeamEventStatusUpdateResponseDto> updateTeamEventStatus(
            @Parameter(description = "ID of the team event to update", required = true)
            @PathVariable Long teameventId,
            @Valid @RequestBody TeamEventStatusUpdateRequestDto requestDto,
            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {
        Long rexUserId = rexUserContext.getRexUserId();

        TeamEventStatusUpdateResponseDto response =
                teamEventStatusUpdateService.updateTeamEventStatus(teameventId, requestDto, rexUserId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{teamEventId}/team-event-notes")
    public ResponseEntity<TeamEventNotesResponseDto> updateNotes(
            @PathVariable Long teamEventId,
            @Valid @RequestBody TeamEventNotesRequestDto teamEventNotesRequestDto,
            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {
        Long rexUserId = rexUserContext.getRexUserId();

        return ResponseEntity.ok(
                teamEventService.updateTeamEventNotes(
                        teamEventId,
                        teamEventNotesRequestDto,
                        rexUserId
                )
        );
    }

    @GetMapping("/{teamEventId}/team-event-details")
    @Operation(
            summary = "Get team event details",
            description = """
                Fetch complete team event details including:
                - Team event header
                - Receipts
                - Receipt files
                - Total amount
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<TeamEventDetailsResponseDto> getTeamEventDetails(

            @Parameter(description = "Team Event ID")
            @PathVariable Long teamEventId,

            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {

        Long rexUserId = rexUserContext.getRexUserId();

        return ResponseEntity.ok(
                teamEventService.getTeamEventDetails(
                        teamEventId,
                        rexUserId
                )
        );
    }

    @PostMapping("/create-team-event")
    @Operation(
            summary = "Create a new team event",
            description = """
                    Creates a new team event and seeds an initial SUBMITTED status history entry.

                    - platformId and rexUserId are extracted from JWT
                    - picklist_id is optional
                    - Returns generated team_event_id
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Team event created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Referenced entity not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<TeamEventResponseDto> createTeamEvent(
            @Valid @RequestBody CreateTeamEventRequestDto requestDto,
            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {


        Long rexUserId = rexUserContext.getRexUserId();
        Long platformId = rexUserContext.getPlatformId();

        TeamEventResponseDto response =
                teamEventService.createTeamEvent(
                        requestDto,
                        rexUserId,
                        platformId
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/team-event-nature")
    @Operation(
            summary = "Get Team Event Type dropdown values",
            description = "Fetches all available Team Event Types filtered by platformId from Auth. " +
                    "Returns localized display text and description based on locale for dropdown population."
    )
    public ResponseEntity<List<TeamEventTypeDropDownDto>> getTeamEventNature(
            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {
        Long platformId = rexUserContext.getPlatformId();
        Long rexUserId = rexUserContext.getRexUserId();

        return ResponseEntity.ok(teamEventTypeService.getTeamEventTypes(platformId, rexUserId));
    }

    @PostMapping("/{teamEventId}/manage-team-event")
    @Operation(
            summary = "Edit Team Event",
            description = """
                Updates team event fields partially.

                Only non-null fields are updated.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Team event updated successfully"),
            @ApiResponse(responseCode = "404", description = "Team event not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized")
    })
    public ResponseEntity<EditTeamEventResponseDto> editTeamEvent(
            @PathVariable Long teamEventId,
            @Valid @RequestBody EditTeamEventRequestDto requestDto,
            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {

        Long rexUserId = rexUserContext.getRexUserId();

        return ResponseEntity.ok(teamEventService.editTeamEvent(teamEventId,requestDto,rexUserId));
    }

    @PostMapping("/{teamEventId}/close-team-event")
    @Operation(
            summary = "Close Team Event with button",
            description = "Closes a teamEvent manually after projectLeader clicks closeEvent button in frontend"
    )
    public ResponseEntity<TeamEventResponseDto> closeTeamEvent(
            @PathVariable Long teamEventId,
            @AuthenticationPrincipal RexUserContext rexUserContext
    ){
        Long rexUserId = rexUserContext.getRexUserId();

        return ResponseEntity.ok(teamEventService.closeTeamEvent(teamEventId, rexUserId));
    }

    @GetMapping("/team-events-submitter")
    public ResponseEntity<ListResponseDto<MySubmittedTeamEventGetListDto,MySubmittedTeamEventGetListRequestDto>> getMySubmittedTeamEvents(
            @ParameterObject @ModelAttribute MySubmittedTeamEventGetListRequestDto request,
            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {
        if ((request.getFromDate() == null)
                !=
                (request.getToDate() == null)) {

            throw new BadRequestException(
                    "Provide both from & to date or neither"
            );
        }

        Long rexUserId = rexUserContext.getRexUserId();

        return ResponseEntity.ok(
                teamEventService.getMySubmittedTeamEvents(
                        rexUserId,
                        request.getStatus(),
                        request.getEventTitle(),
                        request.getProjectNumber(),
                        request.getFromDate(),
                        request.getToDate(),
                        request.getPageNumber(),
                        request.getPageSize(),
                        request.getSortBy(),
                        request.getDirection()
                )
        );
    }
}
