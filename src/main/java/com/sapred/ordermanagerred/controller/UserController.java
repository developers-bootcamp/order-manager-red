package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.dto.UserDTO;
import com.sapred.ordermanagerred.dto.UserNameDTO;
import com.sapred.ordermanagerred.exception.DataExistException;
import com.sapred.ordermanagerred.exception.InvalidDataException;
import com.sapred.ordermanagerred.exception.NoPermissionException;
import com.sapred.ordermanagerred.model.Currency;
import com.sapred.ordermanagerred.model.User;
import com.sapred.ordermanagerred.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin("http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/logIn/{email}/{password}")
    public ResponseEntity<String> logIn(@PathVariable("email") String email, @PathVariable("password") String password) {
        try {
            return new ResponseEntity<String>(userService.logIn(email, password), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<String>(e.getMessage(), e.getStatusCode());
        } catch (RuntimeException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/fill")
    public void fill() {
        userService.fill();
    }

    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestParam("fullName") String fullName, @RequestParam("companyName") String companyName,
                                 @RequestParam("currency") Integer currency, @RequestParam("email") String email,
                                 @RequestParam("password") String password) {
        try {
            String token = userService.signUp(fullName, companyName, currency, email, password);
            return new ResponseEntity(token, HttpStatus.OK);
        } catch (InvalidDataException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        } catch (DataExistException ex) {
            return new ResponseEntity(ex, HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Boolean> deleteUser(@RequestHeader("token") String token, @PathVariable("userId") String userId) {
        try {
            userService.deleteUser(token, userId);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (NoPermissionException e) {
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    @PutMapping("/{userId}")
    public ResponseEntity<Boolean> updateUser(@RequestHeader("token") String token, @PathVariable("userId") String userId, @RequestBody User user) {
        try {
            User u = userService.updateUser(token, userId, user);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (NoPermissionException e) {
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        } catch (DataExistException e) {
            return new ResponseEntity<>(false, HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{pageNumber}")
    public ResponseEntity getUsers(@RequestHeader("token") String token,@PathVariable("pageNumber") int pageNumber) {
        try {
            List<UserDTO> user = userService.getUsers(token, pageNumber);
            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getNamesOfCustomersByPrefix/{prefix}")
    public List<UserNameDTO> getNamesOfCustomersByPrefix(@RequestHeader("token") String token, @PathVariable String prefix) {
        return userService.getNamesOfCustomersByPrefix(token, prefix);
    }
}
