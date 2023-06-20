package com.sapred.ordermanagerred.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.annotation.Collation;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Collation("Address")
public class Address {
    private  int telephone;
    private String address;
    private  String email;
}
