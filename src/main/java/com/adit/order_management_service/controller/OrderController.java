package com.adit.order_management_service.controller;

import com.adit.order_management_service.dto.response.OrderResponseDto;
import com.adit.order_management_service.model.request.OmsOrderRequestPayload;
import com.adit.order_management_service.model.response.OrderResponse;
import com.adit.order_management_service.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v2/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping( value = "/create_order")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Validated OmsOrderRequestPayload orderRequestPayload) {
        OrderResponseDto responseDto = orderService.processOrder(orderRequestPayload);
        ResponseEntity<OrderResponse> orderResponseResponseEntity =
                new ResponseEntity<OrderResponse>(OrderResponse.builder()
                        .orderId(String.valueOf(responseDto.getOrderId()))
                        .userId(String.valueOf(responseDto.getUserId()))
                        .orderResponseMessage("order has been created successfully")
                        .orderStatus(responseDto.getOrderStatus())
                        .totalOrderAmount(responseDto.getTotalAmount())
                        .build(), HttpStatus.OK);


        //log.info("Order has been created");
        return orderResponseResponseEntity;
    }
}
