package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.dto.UserDTO;
import com.sapred.ordermanagerred.model.User;
import com.sapred.ordermanagerred.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/User")
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

    @PostMapping
    public ResponseEntity addUser(@RequestHeader("token") String token, @RequestBody User user) {
        try {
            User newUser = userService.addUser(token, user);
            return ResponseEntity.ok().body(newUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("1");
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("2");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("4");
        }
    }

    @GetMapping("{pageNumber}")
    public ResponseEntity getUsers(@RequestHeader("token") String token,@PathVariable("pageNumber") int pageNumber) {
        try {
            List<UserDTO> user = userService.getUsers(token,pageNumber);
            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
