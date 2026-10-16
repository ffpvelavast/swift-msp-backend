/** Service interface defining RexUser operations; implemented by RexUserServiceImpl. */
package com.rex.commodis.service;

import com.rex.commodis.dto.GetPlatformByEmailResponseDto;
import com.rex.commodis.model.RexUser;

import java.util.List;
import java.util.Optional;

public interface RexUserService {

    RexUser getById(Long userId);

    String getUserNameById(Long userId);

    Optional<RexUser> findByEmailIgnoreCase(String verifierEmail);

    Optional<RexUser> findByKeycloakId(String keycloakId);

    List<RexUser> getActiveUsersByPlatform(Long platformId);

    Long syncUserWithKeycloak(String keycloakId, String email);

    com.rex.commodis.dto.UserProfileDto getUserProfile(String email);

    Boolean checkEmailExists(String email);

    Optional<GetPlatformByEmailResponseDto> getPlatformByEmail(String email);

    String getLocale(Long rexUserId);

    void updateLocale(Long rexUserId, String locale);

}
