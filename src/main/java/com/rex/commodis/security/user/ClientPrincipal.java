/** Spring Security configuration defining authentication, JWT validation, and public/protected endpoint rules. */
package com.rex.commodis.security.user;

public class ClientPrincipal {


    private final Long platformId;

    public ClientPrincipal( Long platformId) {

        this.platformId = platformId;
    }


    public Long getPlatformId() {
        return platformId;
    }
}