package com.sapred.ordermanagerred.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MonthProductCountDto {
    private int month;
    @Field("productCount")
    private List<ProductCountDto> products;
    @Override
    public String toString() {
        return "MonthProductCountDto{" +
                "month='" + month + '\'' +
                ", products=" + products +
                '}';
    }

}
