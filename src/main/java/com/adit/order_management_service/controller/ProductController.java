package com.adit.order_management_service.controller;

import com.adit.order_management_service.dto.response.ProductResponseDto;
import com.adit.order_management_service.model.request.filter.ProductFilterDto;
import com.adit.order_management_service.model.request.product.ProductDashboardView;
import com.adit.order_management_service.model.request.product.ProductRequest;
import com.adit.order_management_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/create")
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequest productRequest) {
        ProductResponseDto productResponseDto =
                productService.createProductDetails(productRequest);
        return new ResponseEntity<>(productResponseDto, HttpStatusCode.valueOf(200));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/get_details")
    public ResponseEntity<ProductResponseDto> getProductDetails(
            @RequestParam(value = "productId") Long productId) {
        ProductResponseDto productResponseDto = productService.getProductDetails(productId);
        return new ResponseEntity<>(productResponseDto, HttpStatusCode.valueOf(200));
    }

    @GetMapping(value = "/get_product_list")
    public ResponseEntity<List<ProductResponseDto>> getProductList(
            @RequestParam(value = "productIds") List<Long> productIds) {
        List<ProductResponseDto> productResponseDtoList = productService.getProductList(productIds);
        return new ResponseEntity<>(productResponseDtoList, HttpStatusCode.valueOf(200));
    }

    @GetMapping("/public/dashboard")
    public ResponseEntity<ProductDashboardView> getAllTheProductsView(
            @RequestParam(defaultValue = "0", value = "page", required = false) int page,
            @RequestParam(defaultValue = "5", value = "size", required = false) int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestBody(required = false) ProductFilterDto productFilterDto) {
        ProductDashboardView productResponsePaginationView =
                productService.getDashboardView(page, size, sortBy, sortOrder, productFilterDto);
        return ResponseEntity.ok(productResponsePaginationView);
    }

    @PreAuthorize("hasRole('ADMIN')") // admin role added
    @GetMapping("/admin/dashboard")
    public ResponseEntity<ProductDashboardView> getAllTheProductsAdminView(
            @RequestParam(defaultValue = "0", value = "page", required = false) int page,
            @RequestParam(defaultValue = "5", value = "size", required = false) int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestBody(required = false) ProductFilterDto productFilterDto) {

        ProductDashboardView productResponsePaginationView =
                productService.getDashboardView(page, size, sortBy, sortOrder, productFilterDto);
        return ResponseEntity.ok(productResponsePaginationView);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/update")
    public ResponseEntity<ProductResponseDto> updatedProductDetails(@RequestParam(value = "productId") Long productId,
                                                                    @RequestBody ProductRequest productRequest) {
        ProductResponseDto productResponseDto =
                productService.updateProductDetails(productId, productRequest);
        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
    }


}
