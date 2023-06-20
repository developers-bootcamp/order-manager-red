package com.sapred.ordermanagerred.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.annotation.Collation;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Collation("OrderItems")
public class OrderItems {
    private Product productId;
    private double amount;
    private int quantity;
}
