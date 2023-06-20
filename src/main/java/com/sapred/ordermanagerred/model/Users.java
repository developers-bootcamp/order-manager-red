package com.sapred.ordermanagerred.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.annotation.Collation;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="Users")
public class Users {
    @Id
    private String id;
    private String fullName;
    private String password;
    @DBRef
    private  Address address ;
    private Roles roleId;
    private Company companyId;
    @DBRef
    private  AuditData auditData;
}
