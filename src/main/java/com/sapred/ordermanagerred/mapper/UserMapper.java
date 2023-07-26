package com.sapred.ordermanagerred.mapper;

import com.sapred.ordermanagerred.dto.UserDTO;
import com.sapred.ordermanagerred.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "roleId.id", target = "roleId")
    @Mapping(source = "address.phone", target = "phone")
    @Mapping(source = "address.email", target = "email")
    @Mapping(source = "address.name", target = "address")
    UserDTO userToDTO(User u);

    @Mapping(source = "roleId", target = "roleId.id")
    @Mapping(source = "phone", target = "address.phone")
    @Mapping(source = "email", target = "address.email")
    @Mapping(source = "address", target = "address.name")
    User DTOToUser(UserDTO u);

    List<UserDTO> userToDTO(List<User> list);

    List<User> DTOToUser(List<UserDTO> list);
}