package com.sapred.ordermanagerred.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.annotation.Collation;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "User")
@SuperBuilder(toBuilder = true)
public class User {
    @Id
    private String id;
    private String fullName;
    private String password;
    private Address address;
    @DBRef
    private Role roleId;
    @DBRef
    private Company companyId;
    private AuditData auditData;
}
