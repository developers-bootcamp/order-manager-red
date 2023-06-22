package com.sapred.ordermanagerred.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.annotation.Collation;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

enum DiscountType{PERCENTAGE,FIXED,AMOUNT};
@Data
@AllArgsConstructor
@Document(collection="Product")
public class Product {
    @Id
    private  String id;
    private  String name;
    private  String desc;
    private  double price;
    private  double discount;
    private  DiscountType discountType;
    private  ProductCategory productCategoryId;
    private  int inventory;
    private  Company companyId;
    @DBRef
    private  AuditData data;
}
