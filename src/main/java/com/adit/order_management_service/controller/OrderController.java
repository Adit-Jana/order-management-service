package com.adit.order_management_service.controller;

import com.adit.order_management_service.model.request.OmsOrderRequestPayload;
import com.adit.order_management_service.model.request.OrderRequest;
import com.adit.order_management_service.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public void createOrder(@RequestBody OmsOrderRequestPayload orderRequestPayload) {
        orderService.createOrderInDB(orderRequestPayload);
        ResponseEntity<String> httpResponse = new ResponseEntity<String>(HttpStatus.OK);
        log.info("Order has been created");
    }
}
