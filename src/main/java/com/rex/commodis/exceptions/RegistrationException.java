/** Custom exception for Registration error conditions; mapped to an HTTP response by GlobalExceptionHandler. */
package com.rex.commodis.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created on Ağustos, 2020
 *
 * @author Faruk
 */
@Getter
@RequiredArgsConstructor
public class RegistrationException extends RuntimeException {

	private final String errorMessage;

}
