package com.sapred.ordermanagerred.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
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

        DONE,

        CREATED
        //\\
    }

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
    private LocalDateTime ExpireOn;
    private int cvc;
    private boolean notificationFlag;
    private AuditData auditData;

}
