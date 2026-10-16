/** Spring Security configuration defining authentication, JWT validation, and public/protected endpoint rules. */
package com.rex.commodis.security.jwt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Component
public class KeycloakPublicKeyCache {

    @Value("${keycloak.jwks-url}")
    private String keycloakJwksUrl;

    // Key ID (kid) → RSAPublicKey
    private final Map<String, RSAPublicKey> publicKeyCache = new ConcurrentHashMap<>();

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper jsonMapper = new ObjectMapper();

    // ---------------------------------------------------------------
    // Public API
    // ---------------------------------------------------------------


    public RSAPublicKey resolvePublicKey(String keyId) {

        // check in-memory cache first
        RSAPublicKey cachedKey = publicKeyCache.get(keyId);
        if (cachedKey != null) {
            log.debug("Public key cache HIT for keyId={}", keyId);
            return cachedKey;
        }

        // cache miss → fetch fresh keys from Keycloak
        log.info("Public key cache MISS for keyId={}, refreshing JWKS from Keycloak", keyId);
        fetchAndRebuildCache();

        // retry lookup after cache refresh
        RSAPublicKey refreshedKey = publicKeyCache.get(keyId);
        if (refreshedKey != null) {
            log.debug("Public key resolved for keyId={} after JWKS refresh", keyId);
        } else {
            log.warn("Public key not found for keyId={} even after JWKS refresh — token will be rejected", keyId);
        }

        return refreshedKey; // null → caller rejects the token
    }

    // ---------------------------------------------------------------
    // Internal helpers
    // ---------------------------------------------------------------


    private void fetchAndRebuildCache() {
        try {
            HttpRequest jwksRequest = HttpRequest.newBuilder()
                    .uri(URI.create(keycloakJwksUrl))
                    .GET()
                    .build();

            HttpResponse<String> jwksResponse =
                    httpClient.send(jwksRequest, HttpResponse.BodyHandlers.ofString());

            if (jwksResponse.statusCode() != 200) {
                log.error("JWKS endpoint returned unexpected HTTP status={}", jwksResponse.statusCode());
                return;
            }

            JsonNode responseRoot = jsonMapper.readTree(jwksResponse.body());
            JsonNode jwkArray    = responseRoot.get("keys");

            if (jwkArray == null || !jwkArray.isArray()) {
                log.error("JWKS response does not contain a valid 'keys' array");
                return;
            }

            Map<String, RSAPublicKey> freshKeyCache = new ConcurrentHashMap<>();

            for (JsonNode jwkNode : jwkArray) {

                // Skip non-signing keys (e.g. RSA-OAEP encryption keys)
                String keyUsage = jwkNode.path("use").asText();
                if (!"sig".equals(keyUsage)) continue;

                String keyId        = jwkNode.path("kid").asText();
                String modulusB64   = jwkNode.path("n").asText();
                String exponentB64  = jwkNode.path("e").asText();

                RSAPublicKey rsaPublicKey = buildRsaPublicKey(modulusB64, exponentB64);
                if (rsaPublicKey != null) {
                    freshKeyCache.put(keyId, rsaPublicKey);
                    log.debug("RSA public key cached for keyId={}", keyId);
                }
            }

            // Atomically replace old cache with fresh keys
            publicKeyCache.clear();
            publicKeyCache.putAll(freshKeyCache);
            log.info("JWKS cache successfully rebuilt — {} RSA signing key(s) loaded", freshKeyCache.size());

        } catch (Exception ex) {
            log.error("Failed to fetch and rebuild JWKS cache: {}", ex.getMessage(), ex);
        }
    }


    private RSAPublicKey buildRsaPublicKey(String modulusBase64, String exponentBase64) {
        try {
            Base64.Decoder base64UrlDecoder = Base64.getUrlDecoder();

            BigInteger modulus  = new BigInteger(1, base64UrlDecoder.decode(modulusBase64));
            BigInteger exponent = new BigInteger(1, base64UrlDecoder.decode(exponentBase64));

            RSAPublicKeySpec rsaKeySpec  = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory       rsaFactory  = KeyFactory.getInstance("RSA");

            return (RSAPublicKey) rsaFactory.generatePublic(rsaKeySpec);

        } catch (Exception ex) {
            log.error("Failed to construct RSAPublicKey from JWKS entry: {}", ex.getMessage());
            return null;
        }
    }
}