package com.adit.order_management_service.service;

import com.adit.order_management_service.dto.response.ProductResponseDto;
import com.adit.order_management_service.entity.ProductEntity;
import com.adit.order_management_service.model.request.ProductRequest;
import com.adit.order_management_service.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public ProductResponseDto createProductDetails(ProductRequest productRequest) {
        ProductEntity productEntity = ProductEntity.builder()
                .product_name(productRequest.getProductName())
                .product_desc(productRequest.getProductDesc())
                .product_category(productRequest.getProductCategory())
                .product_price(productRequest.getProductPrice())
                .product_quantity(productRequest.getProductQuantity())
                .build();
        //save the date
        ProductEntity productEntityResponse = productRepo.save(productEntity);

        ProductResponseDto productResponseDto = ProductResponseDto.builder()
                .productId(productEntityResponse.getProduct_id())
                .productName(productEntity.getProduct_name())
                .productDesc(productEntity.getProduct_desc())
                .productPrice(productEntity.getProduct_price())
                .productQuantity(productEntity.getProduct_quantity())
                .productCategory(productEntity.getProduct_category())
                .build();

        return productResponseDto;
    }
}
