package com.sapred.ordermanagerred.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class ProductCountDto {
    private String productName;
    private int count;
}
