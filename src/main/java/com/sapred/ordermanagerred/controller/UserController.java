package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.dto.UserDTO;
import com.sapred.ordermanagerred.exception.NoPermissionException;
import com.sapred.ordermanagerred.model.User;
import com.sapred.ordermanagerred.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/fill")
    public void fill() {
        userService.fill();
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<Boolean> deleteUser(@RequestHeader("token") String token, @PathVariable("userId") String userId) {
        try {
            userService.deleteUser(token, userId);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (NoPermissionException e) {
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/editUser")
    public ResponseEntity<Boolean> editUser(@RequestHeader("token") String token, @RequestBody User user) {
        try {
            userService.editUser(token, user);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (NoPermissionException e) {
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAllNamesOfCustomers")
    public List<Map.Entry<String, String>> getAllNamesOfCustomers(@RequestHeader("token") String token) {
        return userService.getAllNamesOfCustomers(token);
    }
}
