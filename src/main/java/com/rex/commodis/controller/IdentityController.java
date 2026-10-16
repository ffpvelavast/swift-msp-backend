/** REST controller exposing Identity API endpoints; all queries are scoped to the authenticated tenant via RexUserContext. */
package com.rex.commodis.controller;

import com.rex.commodis.dto.LocaleRequestDto;
import com.rex.commodis.dto.UserProfileDto;
import com.rex.commodis.security.user.RexUserContext;
import com.rex.commodis.service.RexUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/identity")
@RequiredArgsConstructor
public class IdentityController {

    private final RexUserService rexUserService;

    /**
     * GET /api/identity/profile
     * Returns the full profile of the currently authenticated user.
     */
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getMyProfile(@AuthenticationPrincipal RexUserContext user) {
        UserProfileDto profile = rexUserService.getUserProfile(user.getEmail());
        return ResponseEntity.ok(profile);
    }

    /**
     * PATCH /api/identity/locale
     * Updates the preferred locale for the currently authenticated user.
     * Request body: {"locale": "en"}
     */
    @PatchMapping("/locale")
    public ResponseEntity<Void> updateLocale(
            @AuthenticationPrincipal RexUserContext user,
            @Valid @RequestBody LocaleRequestDto request) {
        rexUserService.updateLocale(user.getRexUserId(), request.getLocale());
        return ResponseEntity.noContent().build();
    }
}
