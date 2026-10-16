/** Spring Security configuration defining authentication, JWT validation, and public/protected endpoint rules. */
package com.rex.commodis.security.jwt;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.rex.commodis.model.RexUser;
import com.rex.commodis.service.PlatformConfigurationService;
import com.rex.commodis.service.RexUserService;

import lombok.RequiredArgsConstructor;


/**
 * Created on Ağustos, 2020
 *
 * @author Faruk
 */

@Component
@RequiredArgsConstructor
public class JwtTokenManager {

    private final KeycloakPublicKeyCache KeycloakPublicKeyCache;
    private final PlatformConfigurationService platformConfigurationService;
    private final RexUserService rexUserService;

    // =========================================================
    // 🔹 TOKEN TYPE
    // =========================================================

    public boolean isUserToken(String token) {
        return getRexUserKeycloakId(token) != null;
    }

    public boolean isClientToken(String token) {
        return !isUserToken(token);
    }

    // =========================================================
    // 🔹 CLAIM EXTRACTION
    // =========================================================

    public String getRexUserKeycloakId(String token) {
        return verifyAndDecodeToken(token).getSubject();
    }

    public String getEmail(String token) {
        return getClaim(token, "email");
    }


    // public List<String> getRoles(String token) {
    //     try {
    //         Map<String, Object> realmAccess =
    //                 decode(token).getClaim("realm_access").asMap();
    //         return (List<String>) realmAccess.get("roles");
    //     } catch (Exception e) {
    //         return List.of();
    //     }
    // }

    public String extractRealm(String token) {
        String issuer = verifyAndDecodeToken(token).getIssuer();
        if (issuer == null || !issuer.contains("/realms/")) {
            throw new RuntimeException("Invalid issuer");
        }
        return issuer.substring(issuer.lastIndexOf("/") + 1);
    }

    // =========================================================
    // 🔹 VALIDATION
    // =========================================================

    public boolean validateToken(String token) {
        try {
            verifyAndDecodeToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isExpired(String token) {
        Date expiry = verifyAndDecodeToken(token).getExpiresAt();
        return expiry == null || expiry.before(new Date());
    }

    // =========================================================
    // 🔹 INTERNAL HELPERS
    // =========================================================

    private String getClaim(String token, String claimName) {
        try {
            return verifyAndDecodeToken(token).getClaim(claimName).asString();
        } catch (Exception ex) {
            return null;
        }
    }

    private DecodedJWT verifyAndDecodeToken(String rawToken) {

        // Extract kid from token header without signature verification
        DecodedJWT unverifiedJwt = JWT.decode(rawToken);
        String keyId = unverifiedJwt.getKeyId();

        if (keyId == null || keyId.isBlank()) {
            throw new JWTVerificationException("JWT header is missing required 'kid' claim");
        }

        // Resolve RSA public key from cache or fetch from Keycloak on cache miss
        RSAPublicKey rsaPublicKey = KeycloakPublicKeyCache.resolvePublicKey(keyId);
        if (rsaPublicKey == null) {
            throw new JWTVerificationException("No RSA public key found for kid: " + keyId);
        }

        // Perform cryptographic signature verification using resolved public key
        Algorithm rsaAlgorithm = Algorithm.RSA256(rsaPublicKey, null);
        JWTVerifier jwtVerifier = JWT.require(rsaAlgorithm).build();
        return jwtVerifier.verify(rawToken);
    }

	public Long getPlatformId(String token) {
		
	       DecodedJWT decodedJwt = verifyAndDecodeToken(token);

	        String issuer = decodedJwt.getIssuer();
	        if (issuer == null || !issuer.contains("/realms/")) {
	            throw new JWTVerificationException("Invalid issuer — cannot extract realm");
	        }
	        String realm = issuer.substring(issuer.lastIndexOf("/") + 1);


		Long platformId = platformConfigurationService.getPlatformIdByRealm(realm);
		
		return platformId;
	}

	public Long getRexUserid(String token) {
        DecodedJWT decodedJwt = verifyAndDecodeToken(token);

        String keycloakId = decodedJwt.getSubject();
        String email = getEmail(token);
		
        // Use professional JIT sync to identify/link the user automatically
        return rexUserService.syncUserWithKeycloak(keycloakId, email);
	}


}