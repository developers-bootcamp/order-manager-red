package com.sapred.ordermanagerred.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.annotation.Collation;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Document(collection = "Order")
public class Order {
    @Id
    private String id;
    private String employee;
    private String customer;
    private int totalAmount;
    private List<OrderItem> orderItemsList;
    private String orderStatusId;
    @DBRef
    private Company companyId;
    private int creditCardNumber;
    private LocalDate ExpiryOn;
    private int cvc;
    private boolean notificationFlag;
    private AuditData auditData;

    public Order(String s, String employee, String customer, int i,Company companyId, AuditData d,String orderStatusId) {
        id=s;
        this.employee=employee;
        this.customer=customer;
        this.totalAmount=i;
        this.auditData=d;
        this.companyId=companyId;
        this.orderStatusId=orderStatusId;
    }
}
