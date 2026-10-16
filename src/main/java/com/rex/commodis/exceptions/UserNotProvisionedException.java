/** Custom exception for UserNotProvisioned error conditions; mapped to an HTTP response by GlobalExceptionHandler. */
package com.rex.commodis.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserNotProvisionedException extends RuntimeException {

    public UserNotProvisionedException(String message) {
        super(message);
    }
}
