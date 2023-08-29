package com.sapred.ordermanagerred.dto;

import com.sapred.ordermanagerred.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class DynamicGraph {
    private Object obj;
    private int count;
}
