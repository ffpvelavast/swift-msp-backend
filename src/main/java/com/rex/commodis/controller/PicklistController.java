/** REST controller exposing Picklist API endpoints; all queries are scoped to the authenticated tenant via RexUserContext. */
package com.rex.commodis.controller;

import com.rex.commodis.dto.FavoriteBatchDto;
import com.rex.commodis.dto.PicklistCreateDto;
import com.rex.commodis.dto.PicklistDto;
import com.rex.commodis.dto.PicklistMemberDto;
import com.rex.commodis.dto.PicklistCreateResponseDto;
import com.rex.commodis.dto.PicklistSyncMembersRequestDto;
import com.rex.commodis.dto.PicklistSyncMembersResponseDto;
import com.rex.commodis.security.user.RexUserContext;
import com.rex.commodis.service.PicklistService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Picklist APIs
 */
@RestController
@RequestMapping("/api/picklists")
@RequiredArgsConstructor
public class PicklistController {

    private final PicklistService picklistService;
    private final com.rex.commodis.security.jwt.JwtTokenManager tokenManager;

    /**
     * List all picklists for the current user.
     */
    @GetMapping
    public ResponseEntity<List<PicklistDto>> getUserPicklists(jakarta.servlet.http.HttpServletRequest request) {
        String token = extractToken(request);
        Long rexUserId = tokenManager.getRexUserid(token);

        List<PicklistDto> picklists = picklistService.getUserPicklists(rexUserId);
        return ResponseEntity.ok(picklists);
    }

    /**
     * Get members of a picklist
     */
    @GetMapping("/{picklistId}/members")
    public ResponseEntity<List<PicklistMemberDto>> getPicklistMembers(
            @PathVariable Long picklistId, jakarta.servlet.http.HttpServletRequest request) {
        String token = extractToken(request);
        Long rexUserId = tokenManager.getRexUserid(token);
        List<PicklistMemberDto> members = picklistService.getPicklistMembers(picklistId, rexUserId);
        return ResponseEntity.ok(members);
    }

    /**
     * Create a new picklist with members.
     */
    @PostMapping
    @Operation(
            summary = "Create a new picklist with members",
            description = """
                    Creates a new picklist for the current authenticated user and assigns
                    specified beneficiaries and guests as members.
                    
                    - rexUserId is extracted from JWT
                    - Returns generated picklist details including picklist_id
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Picklist created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<PicklistCreateResponseDto> createPicklist(@RequestBody PicklistCreateDto request,
                                                                     jakarta.servlet.http.HttpServletRequest httpRequest) {
        String token = extractToken(httpRequest);
        Long rexUserId = tokenManager.getRexUserid(token);

        PicklistCreateResponseDto response = picklistService.createPicklist(request, rexUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update all favorites at once using a batch operation.
     */
    @PostMapping("/favorites/batch")
    public ResponseEntity<Void> batchUpdateFavorites(@RequestBody FavoriteBatchDto request,
                                                     jakarta.servlet.http.HttpServletRequest httpRequest) {
        String token = extractToken(httpRequest);
        Long rexUserId = tokenManager.getRexUserid(token);

        picklistService.batchUpdateFavorites(request.getBeneficiaryIds(), rexUserId);
        return ResponseEntity.ok().build();
    }

    /**
     * Replace all members of a picklist in one shot (wipe + reinsert).
     */
    @PostMapping("/{picklistId}/update-members")
    public ResponseEntity<PicklistSyncMembersResponseDto> syncPicklistMembers(
            @PathVariable Long picklistId,
            @RequestBody PicklistSyncMembersRequestDto request,
            @AuthenticationPrincipal RexUserContext rexUserContext) {
        Long rexUserId = rexUserContext.getRexUserId();
        return ResponseEntity.ok(picklistService.syncMembers(picklistId, request, rexUserId));
    }

    /**
     * Delete a picklist by ID.
     */
    @DeleteMapping("/{picklistId}")
    public ResponseEntity<Void> deletePicklist(
            @PathVariable Long picklistId,
            @AuthenticationPrincipal RexUserContext rexUserContext) {
        Long rexUserId = rexUserContext.getRexUserId();
        picklistService.deletePicklistById(picklistId, rexUserId);
        return ResponseEntity.ok().build();
    }

    private String extractToken(jakarta.servlet.http.HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new RuntimeException("Unauthorized: Missing or invalid token");
    }
}
