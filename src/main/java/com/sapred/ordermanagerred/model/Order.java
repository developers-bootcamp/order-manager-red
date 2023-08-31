package com.sapred.ordermanagerred.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
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
@FieldNameConstants
public class Order {

    @Id
    private String id;
    @DBRef
    private User employeeId;
    @DBRef
    private User customerId;
    private double totalAmount;
    private List<OrderItem> orderItemsList;
    private OrderStatus orderStatus;
    @DBRef
    private Company companyId;
    private Currency currency;
    private String creditCardNumber;
    private String expireOn;
    private int cvc;
    private boolean notificationFlag;
    private AuditData auditData;
}
