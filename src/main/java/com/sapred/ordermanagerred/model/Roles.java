package com.sapred.ordermanagerred.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.annotation.Collation;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

enum RoleOptions{ADMIN,EMPLOYEE,CUSTOMER};
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="Roles")
public class Roles {
    @Id
    private  String id;
    private RoleOptions  name;
    private  String desc;
    @DBRef
    private  AuditData auditData;
}
