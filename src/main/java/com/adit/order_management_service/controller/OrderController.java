package com.adit.order_management_service.controller;

import com.adit.order_management_service.dto.response.OrderResponseDto;
import com.adit.order_management_service.model.request.AdditionalInfo;
import com.adit.order_management_service.model.request.OmsOrderRequestPayload;
import com.adit.order_management_service.model.response.OrderResponse;
import com.adit.order_management_service.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/v2/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping( value = "/create_order")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Validated OmsOrderRequestPayload orderRequestPayload) {
        OrderResponseDto responseDto = orderService.processOrder(orderRequestPayload);


        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDto.getOrderId())
                .toUri();


        return ResponseEntity
                .created(location) // best practices
                .body(OrderResponse.builder()
                .orderId(String.valueOf(responseDto.getOrderId()))
                .userId(String.valueOf(responseDto.getUserId()))
                .orderResponseMessage("order has been created successfully")
                .orderStatus(responseDto.getOrderStatus())
                .totalOrderAmount(responseDto.getTotalAmount())
                .build());
    }

    @GetMapping(value = "/get_details/{id}")
    public ResponseEntity<OrderResponse> getOrderDetails(@PathVariable("id") String orderId) {
        OrderResponseDto orderResponseDto = orderService.getOrderDetailsById(orderId);
        return new ResponseEntity<>(OrderResponse.builder()
                .orderId(String.valueOf(orderResponseDto.getOrderId()))
                .orderStatus(orderResponseDto.getOrderStatus())
                .userId(String.valueOf(orderResponseDto.getUserId()))
                .totalOrderAmount(orderResponseDto.getTotalAmount())
                .additionalInfo(AdditionalInfo.builder()
                        .orderCreatedDate("")
                        .orderLastUpdatedDate("")
                        .userAuthenticated("true")
                        .build())
                .build(), HttpStatus.OK);
    }

}
