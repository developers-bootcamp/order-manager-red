package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.dto.ProductCartDTO;
import com.sapred.ordermanagerred.model.Order;
import com.sapred.ordermanagerred.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@CrossOrigin("http://localhost:3000")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{userId}/{status}/{pageNumber}")
    public ResponseEntity getOrders(@RequestHeader("token") String token, @PathVariable("userId") String userId, @PathVariable("status") String statusId, @PathVariable("pageNumber") int pageNumber) {
        log.debug("Entering getOrders method. @PathVariable userId: {}, statusId: {}, pageNumber: {}", userId, statusId, pageNumber);
        List<Order> orders = orderService.getOrders(token, statusId, pageNumber, userId);
        return ResponseEntity.ok().body(orders);
    }

    @PostMapping("/")
    public ResponseEntity createOrder(@RequestHeader("token") String token, @RequestBody Order order) {
        log.debug("Entering createOrder method. @RequestBody order: {}", order);
        String id = orderService.createOrder(token, order);
        return ResponseEntity.ok().body(id);
    }

    @GetMapping("/fillProducts")
    public void fillProducts() {
        log.debug("Entering fillProducts method");
        orderService.fillProducts();
    }

    @PostMapping("/calculateOrderAmount")
    public ResponseEntity<List<ProductCartDTO>> calculateOrderAmount(@RequestHeader("token") String token, @RequestBody Order order) {
        log.debug("Entering calculateOrderAmount method. @RequestBody order: {}", order);
        List<ProductCartDTO> result = orderService.calculateOrderAmount(token,order);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
