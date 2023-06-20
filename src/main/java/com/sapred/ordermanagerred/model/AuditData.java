package com.sapred.ordermanagerred.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.annotation.Collation;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Collation("AuditData")
public class AuditData {
    private Date createDate;
    private Date updateData;
}
