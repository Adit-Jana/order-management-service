package com.adit.order_management_service.service;

import com.adit.order_management_service.dto.request.OrderRequestDto;
import com.adit.order_management_service.mapper.OrderMapper;
import com.adit.order_management_service.model.request.OmsOrderRequestPayload;
import com.adit.order_management_service.repo.OrderRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderMapper orderMapper;

    public void createOrderInDB(OmsOrderRequestPayload orderRequestPayload) {

       OrderRequestDto orderRequestDto = orderMapper.toOrderRequestDto(orderRequestPayload);

       log.info("order id {} and order desc {}", orderRequestDto.getOrderId(), orderRequestDto.getOrderDesc());


        //Order orderResponse = orderRepo.save(order);
        //log.info("Created your order, reference order id {}", orderResponse.getOrder_id());
    }
}
