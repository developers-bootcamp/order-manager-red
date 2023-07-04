package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.model.Order;
import com.sapred.ordermanagerred.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    //the params should be pathparams?...
    @GetMapping("/getOrders/{userId}/{status}/{pageNumber}")
    public ResponseEntity getOrders(@RequestHeader("token") String token, @PathVariable("userId") String userId, @PathVariable("status") String statusId, @PathVariable("pageNumber") int pageNumber) {
        try {
            List<Order> orders = orderService.getOrders(token, statusId, pageNumber, userId);
            return ResponseEntity.ok().body(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // it is a function just to fill data
    @GetMapping("/fill")
    public void fill() {
        orderService.fill();
    }

    //Deliver/Cancel Orders api
    @GetMapping("/getStatus")
//    public ResponseEntity<Map<String,Map<Integer,Integer>>> getStatus() {
    public ResponseEntity getStatus() {
        try {
           Map<String, Map<Integer, Integer>> ordersMap = orderService.getStatus();
            return ResponseEntity.ok().body(ordersMap);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
