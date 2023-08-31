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
//    @Mapping(source = "orderStatus",target = "statusOptions")
    OrderDTO orderToDTO (Order o);

    @Mapping(source = "customerId",target = "customerId.id")
//    @Mapping(source = "statusOptions",target = "orderStatus")
    Order DTOToOrder (OrderDTO o);


}