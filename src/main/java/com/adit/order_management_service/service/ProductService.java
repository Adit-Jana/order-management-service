package com.adit.order_management_service.service;

import com.adit.order_management_service.dto.response.ProductResponseDto;
import com.adit.order_management_service.entity.ProductEntity;
import com.adit.order_management_service.exception.ProductNotFount;
import com.adit.order_management_service.model.request.filter.ProductFilterDto;
import com.adit.order_management_service.model.request.pagination.PaginationFilter;
import com.adit.order_management_service.model.request.product.ProductDashboardView;
import com.adit.order_management_service.model.request.product.ProductRequest;
import com.adit.order_management_service.repo.ProductRepo;
import com.adit.order_management_service.specification.ProductSpecification;
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
                .productName(productRequest.getProductName())
                .productDesc(productRequest.getProductDesc())
                .productCategory(productRequest.getProductCategory())
                .productPrice(productRequest.getProductPrice())
                .productQuantity(productRequest.getProductQuantity())
                .build();
        //save the date
        ProductEntity productEntityResponse = productRepo.save(productEntity);

        return ProductResponseDto.builder()
                .productId(productEntityResponse.getProductId())
                .productName(productEntity.getProductName())
                .productDesc(productEntity.getProductDesc())
                .productPrice(productEntity.getProductPrice())
                .productQuantity(productEntity.getProductQuantity())
                .productCategory(productEntity.getProductCategory())
                .build();
    }


    public ProductResponseDto updateProductDetails(Long productId, ProductRequest productRequest) {

        Optional<ProductEntity> productEntityResponse = productRepo.findById(productId);
        if (productEntityResponse.isPresent()) {

            productEntityResponse.get().setProductName(productRequest.getProductName());
            productEntityResponse.get().setProductDesc(productRequest.getProductDesc());
            productEntityResponse.get().setProductCategory(productRequest.getProductCategory());
            productEntityResponse.get().setProductQuantity(productRequest.getProductQuantity());
            productEntityResponse.get().setProductPrice(productRequest.getProductPrice());

            ProductEntity productEntity = productRepo.save(productEntityResponse.get());

            return ProductResponseDto.builder()
                    .productId(productEntity.getProductId())
                    .productName(productEntity.getProductName())
                    .productDesc(productEntity.getProductDesc())
                    .productCategory(productRequest.getProductCategory())
                    .productPrice(productEntity.getProductPrice())
                    .productQuantity(productEntity.getProductQuantity())
                    .build();
        } else {
            throw new ProductNotFount("Not found! Product id : " + productId);
        }
    }

    public ProductResponseDto getProductDetails(Long productId) {
        return productRepo.findById(productId)
                .map(productEntity -> ProductResponseDto.builder()
                        .productId(productEntity.getProductId())
                        .productName(productEntity.getProductName())
                        .productDesc(productEntity.getProductDesc())
                        .productCategory(productEntity.getProductCategory())
                        .productPrice(productEntity.getProductPrice())
                        .productQuantity(productEntity.getProductQuantity())
                        .build()).orElseThrow(() -> new ProductNotFount("Not Found! product id: " + productId));


    }

    public List<ProductResponseDto> getProductList(List<Long> productIdList) {

        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();
        productIdList.stream()
                .map(id -> productRepo.findById(id)
                        .orElseThrow(() -> new ProductNotFount(" product id " + id)))
                .map(pe -> productResponseDtoList.add(ProductResponseDto.builder()
                        .productId(pe.getProductId())
                        .productName(pe.getProductName())
                        .productDesc(pe.getProductDesc())
                        .productCategory(pe.getProductCategory())
                        .productPrice(pe.getProductPrice())
                        .productQuantity(pe.getProductQuantity())
                        .build())).toList();

        return productResponseDtoList;

    }

    public ProductDashboardView getDashboardView(int page, int size, String sortBy, String sortOrder,
                                                 ProductFilterDto productFilterDto) {

        // add custom specification filter
        // add sorting and dynamic filtering
        ProductSpecification productSpecification = new ProductSpecification(productFilterDto);

        // sorting
        Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder)
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductEntity> productPage = productRepo.findAll(productSpecification, pageable);

        ProductDashboardView dashboardView = new ProductDashboardView();
        // Map the product entities to ProductResponseDto
        List<ProductResponseDto> productDtos = productPage.stream()
                .map(ProductResponseDto::fromEntity)
                .toList();
        dashboardView.setProductResponseDto(productDtos);

        PaginationFilter paginationFilter = new PaginationFilter();
        paginationFilter.setPageNumber(productPage.getNumber());
        paginationFilter.setPageSize(productPage.getSize());
        paginationFilter.setTotalElements(productPage.getTotalElements());
        paginationFilter.setTotalPages(productPage.getTotalPages());
        paginationFilter.setFirst(productPage.isFirst());
        paginationFilter.setLast(productPage.isLast());
        paginationFilter.setSortBy(sortBy);
        paginationFilter.setSortOrder(sortOrder);
        dashboardView.setPaginationFilter(paginationFilter);

        dashboardView.setPaginationFilter(paginationFilter);

        return dashboardView;

    }
}
