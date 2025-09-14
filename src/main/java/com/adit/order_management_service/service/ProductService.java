package com.adit.order_management_service.service;

import com.adit.order_management_service.dto.response.ProductResponseDto;
import com.adit.order_management_service.entity.ProductEntity;
import com.adit.order_management_service.exception.ProductNotFount;
import com.adit.order_management_service.model.request.ProductDashboardView;
import com.adit.order_management_service.model.request.ProductRequest;
import com.adit.order_management_service.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        return ProductResponseDto.builder()
                .productId(productEntityResponse.getProduct_id())
                .productName(productEntity.getProduct_name())
                .productDesc(productEntity.getProduct_desc())
                .productPrice(productEntity.getProduct_price())
                .productQuantity(productEntity.getProduct_quantity())
                .productCategory(productEntity.getProduct_category())
                .build();
    }


    public ProductResponseDto updateProductDetails(Long productId, ProductRequest productRequest) {

        Optional<ProductEntity> productEntityResponse = productRepo.findById(productId);
        if (productEntityResponse.isPresent()) {

            productEntityResponse.get().setProduct_name(productRequest.getProductName());
            productEntityResponse.get().setProduct_desc(productRequest.getProductDesc());
            productEntityResponse.get().setProduct_category(productRequest.getProductCategory());
            productEntityResponse.get().setProduct_quantity(productRequest.getProductQuantity());
            productEntityResponse.get().setProduct_price(productRequest.getProductPrice());

            ProductEntity productEntity = productRepo.save(productEntityResponse.get());

            return ProductResponseDto.builder()
                    .productId(productEntity.getProduct_id())
                    .productName(productEntity.getProduct_name())
                    .productDesc(productEntity.getProduct_desc())
                    .productCategory(productRequest.getProductCategory())
                    .productPrice(productEntity.getProduct_price())
                    .productQuantity(productEntity.getProduct_quantity())
                    .build();
        } else {
            throw new ProductNotFount("Not found! Product id : " + productId);
        }
    }

    public ProductResponseDto getProductDetails(Long productId) {
        return productRepo.findById(productId)
                .map(productEntity -> ProductResponseDto.builder()
                        .productId(productEntity.getProduct_id())
                        .productName(productEntity.getProduct_name())
                        .productDesc(productEntity.getProduct_desc())
                        .productCategory(productEntity.getProduct_category())
                        .productPrice(productEntity.getProduct_price())
                        .productQuantity(productEntity.getProduct_quantity())
                        .build()).orElseThrow(() -> new ProductNotFount("Not Found! product id: " + productId));


    }

    public List<ProductResponseDto> getProductList(List<Long> productIdList) {

        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();
        productIdList.stream()
                .map(id -> productRepo.findById(id)
                        .orElseThrow(() -> new ProductNotFount(" product id " + id)))
                .map(pe -> productResponseDtoList.add(ProductResponseDto.builder()
                        .productId(pe.getProduct_id())
                        .productName(pe.getProduct_name())
                        .productDesc(pe.getProduct_desc())
                        .productCategory(pe.getProduct_category())
                        .productPrice(pe.getProduct_price())
                        .productQuantity(pe.getProduct_quantity())
                        .build())).toList();

        return productResponseDtoList;

    }

    public ProductDashboardView getDashboardView(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductEntity> productEntities = productRepo.findAll(pageable);
        return new ProductDashboardView(productEntities.map(ProductResponseDto::fromEntity));
    }
}
