package com.sapred.ordermanagerred.dto;

import com.sapred.ordermanagerred.model.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserDTO {
    private String id;
    private String fullName;
    private String password;
    private String email;
    private String address;
    private String phone;
    private String roleId;
}
