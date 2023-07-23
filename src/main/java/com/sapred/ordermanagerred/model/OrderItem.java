package com.sapred.ordermanagerred.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @DBRef
    private Product productId;
    private double amount;
    private int quantity;
}
