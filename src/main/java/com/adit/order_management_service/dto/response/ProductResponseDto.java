package com.adit.order_management_service.dto.response;

import com.adit.order_management_service.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Long productId;
    private String productName;
    private String productDesc;
    private Double productPrice;
    private Integer productQuantity;
    private String productCategory;

    public static ProductResponseDto fromEntity(ProductEntity productEntity) {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.productId = productEntity.getProductId();
        productResponseDto.productName = productEntity.getProductName();
        productResponseDto.productDesc = productEntity.getProductDesc();
        productResponseDto.productPrice = productEntity.getProductPrice();
        productResponseDto.productCategory = productEntity.getProductCategory();
        productResponseDto.productQuantity = productEntity.getProductQuantity();
        return productResponseDto;
    }
}
