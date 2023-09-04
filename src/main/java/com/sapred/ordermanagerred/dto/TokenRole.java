package com.sapred.ordermanagerred.dto;

import com.sapred.ordermanagerred.model.RoleOptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class TokenRole {
    private String token;
    private RoleOptions role;
}
