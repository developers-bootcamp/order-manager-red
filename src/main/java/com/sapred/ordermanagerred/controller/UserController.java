package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.dto.UserDTO;
import com.sapred.ordermanagerred.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/User")
@CrossOrigin("http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/logIn/{email}/{password}")
    public ResponseEntity<String> logIn(@PathVariable("email") String email, @PathVariable("password") String password) {
        return userService.logIn(email, password);
    }

    // זה פונקציה שמכניסה נתונים רק בשביל לבדוק שההתחברות עובדת
    @GetMapping("/fill")
    public void fill() {
        userService.fill();
    }

    @DeleteMapping("/deleteCustomer/{customerId}")
    public ResponseEntity<Boolean> deleteCustomer(@RequestHeader("token") String token, @PathVariable("customerId") String customerId) {
        return userService.deleteCustomer(token, customerId);
    }

    @PutMapping("/editCustomer")
    public ResponseEntity<Boolean> editCustomer(@RequestHeader("token") String token, @RequestBody UserDTO customer) {
        return userService.editCustomer(token, customer);
    }

    @GetMapping("/getAllNames")
    public List<String> getAllNames(@RequestHeader("token") String token) {
        return userService.getAllNames(token);
    }
}
