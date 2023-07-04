package com.sapred.ordermanagerred.dto;
import com.sapred.ordermanagerred.model.Address;
import lombok.Data;
@Data
public class UserDTO {
    private String fullName;
    private String email;
    private Address address;
    private String phone;
}
