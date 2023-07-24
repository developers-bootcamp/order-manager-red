package com.sapred.ordermanagerred.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Month;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MonthProductCountDto {
    private Month month;
    private List<ProductCountDto> products;
}
