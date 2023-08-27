package com.sapred.ordermanagerred.dto;

import com.sapred.ordermanagerred.model.Order.StatusOptions;
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
    private String id;
    private  String customerId;
    private double totalAmount;
    private String address;
    private String email;
    private String phone;
    private OrderStatus orderStatus;
    private int creditCardNumber;
    private LocalDateTime expireOn;
    private int cvc;
}
