package com.adit.order_management_service.dto.response;

import com.adit.order_management_service.model.request.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class OrderResponseDto {
    private Long orderId;
    private Long userId;
    private OrderStatus orderStatus;
    private BigDecimal totalAmount;
}
