package com.adit.order_management_service.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CouponCode {
    private String couponCode;
}
