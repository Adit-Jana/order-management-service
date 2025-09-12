package com.adit.order_management_service.model.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Product {
    private String productId;
    private String productName;
    private String productDesc;
    private String productCategory;
    private Double productPrice;
}
