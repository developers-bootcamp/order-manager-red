package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.dto.UserDTO;
import com.sapred.ordermanagerred.model.Product;
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
    public ResponseEntity addUser(@RequestBody User user) {
        try {
            User newUser = userService.addUser(user);
            return ResponseEntity.ok().body(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("{pageNumber}")
    public ResponseEntity getUsers(@PathVariable("pageNumber") int pageNumber) {
        try {
           List<UserDTO> user= userService.getUsers(pageNumber);
            return ResponseEntity.ok().body(user);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


//    @GetMapping
//    public ResponseEntity getAll() {
//        try {
//            List<User>users=userService.getAllUser()
//            return ResponseEntity.ok().body(users);
//        }
//    catch (Exception e){
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
//        }
//    }
}
