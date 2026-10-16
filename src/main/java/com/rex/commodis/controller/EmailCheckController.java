/** REST controller exposing EmailCheck API endpoints; all queries are scoped to the authenticated tenant via RexUserContext. */
package com.rex.commodis.controller;

import com.rex.commodis.dto.EmailCheckRequestDto;
import com.rex.commodis.dto.EmailCheckResponseDto;
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
@RequestMapping("/api/email-check")
@RequiredArgsConstructor
@Tag(name = "Email Verification", description = "APIs for email verification")
public class EmailCheckController {

    private final RexUserService rexUserService;

    @PostMapping
    @Operation(
            summary = "Check if email exists",
            description = "Checks whether the given email address exists in the system. Returns true if an active user with this email is found, false otherwise. This endpoint is public and requires no authentication."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Email existence check completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmailCheckResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request - email is required and must be a valid email format"
            )
    })
    public ResponseEntity<EmailCheckResponseDto> checkEmailExists(
            @Valid @RequestBody EmailCheckRequestDto request
    ) {
        Boolean exists = rexUserService.checkEmailExists(request.getEmail());

        return ResponseEntity.ok(
                EmailCheckResponseDto.builder()
                        .exists(exists)
                        .build()
        );
    }
}