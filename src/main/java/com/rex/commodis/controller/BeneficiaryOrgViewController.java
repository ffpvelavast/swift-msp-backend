/** REST controller exposing BeneficiaryOrgView API endpoints; all queries are scoped to the authenticated tenant via RexUserContext. */
package com.rex.commodis.controller;

import com.rex.commodis.model.BeneficiaryOrgRow;
import com.rex.commodis.model.GuestRow;
import com.rex.commodis.repository.BeneficiaryOrgViewRepository;
import com.rex.commodis.security.jwt.JwtTokenManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/views/beneficiary-orgdata")
@RequiredArgsConstructor
public class BeneficiaryOrgViewController {

    private final BeneficiaryOrgViewRepository repository;
    private final JwtTokenManager tokenManager;

    /**
     * GET /api/views/beneficiary-orgdata
     * Primary internal list.
     */
    @GetMapping
    public ResponseEntity<List<BeneficiaryOrgRow>> getInternal(HttpServletRequest request) {
        String token = extractToken(request);
        Long platformId = tokenManager.getPlatformId(token);
        Long rexUserId = tokenManager.getRexUserid(token);
        return ResponseEntity.ok(repository.findInternal(platformId, rexUserId));
    }

    /**
     * GET /api/views/beneficiary-orgdata/paged
     * Restored for backward compatibility, now using secure platform context.
     */
    @GetMapping("/paged")
    public ResponseEntity<List<BeneficiaryOrgRow>> getPaged(
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "0") int offset,
            HttpServletRequest request) {
        String token = extractToken(request);
        Long platformId = tokenManager.getPlatformId(token);
        Long rexUserId = tokenManager.getRexUserid(token);
        // We use the paged repository method to restrict the result set
        return ResponseEntity.ok(repository.findPaged(platformId, rexUserId, limit, offset));
    }

    /**
     * GET /api/views/beneficiary-orgdata/by-platform/{platformId}
     * Restored but secured: You can only request YOUR OWN platform ID.
     */
    @GetMapping("/by-platform/{platformId}")
    public ResponseEntity<List<BeneficiaryOrgRow>> getByPlatform(@PathVariable Long platformId,
                                                                 HttpServletRequest request) {
        String token = extractToken(request);
        Long tokenPlatformId = tokenManager.getPlatformId(token);
        Long rexUserId = tokenManager.getRexUserid(token);

        if (!platformId.equals(tokenPlatformId)) {
            throw new RuntimeException("Access Denied: You cannot view data from another platform.");
        }

        return ResponseEntity.ok(repository.findInternal(platformId, rexUserId));
    }

    @GetMapping("/by-beneficiary/{beneficiaryId}")
    public ResponseEntity<List<BeneficiaryOrgRow>> getByBeneficiary(@PathVariable Long beneficiaryId,
                                                                    HttpServletRequest request) {
        String token = extractToken(request);
        Long platformId = tokenManager.getPlatformId(token);
        Long rexUserId = tokenManager.getRexUserid(token);
        return ResponseEntity.ok(repository.findByBeneficiaryId(beneficiaryId, platformId, rexUserId));
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new RuntimeException("Unauthorized: Missing Bearer Token");
    }
}
