package com.sapred.ordermanagerred.dto;

import com.sapred.ordermanagerred.model.Order.StatusOptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
private StatusOptions statusOptions;
private int creditCardNumber;
private Date ExpireOn;
private int cvc;
}
