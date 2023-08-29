package com.sapred.ordermanagerred.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    @Id
    private String id;
    private String name;
    private String desc;
    private int inventory;
    private double discount;
    private String discountType;
    private String productCategoryName;
    private double price;
}
