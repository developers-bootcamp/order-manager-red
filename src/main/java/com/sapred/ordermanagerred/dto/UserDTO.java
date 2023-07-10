package com.sapred.ordermanagerred.dto;

import com.sapred.ordermanagerred.model.Address;
import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String fullName;
    private String email;
    private String address;
    private String phone;
    private String roleId;
}
