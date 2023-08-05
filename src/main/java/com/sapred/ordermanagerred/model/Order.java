package com.sapred.ordermanagerred.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Document(collection = "Order")
public class Order {

    public enum StatusOptions {
        NEW,
        APPROVED,
        CANCELLED,
        CHARGING,
        PACKING,
        DELIVERED,

    }

    @Id
    private String id;
    @DBRef
    private User employeeId;
    @DBRef
    private User customerId;
    private double totalAmount;
    private List<OrderItem> orderItemsList;
    private StatusOptions orderStatus;
    @DBRef
    private Company companyId;
    private int creditCardNumber;
    private Date ExpireOn;
    private int cvc;
    private boolean notificationFlag;
    private AuditData auditData;

}
