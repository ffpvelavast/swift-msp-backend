/** REST controller exposing Search API endpoints; all queries are scoped to the authenticated tenant via RexUserContext. */
package com.rex.commodis.controller;

import com.rex.commodis.dto.MemberSearchResultDto;
import com.rex.commodis.repository.SearchRepository;
import com.rex.commodis.security.jwt.JwtTokenManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "Unified Search", description = "Unified search API for picking members and guests")
public class SearchController {

    private final SearchRepository searchRepository;
    private final JwtTokenManager tokenManager;

    @GetMapping("/members")
    @Operation(summary = "Search beneficiaries and guests", description = "Dynamically searches both beneficiaries and guests using exact name/partial name ranking and favorites evaluation.")
    public ResponseEntity<List<MemberSearchResultDto>> searchMembers(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "50") int limit,
            HttpServletRequest request) {

        String token = extractToken(request);
        Long platformId = tokenManager.getPlatformId(token);
        Long rexUserId = tokenManager.getRexUserid(token);

        // Convert empty string query to null so SQL function ignores filter
        if (q != null && q.trim().isEmpty()) {
            q = null;
        }

        List<MemberSearchResultDto> results = searchRepository.searchMembers(q, rexUserId, platformId, limit);
        return ResponseEntity.ok(results);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new RuntimeException("Unauthorized: Missing Bearer Token");
    }
}
