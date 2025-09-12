package com.adit.order_management_service.model.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AdditionalInfo {
    private String orderCreatedDate;
    private String  orderLastUpdatedDate;
    private String userAuthenticated;
}
