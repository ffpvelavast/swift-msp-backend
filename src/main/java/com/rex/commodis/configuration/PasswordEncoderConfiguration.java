/** Spring configuration class for PasswordEncoderConfiguration; registers beans or properties used across the application. */
package com.rex.commodis.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created on March, 2023
 *
 * @author Faruk
 */

@Configuration
public class PasswordEncoderConfiguration {

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

}
