package com.sapred.ordermanagerred.dto;

import com.sapred.ordermanagerred.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
@Data
@AllArgsConstructor
public class TopEmployeeDTO {
    @DBRef
    private User user;
    private String CountOrders;
}
