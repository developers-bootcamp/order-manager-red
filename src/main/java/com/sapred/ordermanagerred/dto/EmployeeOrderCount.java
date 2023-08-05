package com.sapred.ordermanagerred.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeOrderCount {
    private String employeeId;
    private long orderCount;
}