package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.dto.UserDTO;
import com.sapred.ordermanagerred.dto.UserNameDTO;
import com.sapred.ordermanagerred.model.Currency;
import com.sapred.ordermanagerred.model.ProductCategory;
import com.sapred.ordermanagerred.model.User;
import com.sapred.ordermanagerred.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin("http://localhost:3000")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/logIn/{email}/{password}")
    public ResponseEntity logIn(@PathVariable("email") String email, @PathVariable("password") String password) {
        log.debug("Entering logIn method with email: {}", email);
        String response = userService.logIn(email, password);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/fill")
    public void fill() {
        log.debug("Entering fill method");
        userService.fill();
    }

    @GetMapping("/fillRoles")
    public void fillRoles() {
        userService.fillRoles();
    }

    @PostMapping
    public ResponseEntity addUser(@RequestHeader("token") String token, @RequestBody UserDTO user) {
        log.debug("Entering addUser method");
       userService.addUser(token, user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestParam("fullName") String fullName, @RequestParam("companyName") String companyName,
                                 @RequestParam("currency") Currency currency, @RequestParam("email") String email,
                                 @RequestParam("password") String password) {
        log.debug("Entering signUp method with email: {}", email);
        String token = userService.signUp(fullName, companyName, currency, email, password);
        return new ResponseEntity(token, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Boolean> deleteUser(@RequestHeader("token") String token, @PathVariable("userId") String userId) {
        log.debug("Entering deleteUser method with userId: {}", userId);
        userService.deleteUser(token, userId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }


    @PutMapping("/{userId}")
    public ResponseEntity<Boolean> updateUser(@RequestHeader("token") String token, @PathVariable("userId") String userId, @RequestBody User user) {
        log.debug("Entering updateUser method with userId: {}", userId);
        userService.updateUser(token, userId, user);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("{pageNumber}")
    public ResponseEntity getUsers(@RequestHeader("token") String token, @PathVariable("pageNumber") int pageNumber) {
        log.debug("Entering getUsers method with pageNumber: {}", pageNumber);
        List<UserDTO> user = userService.getUsers(token, pageNumber);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/getNamesOfCustomersByPrefix/{prefix}")
    public List<UserNameDTO> getNamesOfCustomersByPrefix(@RequestHeader("token") String token, @PathVariable String prefix) {
        log.debug("Entering getNamesOfCustomersByPrefix method with prefix: {}", prefix);
        return userService.getNamesOfCustomersByPrefix(token, prefix);
    }
}
