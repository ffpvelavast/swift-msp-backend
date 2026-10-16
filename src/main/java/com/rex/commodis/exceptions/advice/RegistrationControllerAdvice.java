/** REST controller exposing RegistrationControllerAdvice API endpoints; all queries are scoped to the authenticated tenant via RexUserContext. */
package com.rex.commodis.exceptions.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rex.commodis.controller.RegistrationController;
import com.rex.commodis.exceptions.ApiExceptionResponse;
import com.rex.commodis.exceptions.RegistrationException;

import java.time.LocalDateTime;

/**
 * Created on Ağustos, 2020
 *
 * @author Faruk
 */
@RestControllerAdvice(basePackageClasses = RegistrationController.class)
public class RegistrationControllerAdvice {

	@ExceptionHandler(RegistrationException.class)
	ResponseEntity<ApiExceptionResponse> handleRegistrationException(RegistrationException exception) {

		final ApiExceptionResponse response = new ApiExceptionResponse(exception.getErrorMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());

		return ResponseEntity.status(response.getStatus()).body(response);
	}

}
