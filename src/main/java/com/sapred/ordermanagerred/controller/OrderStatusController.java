package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.dto.OrderStatusDTO;
import com.sapred.ordermanagerred.model.Order;
import com.sapred.ordermanagerred.service.OrdersStatusService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Month;
import java.util.List;
import java.util.Map;

@Controller
@RestController
@RequestMapping("/cancelDeliver")
 public class OrderStatusController {
    @Autowired
    private  OrdersStatusService ordersStatusService;

    @GetMapping("/getStatus")
    public ResponseEntity<Map<Month, Map<Integer,Integer>>> getStatus(@RequestHeader("token") String token) {
        int monthAmount = 5;
            try{
                return ResponseEntity.ok(ordersStatusService.getStatus(token, monthAmount));
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    @GetMapping("/getAll")
    public Map<Month, Map<Integer, Integer>> getAll() {
      return ordersStatusService.getAll();
    }

}
