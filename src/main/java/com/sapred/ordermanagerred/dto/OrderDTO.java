package com.sapred.ordermanagerred.dto;

import com.sapred.ordermanagerred.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    public enum  PaymentType{
        Credit,
        Debit,

    }
    private String id;
    private String customerId;
    private double totalAmount;
    private OrderStatus orderStatus;
    private PaymentType paymentType;
    private int creditCardNumber;
    private String expireOn;
    private int cvc;
}
