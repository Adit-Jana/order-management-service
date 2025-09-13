package com.adit.order_management_service.controller;

import com.adit.order_management_service.dto.response.ProductResponseDto;
import com.adit.order_management_service.model.request.ProductRequest;
import com.adit.order_management_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    
}
