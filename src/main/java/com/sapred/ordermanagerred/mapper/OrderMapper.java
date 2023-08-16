package com.sapred.ordermanagerred.mapper;

import com.sapred.ordermanagerred.dto.OrderDTO;
import com.sapred.ordermanagerred.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE= Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "customerId.id",target = "customerId")
    @Mapping(source = "customerId.address",target = "address")
    @Mapping(source = "customerId.address.email",target = "email")
    @Mapping(source = "customerId.address.phone",target = "phone")
    @Mapping(source = "orderStatus",target = "statusOptions")
    OrderDTO orderToDTO (Order o);

    @Mapping(source = "customerId",target = "customerId.id")
    @Mapping(source = "address",target = "customerId.address")
    @Mapping(source = "email",target = "customerId.address.email")
    @Mapping(source = "phone",target = "customerId.address.phone")
    @Mapping(source = "statusOptions",target = "orderStatus")
    Order DTOToOrder (OrderDTO o);


}
