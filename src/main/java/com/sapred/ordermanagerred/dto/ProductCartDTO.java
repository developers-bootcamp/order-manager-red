package com.sapred.ordermanagerred.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ProductCartDTO {
    private String name;
    private double amount;
    private double discount;
    private int quantity;
}