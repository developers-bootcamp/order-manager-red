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
@Document(collection = "Order")
public class Order {
    @Id
    private String id;
    @DBRef
    private User employeeId;
    @DBRef
    private User customerId;
    private int totalAmount;
    private List<OrderItem> orderItemsList;
    private StatusOptions orderStatus;
    @DBRef
    private Company companyId;
    private int creditCardNumber;
    private Date ExpireOn;
    private int cvc;
    private boolean notificationFlag;
    private AuditData auditData;
//    public Order(String s, String employee, String customer, int i,Company companyId, AuditData d,String orderStatusId) {
//        id=s;
//        this.employee=employee;
//        this.customer=customer;
//        this.totalAmount=i;
//        this.auditData=d;
//        this.companyId=companyId;
//        this.orderStatusId=orderStatusId;
//    }
}