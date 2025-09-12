package com.adit.order_management_service.error;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private String errorMessage;
    private String errorCode;
    private String timestamp;

}
