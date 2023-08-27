package com.sapred.ordermanagerred.dto;

import com.sapred.ordermanagerred.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ProductCountDto {
    private Product productId;
    private int totalQuantity;
    @Override
    public String toString() {
        return "ProductCountDto{" +
                "productId='" + productId + '\'' +
                ", totalQuantity=" + totalQuantity +
                '}';
    }
}
