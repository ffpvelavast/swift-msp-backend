/** REST controller exposing GetPlatformByEmail API endpoints; all queries are scoped to the authenticated tenant via RexUserContext. */
package com.rex.commodis.controller;

import com.rex.commodis.dto.GetPlatformByEmailRequestDto;
import com.rex.commodis.dto.GetPlatformByEmailResponseDto;
import com.rex.commodis.service.RexUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/get-platform-by-email")
@RequiredArgsConstructor
@Tag(name = "Platform Information", description = "APIs for retrieving platform information")
public class GetPlatformByEmailController {

    private final RexUserService rexUserService;

    @PostMapping
    @Operation(
            summary = "Get platform information by email",
            description = "Retrieves platform ID and display name for the given email address. Returns platform information if an active user with this email exists, null otherwise. This endpoint is public and requires no authentication."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Platform information retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetPlatformByEmailResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request - email is required and must be a valid email format"
            )
    })
    public ResponseEntity<GetPlatformByEmailResponseDto> getPlatformByEmail(
            @Valid @RequestBody GetPlatformByEmailRequestDto request
    ) {
        return rexUserService.getPlatformByEmail(request.getEmail())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(null));
    }
}