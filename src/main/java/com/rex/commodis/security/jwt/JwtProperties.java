/** Externalized configuration properties for Jwt; bound from application.yml via @ConfigurationProperties. */
package com.rex.commodis.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created on October, 2022
 *
 * @author Faruk
 */

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

	private String issuer;



	private long expirationMinute;



}
