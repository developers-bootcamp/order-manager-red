package com.sapred.ordermanagerred.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.annotation.Collation;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Product")
public class Product {
    @Id
    private String id;
    private String name;
    private String desc;
    private double price;
    private double discount;
    private DiscountType discountType;
    @DBRef
    private ProductCategory productCategoryId;
    private int inventory;
    @DBRef
    private Company companyId;
    private AuditData auditData;
}
