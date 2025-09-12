package com.adit.order_management_service.dto.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderRequestDto {
    private Long orderId;
    private String orderDesc;
    private BigDecimal totalOrderAmount;
    private Integer productId;
    private Integer userId;
    private String paymentUid;
    private Integer productQuantity;
    private String billingRefId;
    private String shippingRefId;
    private String orderStatus;
}
