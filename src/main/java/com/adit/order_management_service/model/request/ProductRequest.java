package com.adit.order_management_service.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRequest {
    private String productName;
    private String productDesc;
    private String productCategory;
    private Double productPrice;
    private Integer productQuantity;
}
