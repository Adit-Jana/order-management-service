package com.adit.order_management_service.model.response;

import com.adit.order_management_service.model.request.AdditionalInfo;
import com.adit.order_management_service.model.request.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class OrderResponse {
    private String orderId;
    private String userId;
    private OrderStatus orderStatus;
    private BigDecimal totalOrderAmount;
    private String orderResponseMessage;
    private AdditionalInfo additionalInfo;
}
