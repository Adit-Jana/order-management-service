package com.adit.order_management_service.mapper;

import com.adit.order_management_service.dto.request.OrderRequestDto;
import com.adit.order_management_service.model.request.OmsOrderRequestPayload;
import com.adit.order_management_service.model.request.OrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "orderId", source = "orderRequest.orderId")
    @Mapping(target = "orderDesc", source = "orderRequest.orderDesc")
    OrderRequestDto toOrderRequestDto(OmsOrderRequestPayload omsOrderRequestPayload);
}
