package com.adit.order_management_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductResponseDto {
    private Long productId;
    private String productName;
    private String productDesc;
    private Double productPrice;
    private Integer productQuantity;
    private String productCategory;
}
