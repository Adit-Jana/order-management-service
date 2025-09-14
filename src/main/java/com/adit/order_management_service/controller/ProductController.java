package com.adit.order_management_service.controller;

import com.adit.order_management_service.dto.response.ProductResponseDto;
import com.adit.order_management_service.model.request.ProductDashboardView;
import com.adit.order_management_service.model.request.ProductRequest;
import com.adit.order_management_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/product")
public class ProductController {
    // add
    //delete
    // update
    // edit

    @Autowired
    private ProductService productService;

    @PostMapping(value = "/create")
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequest productRequest) {
        ProductResponseDto productResponseDto =
                productService.createProductDetails(productRequest);
        return new ResponseEntity<>(productResponseDto, HttpStatusCode.valueOf(200));
    }

    @GetMapping(value = "/get_details")
    public ResponseEntity<ProductResponseDto> getProductDetails(
            @RequestParam (value = "productId") Long productId) {
        ProductResponseDto productResponseDto = productService.getProductDetails(productId);
        return new ResponseEntity<>(productResponseDto, HttpStatusCode.valueOf(200));
    }

    @GetMapping(value = "/get_product_list")
    public ResponseEntity<List<ProductResponseDto>> getProductList(
            @RequestParam (value = "productIds") List<Long> productIds) {
        List<ProductResponseDto> productResponseDtoList = productService.getProductList(productIds);
        return new ResponseEntity<>(productResponseDtoList, HttpStatusCode.valueOf(200));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ProductDashboardView> getAllTheProductsView(
            @RequestParam(defaultValue = "0", value = "page") int page,
            @RequestParam(defaultValue = "5", value = "size") int size) {
        ProductDashboardView productResponsePaginationView =
               productService.getDashboardView(page, size);
        return ResponseEntity.ok(productResponsePaginationView);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<ProductResponseDto> updatedProductDetails(@RequestParam (value = "productId") Long productId,
                                                                    @RequestBody ProductRequest productRequest) {
        ProductResponseDto productResponseDto  =
                productService.updateProductDetails(productId, productRequest);
        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
    }

    
}
