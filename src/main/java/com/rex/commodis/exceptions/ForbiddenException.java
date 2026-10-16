/** Custom exception for Forbidden error conditions; mapped to an HTTP response by GlobalExceptionHandler. */
package com.rex.commodis.exceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}