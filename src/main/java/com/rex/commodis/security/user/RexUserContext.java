/** Spring Security configuration defining authentication, JWT validation, and public/protected endpoint rules. */
package com.rex.commodis.security.user;

public class RexUserContext {

    private final Long rexUserId;
    private final Long platformId;
    private final String email;

    public RexUserContext(Long rexUserId, Long platformId, String email) {
        this.rexUserId = rexUserId;
        this.platformId = platformId;
        this.email = email;
    }

    public Long getRexUserId() {
        return rexUserId;
    }

    public Long getPlatformId() {
        return platformId;
    }

    public String getEmail() {
        return email;
    }
}
