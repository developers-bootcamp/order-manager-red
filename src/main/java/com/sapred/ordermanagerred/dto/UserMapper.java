package com.sapred.ordermanagerred.dto;

import com.sapred.ordermanagerred.model.Address;
import com.sapred.ordermanagerred.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "address.phone", target = "phone")
    @Mapping(source = "address.email", target = "email")
//    @Mapping(source = "address.addresss", target = "addresss")
    UserDTO userToDTO(User u);

    @Mapping(source = "phone", target = "address.phone")
    @Mapping(source = "email", target = "address.email")
//    @Mapping(source = "addresss", target = "address.addresss")
    User DTOToUser(UserDTO u);

    List<UserDTO> userToDTO(List<User> list);
    List<User> DTOToUser(List<UserDTO> list);
}