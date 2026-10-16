/** REST controller exposing BeneficiaryOrgViewControllerAdvice API endpoints; all queries are scoped to the authenticated tenant via RexUserContext. */
package com.rex.commodis.exceptions.advice;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.rex.commodis.controller.BeneficiaryOrgViewController;
import com.rex.commodis.exceptions.ApiExceptionResponse;
import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.exceptions.ConflictException;
import com.rex.commodis.exceptions.ForbiddenException;
import com.rex.commodis.exceptions.NotFoundException;

import jakarta.validation.ConstraintViolationException;


@Order(2)
@RestControllerAdvice(basePackageClasses = BeneficiaryOrgViewController.class)
public class BeneficiaryOrgViewControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ApiExceptionResponse> handleNotFoundException(NotFoundException exception) {
        final ApiExceptionResponse response = new ApiExceptionResponse(
                exception.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(ConflictException.class)
    ResponseEntity<ApiExceptionResponse> handleConflictException(ConflictException exception) {
        final ApiExceptionResponse response = new ApiExceptionResponse(
                exception.getMessage(),
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    ResponseEntity<ApiExceptionResponse> handleBadRequestException(BadRequestException exception) {

        final ApiExceptionResponse response = new ApiExceptionResponse(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );

        return ResponseEntity.status(response.getStatus()).body(response);
    }
    
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ApiExceptionResponse> handleForbiddenException(ForbiddenException ex) {

	    ApiExceptionResponse response = new ApiExceptionResponse();
	    response.setMessage(ex.getMessage());
	    response.setStatus(HttpStatus.FORBIDDEN);
	    response.setTime(LocalDateTime.now());

	    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ApiExceptionResponse> handleMethodArgumentTypeMismatchException(
	        MethodArgumentTypeMismatchException ex) {

	    ApiExceptionResponse response = new ApiExceptionResponse();

	    String message;

	    if (ex.getRequiredType() == LocalDate.class) {
	        message = String.format(
	                "Invalid value '%s' for parameter '%s'. Expected format is YYYY-MM-DD.",
	                ex.getValue(),
	                ex.getName()
	        );
	    } else {
	        message = String.format(
	                "Invalid value '%s' for parameter '%s'.",
	                ex.getValue(),
	                ex.getName()
	        );
	    }

	    response.setMessage(message);
	    response.setStatus(HttpStatus.BAD_REQUEST);
	    response.setTime(LocalDateTime.now());

	    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiExceptionResponse> handleValidationException(
	        MethodArgumentNotValidException ex) {

	    String errorMessage = ex.getBindingResult()
	            .getFieldErrors()
	            .stream()
	            .map(err -> err.getField() + ": " + err.getDefaultMessage())
	            .findFirst()
	            .orElse("Validation failed");

	    ApiExceptionResponse response = new ApiExceptionResponse(
	            errorMessage,
	            HttpStatus.BAD_REQUEST,
	            LocalDateTime.now()
	    );

	    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiExceptionResponse> handleHttpMessageNotReadable(
	        HttpMessageNotReadableException ex) {

	    ApiExceptionResponse response = new ApiExceptionResponse(
	            "Malformed JSON request or invalid field format",
	            HttpStatus.BAD_REQUEST,
	            LocalDateTime.now()
	    );

	    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiExceptionResponse> handleConstraintViolation(
	        ConstraintViolationException ex) {

	    ApiExceptionResponse response = new ApiExceptionResponse(
	            ex.getMessage(),
	            HttpStatus.BAD_REQUEST,
	            LocalDateTime.now()
	    );

	    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ApiExceptionResponse> handleMissingParams(
	        MissingServletRequestParameterException ex) {

	    String message = String.format(
	            "Missing required parameter '%s' of type '%s'",
	            ex.getParameterName(),
	            ex.getParameterType()
	    );

	    ApiExceptionResponse response = new ApiExceptionResponse();
	    response.setMessage(message);
	    response.setStatus(HttpStatus.BAD_REQUEST);
	    response.setTime(LocalDateTime.now());

	    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiExceptionResponse> handleAccessDenied(
			AccessDeniedException ex) {

		ApiExceptionResponse response = new ApiExceptionResponse(
				ex.getMessage(),
				HttpStatus.FORBIDDEN,
				LocalDateTime.now()
		);

		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}
}
