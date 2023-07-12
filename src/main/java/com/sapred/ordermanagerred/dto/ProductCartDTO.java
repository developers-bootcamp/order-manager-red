package com.sapred.ordermanagerred.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductCartDTO {
    private String name;
    private double amount;
    private double discount;
    private int quantity;
}
