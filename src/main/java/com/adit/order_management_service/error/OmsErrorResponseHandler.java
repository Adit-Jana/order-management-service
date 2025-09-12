package com.adit.order_management_service.error;

import com.adit.order_management_service.exception.OrderNotFoundException;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

}
