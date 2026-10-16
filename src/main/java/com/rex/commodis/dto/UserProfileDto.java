/** Data transfer object carrying UserProfile data between the API layer and the service/client. */
package com.rex.commodis.dto;

import lombok.*;
import java.util.List;

/**
 * High-level DTO representing the full user profile including roles and rights.
 * This matches the JSONB structure returned by rexbase.fn_login_user_profile.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private UserInfoDto user;
    private List<UserRoleDto> roles;
    private List<UserRightDto> rights;
    private List<Long> favorites;


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfoDto {
        private Long id;
        private String firstname;
        private String lastname;
        private String fullName;
        private String email;
        private String academictitle;
        private java.util.UUID keycloakId;
        private Long platformId;
        private String locale;
        private Boolean active;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRoleDto {
        private String role;
        private String displayText;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRightDto {
        private String right;
        private String displayText;
    }
}