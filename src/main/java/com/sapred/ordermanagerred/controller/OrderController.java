package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.model.Order;
import com.sapred.ordermanagerred.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Order")
public class OrderController
{
    @Autowired
    private OrderService orderService;

    //the params should be pathparams?...
    @GetMapping("/getOrders/{userId}/{status}/{pageNumber}")
    public List<Order> getOrders(@RequestHeader("token") String token ,@PathVariable("userId") String userId, @PathVariable("status") String statusId, @PathVariable("pageNumber") int pageNumber) {
        return orderService.getOrders(token,statusId,pageNumber,userId);
    }
    // it is a function just to fill data
    @GetMapping("/fill")
    public void fill() {
        orderService.fill();
    }
}
