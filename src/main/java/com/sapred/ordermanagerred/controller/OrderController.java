package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.model.Orders;
import com.sapred.ordermanagerred.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Order")
public class OrderController
{
    @Autowired
    private OrderService orderService;
    //the params should be pathparams?...
    @GetMapping("/allOrders/{userId}/{status}/{pageNumber}")
    public List<Orders> getOrders(@PathVariable("userId") String userId, @PathVariable("status") String statusId, @PathVariable("pageNumber") int pageNumber) {
        String companyId="11";//should be by the user id?
        return orderService.getOrders(companyId,statusId,pageNumber);
    }
    // זה פונקציה שמכניסה נתונים רק בשביל לבדוק שההתחברות עובדת
    @GetMapping("/fill")
    public void fill() {
        orderService.fill();
    }
}
