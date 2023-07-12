package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.dto.ProductCartDTO;
import com.sapred.ordermanagerred.model.Currency;
import com.sapred.ordermanagerred.model.Order;
import com.sapred.ordermanagerred.model.User;
import com.sapred.ordermanagerred.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
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



    @GetMapping("/fillProducts")
    public void fillProducts() {
        orderService.fillProducts();
    }

    @PostMapping("/calculateOrderAmount")
    public ResponseEntity<List<ProductCartDTO>> calculateOrderAmount(@RequestHeader("token") String token, @RequestBody Order order) {
        return new ResponseEntity<>(orderService.calculateOrderAmount(order), HttpStatus.OK);
    }

    @GetMapping("/getCurrencies")
    public ResponseEntity<List<Currency>> getCurrencies(@RequestHeader("token") String token) {
        return new ResponseEntity<>(orderService.getCurrencies(), HttpStatus.OK);
    }
}
