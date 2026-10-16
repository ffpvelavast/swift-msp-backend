/** Custom exception for GlobalExceptionHandler error conditions; mapped to an HTTP response by GlobalExceptionHandler. */
package com.rex.commodis.exceptions.advice;

import java.time.LocalDateTime;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rex.commodis.exceptions.ApiExceptionResponse;
import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.exceptions.ConflictException;
import com.rex.commodis.exceptions.ForbiddenException;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.exceptions.UserNotProvisionedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiExceptionResponse> handleAllExceptions(Exception ex) {
    	
    	log.error("Unhandled exception occurred", ex);
    	 
        return ResponseEntity.status(500)
                .body(new ApiExceptionResponse(
                        ex.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiExceptionResponse> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiExceptionResponse(
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND,
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(UserNotProvisionedException.class)
    public ResponseEntity<ApiExceptionResponse> handleUserNotProvisionedException(UserNotProvisionedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ApiExceptionResponse(
                        ex.getMessage(),
                        HttpStatus.FORBIDDEN,
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiExceptionResponse> handleForbiddenException(ForbiddenException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ApiExceptionResponse(
                        ex.getMessage(),
                        HttpStatus.FORBIDDEN,
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiExceptionResponse> handleConflictException(ConflictException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiExceptionResponse(
                        ex.getMessage(),
                        HttpStatus.CONFLICT,
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiExceptionResponse> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiExceptionResponse(
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST,
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiExceptionResponse> handleUnreadableMessage(
            HttpMessageNotReadableException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiExceptionResponse(
                        "Malformed request body. Ensure status is a valid string.",
                        HttpStatus.BAD_REQUEST,
                        LocalDateTime.now()
                ));
    }
}