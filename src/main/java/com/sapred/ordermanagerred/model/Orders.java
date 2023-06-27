package com.sapred.ordermanagerred.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.annotation.Collation;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="Orders")
public class Orders {
    @Id
    private  String id;
    private String employee;
    private String customer;
    private int totalAmount;
    @DBRef
    private List<OrderItems> orderItemsList;
    private String  orderStatusId;
    private Company companyId;
    private int creditCardNumber;
    private Date ExpiryOn;
    private int cvc;
    private boolean notificationFlag;
    private  AuditData auditData;

    public Orders(String s, String employee, String customer, int i,Company companyId, AuditData d,String orderStatusId) {
        id=s;
        this.employee=employee;
        this.customer=customer;
        this.totalAmount=i;
        this.auditData=d;
        this.companyId=companyId;
        this.orderStatusId=orderStatusId;
    }
}
