package com.sapred.ordermanagerred.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.annotation.Collation;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Collation("ProductCategory")
public class ProductCategory {
    @Id
    private  String id;
    private  String name;
    private  String desc;
    private  Company companyId;
    @DBRef
    private  AuditData auditData;
}
