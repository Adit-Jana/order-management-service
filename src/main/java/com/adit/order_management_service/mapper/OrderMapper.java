package com.adit.order_management_service.mapper;

import com.adit.order_management_service.dto.request.OrderRequestDto;
import com.adit.order_management_service.model.request.order.OmsOrderRequestPayload;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "orderId", source = "orderRequest.orderId")
    @Mapping(target = "orderDesc", source = "orderRequest.orderDesc")
    OrderRequestDto toOrderRequestDto(OmsOrderRequestPayload omsOrderRequestPayload);
}
