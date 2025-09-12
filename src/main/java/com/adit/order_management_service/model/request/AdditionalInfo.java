package com.adit.order_management_service.model.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AdditionalInfo {
    private LocalDateTime orderCreatedDate;
    private LocalDateTime orderLastUpdatedDate;
    private String userAuthenticated;
}
