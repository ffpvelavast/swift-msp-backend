/** Service implementation for RexUser business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import java.util.List;
import java.util.Optional;

import com.rex.commodis.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rex.commodis.dto.GetPlatformByEmailResponseDto;
import com.rex.commodis.dto.UserProfileDto;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.exceptions.UserNotProvisionedException;
import com.rex.commodis.model.Platform;
import com.rex.commodis.model.RexUser;
import com.rex.commodis.repository.RexUserRepository;
import com.rex.commodis.repository.PlatformRepository;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class RexUserServiceImpl implements RexUserService {

    private final RexUserRepository rexUserRepository;
    private final PlatformRepository platformRepository;


    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    @Override
    @Transactional
    public RexUser getById(Long rexUserId) {

        return rexUserRepository.findById(rexUserId)
                .orElseThrow(() ->
                        new NotFoundException( "Rex user not found: " + rexUserId)
                );
    }

    @Override
    @Transactional
    public String getUserNameById(Long userId) {

        return getById(userId).getListname();
    }

    @Override
    @Transactional
    public Optional<RexUser> findByEmailIgnoreCase(String verifierEmail){

        return rexUserRepository.findByEmailIgnoreCaseAndActiveTrue(verifierEmail);
    }


    @Override
    @Transactional
    public Optional<RexUser> findByKeycloakId(String keycloakId) {
        try {
            java.util.UUID keycloakUuid = java.util.UUID.fromString(keycloakId);
            return rexUserRepository.findByKeycloakId(keycloakUuid);
        } catch (IllegalArgumentException ex) {
            log.warn("Invalid keycloakId format, cannot parse as UUID: {}", keycloakId);
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public List<RexUser> getActiveUsersByPlatform(Long platformId) {
        return rexUserRepository.findByPlatformIdAndActiveTrue(platformId);
    }

    @Override
    @Transactional
    public UserProfileDto getUserProfile(String email) {
        String json = rexUserRepository.getUserProfileJson(email);
        if (json == null) {
            throw new NotFoundException("Profile not found for email: " + email);
        }

        try {
            return objectMapper.readValue(json, UserProfileDto.class);
        } catch (Exception e) {
            log.error("Error parsing user profile JSON for email: {}", email, e);
            throw new RuntimeException("Error loading user profile", e);
        }
    }

    @Override
    @Transactional
    public Long syncUserWithKeycloak(String keycloakId, String email) {
        if (keycloakId == null || keycloakId.isBlank()) {
            return null;
        }

        // 1. Primary Identity Lookup (Trust the Keycloak ID)
        Optional<RexUser> userByKeycloak = findByKeycloakId(keycloakId);
        if (userByKeycloak.isPresent()) {
            return userByKeycloak.get().getId();
        }

        // 2. Controlled JIT Linking (Map existing email records to Keycloak)
        if (email != null && !email.isBlank()) {
            Optional<RexUser> userByEmail = findByEmailIgnoreCase(email);
            if (userByEmail.isPresent()) {
                RexUser user = userByEmail.get();

                // Safety: Only link if NOT already linked to another ID
                if (user.getKeycloakId() == null) {
                    log.info("JIT Linking: Mapping existing user '{}' to new Keycloak ID '{}'", email, keycloakId);
                    user.setKeycloakId(java.util.UUID.fromString(keycloakId));
                    return rexUserRepository.save(user).getId();
                } else {
                    log.error("Security Conflict: Identity mismatch for email '{}'. Token ID: {}, DB ID: {}",
                            email, keycloakId, user.getKeycloakId());
                    return user.getId();
                }
            }
        }

        log.warn("Access denied: no provisioned user for Keycloak ID '{}' or email '{}'", keycloakId, email);
   
//        throw new UserNotProvisionedException(
//                "Your account is not provisioned in REX. Contact your administrator."
//        );
        
        return null;
    }

    @Override
    @Transactional
    public Boolean checkEmailExists(String email) {
        return rexUserRepository.findByEmailIgnoreCaseAndActiveTrue(email).isPresent();
    }

    @Override
    @Transactional
    public Optional<GetPlatformByEmailResponseDto> getPlatformByEmail(String email) {
        Optional<RexUser> rexUserOpt = rexUserRepository.findByEmailIgnoreCaseAndActiveTrue(email);
        if (rexUserOpt.isEmpty()) {
            return Optional.empty();
        }

        RexUser rexUser = rexUserOpt.get();
        Optional<Platform> platformOpt = platformRepository.findById(rexUser.getPlatformId());

        if (platformOpt.isEmpty()) {
            return Optional.empty();
        }

        Platform platform = platformOpt.get();
        return Optional.of(GetPlatformByEmailResponseDto.builder()
                .platformId(platform.getId())
                .displayName(platform.getDisplayName())
                .build());
    }

        @Override
        public String getLocale(Long rexUserId) {
            return rexUserRepository.findById(rexUserId)
                    .orElseThrow(() -> new NotFoundException("Rex user not found: " + rexUserId))
                    .getLocale();
        }

        @Override
        @Transactional
        public void updateLocale(Long rexUserId, String locale) {
            RexUser user = rexUserRepository.findById(rexUserId)
                    .orElseThrow(() -> new NotFoundException("Rex user not found: " + rexUserId));
            user.setLocale(locale.toLowerCase());
            rexUserRepository.save(user);
            log.info("Updated locale to '{}' for user id {}", locale, rexUserId);
        }

}
