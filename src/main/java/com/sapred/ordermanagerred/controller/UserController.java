package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.Exception.DataExistException;
import com.sapred.ordermanagerred.Exception.InvalidDataException;
import com.sapred.ordermanagerred.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/signup")
    public ResponseEntity signUP(@RequestParam("fullName") String fullName, @RequestParam("companyName") String companyName, @RequestParam("email") String email, @RequestParam("password") String password) {
        try {
            String token = userService.signUp(fullName, companyName, email, password);
            return new ResponseEntity(token, HttpStatus.OK);
        } catch (InvalidDataException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        } catch (DataExistException ex) {
            return new ResponseEntity(ex, HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
