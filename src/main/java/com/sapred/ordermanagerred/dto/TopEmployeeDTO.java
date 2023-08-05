package com.sapred.ordermanagerred.dto;

import com.sapred.ordermanagerred.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopEmployeeDTO {
    private User employee;
    private Integer countOfDeliveredOrders;
}
