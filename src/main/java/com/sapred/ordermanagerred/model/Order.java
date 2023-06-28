package com.sapred.ordermanagerred.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private Date ExpiryOn;
    private int cvc;
    private boolean notificationFlag;
    private AuditData auditData;
}