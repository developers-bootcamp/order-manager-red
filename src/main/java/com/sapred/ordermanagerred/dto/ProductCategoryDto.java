package com.sapred.ordermanagerred.dto;

import org.springframework.data.annotation.Id;
import lombok.Data;

@Data
public class ProductCategoryDto {

    @Id
    private String id;
    private String name;
    private String desc;

}
