package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.dto.ProductCartDTO;
import com.sapred.ordermanagerred.exception.MismatchData;
import com.sapred.ordermanagerred.exception.StatusException;
import com.sapred.ordermanagerred.model.Order;
import com.sapred.ordermanagerred.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/{userId}/{status}/{pageNumber}")
    public ResponseEntity getOrders(@RequestHeader("token") String token, @PathVariable("userId") String userId, @PathVariable("status") String statusId, @PathVariable("pageNumber") int pageNumber) {
        try {
            List<Order> orders = orderService.getOrders(token, statusId, pageNumber, userId);
            return ResponseEntity.ok().body(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @PostMapping ("/{pageNumber}/{ta}")
    public ResponseEntity getOrdersWithFilter(@RequestHeader("token") String token, @PathVariable("ta") int ta, @PathVariable("pageNumber") int pageNumber) {
        try {
            List<Order> orders = orderService.getOrdersWithFilter(token, ta, pageNumber);
            return ResponseEntity.ok().body(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @PostMapping("/")
    public ResponseEntity createOrder(@RequestHeader("token") String token, @RequestBody Order order) {
        try {
            String id = orderService.createOrder(token, order);
            return ResponseEntity.ok().body(id);
        } catch (StatusException exception) {
            return new ResponseEntity(exception, HttpStatus.CONFLICT);
        } catch (MismatchData exception) {
            return new ResponseEntity(exception, HttpStatus.CONFLICT);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/fillProducts")
    public void fillProducts() {
        orderService.fillProducts();
    }

    @PostMapping("/calculateOrderAmount")
    public ResponseEntity<List<ProductCartDTO>> calculateOrderAmount(@RequestHeader("token") String token, @RequestBody Order order) {
        return new ResponseEntity<>(orderService.calculateOrderAmount(order), HttpStatus.OK);
    }

    @GetMapping("/fill")
    public void fill() {
        orderService.fill();
    }
}
