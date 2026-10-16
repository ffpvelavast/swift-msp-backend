/** REST controller exposing LoginControllerAdvice API endpoints; all queries are scoped to the authenticated tenant via RexUserContext. */
package com.rex.commodis.exceptions.advice;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rex.commodis.exceptions.ApiExceptionResponse;

import java.time.LocalDateTime;

/**
 * Created on Ağustos, 2020
 *
 * @author Faruk
 */
@RestControllerAdvice
public class LoginControllerAdvice {

	@ExceptionHandler(BadCredentialsException.class)
	ResponseEntity<ApiExceptionResponse> handleRegistrationException(BadCredentialsException exception) {

		final ApiExceptionResponse response = new ApiExceptionResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED, LocalDateTime.now());

		return ResponseEntity.status(response.getStatus()).body(response);
	}

}
