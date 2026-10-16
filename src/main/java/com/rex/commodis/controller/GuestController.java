/** REST controller exposing Guest API endpoints; all queries are scoped to the authenticated tenant via RexUserContext. */
package com.rex.commodis.controller;

import com.rex.commodis.dto.GuestCreateDto;
import com.rex.commodis.dto.GuestCreateResponseDto;
import com.rex.commodis.dto.GuestResponseDto;
import com.rex.commodis.service.GuestService;
import com.rex.commodis.security.jwt.JwtTokenManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
@Tag(name = "Guests Management", description = "APIs for managing guests")
public class GuestController {

    private final GuestService guestService;
    private final JwtTokenManager tokenManager;

    /**
     * POST /api/guests
     * Save a new external member.
     */
    @PostMapping
    @Operation(
            summary = "Create a new guest",
            description = "Creates a new guest member for the active platform. Returns the generated guest details including the unique guest id."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Guest created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GuestCreateResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request details"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Missing or invalid token")
    })
    public ResponseEntity<GuestCreateResponseDto> createGuest(@RequestBody GuestCreateDto request, HttpServletRequest httpRequest) {
        String token = extractToken(httpRequest);
        Long rexUserId = tokenManager.getRexUserid(token);
        Long platformId = tokenManager.getPlatformId(token);


        GuestCreateResponseDto response = guestService.createGuest(request, rexUserId, platformId);
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/guests
     * Fetch all guests accessible to the user.
     */
    @GetMapping
    public ResponseEntity<List<GuestResponseDto>> getGuests(
            @RequestParam(defaultValue = "en") String locale,
            HttpServletRequest httpRequest) {
        String token = extractToken(httpRequest);
        Long rexUserId = tokenManager.getRexUserid(token);
        Long platformId = tokenManager.getPlatformId(token);


        List<GuestResponseDto> response = guestService.getGuests(rexUserId, platformId, locale);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/guests/paged
     * Fetch guests with pagination support.
     */
    @GetMapping("/paged")
    public ResponseEntity<List<GuestResponseDto>> getGuestsPaged(
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "en") String locale,
            HttpServletRequest httpRequest) {
        String token = extractToken(httpRequest);
        Long rexUserId = tokenManager.getRexUserid(token);
        Long platformId = tokenManager.getPlatformId(token);

        List<GuestResponseDto> response = guestService.getGuestsPaged(rexUserId, platformId, locale, limit, offset);
        return ResponseEntity.ok(response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new RuntimeException("Unauthorized: Missing Bearer Token");
    }
}