package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/signUp")
    public ResponseEntity<String> signUP(@RequestParam("fullName") String fullName,@RequestParam("companyName") String companyName, @RequestParam("email") String email,@RequestParam("password") String password){
        return userService.signUp(fullName, companyName, email, password);
    }
}
