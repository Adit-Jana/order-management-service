package com.adit.order_management_service.error;

import com.adit.order_management_service.exception.OrderNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class OmsErrorResponseHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleError(OrderNotFoundException ex) {
        LocalDateTime localDateTime = LocalDateTime.now();

        return new ResponseEntity<ErrorResponse>(ErrorResponse.builder()
                .errorMessage(ex.getMessage())
                .errorCode("100")
                .timestamp(localDateTime.toString())
                .build(), HttpStatus.OK);
    }

    // useful for if we want to have consistent json error mapping
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthenticationException ex,
                                                             HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                "401",
                request.getServletPath()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

}
