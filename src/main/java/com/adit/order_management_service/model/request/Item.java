package com.adit.order_management_service.model.request;

import com.adit.order_management_service.model.request.product.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {
    private Product product;
    private Integer quantity;
}
