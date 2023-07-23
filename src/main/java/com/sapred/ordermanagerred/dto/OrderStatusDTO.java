package com.sapred.ordermanagerred.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusDTO {
    private Month month;
    private int cancelled;
    private int delivered;

}

//    public Map<Integer, Integer> getCountOfOrdersInStatus;
//    @Getter
//
//    private  Month month;
//   private String orderStatusId;
//
//   @Getter
//
//    private int CountOfOrdersInStatus;
//
//
//}
//fraidi
//package com.sap.ordermanagergreen.dto;
//
//
//        import lombok.AllArgsConstructor;
//        import lombok.Data;
//        import lombok.NoArgsConstructor;
//
//        import java.time.Month;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class DeliverCancelOrdersDTO {
//    private Month month;
//    private int cancelled;
//    private int delivered;
//
//}

