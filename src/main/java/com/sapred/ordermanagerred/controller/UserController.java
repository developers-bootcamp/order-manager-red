package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.dto.UserDTO;
import com.sapred.ordermanagerred.dto.UserNameDTO;
import com.sapred.ordermanagerred.exception.DataExistException;
import com.sapred.ordermanagerred.exception.InvalidDataException;
import com.sapred.ordermanagerred.exception.NoPermissionException;
import com.sapred.ordermanagerred.model.Currency;
import com.sapred.ordermanagerred.model.User;
import com.sapred.ordermanagerred.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin("http://localhost:3000")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Value("${spring.profiles.active:}")
    private String activeProfiles;
    @GetMapping("/logIn/{email}/{password}")
    public ResponseEntity<String> logIn(@PathVariable("email") String email, @PathVariable("password") String password) {
        try {
            log.info("Logging in user with email: {}", email);
            return new ResponseEntity<String>(userService.logIn(email, password), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            log.error("Error while logging in: {}", e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), e.getStatusCode());
        } catch (RuntimeException e) {
            log.error("Error while logging in: {}", e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/fill")
    public void fill() {
        log.info("Filling user data");
        userService.fill();
    }

    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestParam("fullName") String fullName, @RequestParam("companyName") String companyName,
                                 @RequestParam("currency") Currency currency, @RequestParam("email") String email,
                                 @RequestParam("password") String password) {
        try {
            log.info("Signing up user with email: {}", email);
            String token = userService.signUp(fullName, companyName, currency, email, password);
            return new ResponseEntity(token, HttpStatus.OK);
        } catch (InvalidDataException ex) {
            log.error("Invalid data during sign-up: {}", ex.getMessage());
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        } catch (DataExistException ex) {
            log.error("Data already exists during sign-up: {}", ex.getMessage());
            return new ResponseEntity(ex, HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Error during sign-up: {}", ex.getMessage());
            return new ResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Boolean> deleteUser(@RequestHeader("token") String token, @PathVariable("userId") String userId) {
        try {
            log.info("Deleting user with ID: {}", userId);
            userService.deleteUser(token, userId);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (NoPermissionException e) {
            log.error("Unauthorized user deletion: {}", e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Error while deleting user: {}", e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity addUser(@RequestHeader("token") String token, @RequestBody User user) {
        try {
            log.info("Adding a new user");
            User newUser = userService.addUser(token, user);
            return ResponseEntity.ok().body(newUser);
        } catch (IllegalArgumentException e) {
            log.error("User addition failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("1");
        } catch (UnsupportedOperationException e) {
            log.error("User addition forbidden: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("2");
        } catch (Exception e) {
            log.error("Error while adding user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("4");
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Boolean> updateUser(@RequestHeader("token") String token, @PathVariable("userId") String userId, @RequestBody User user) {
        try {
            log.info("Updating user with ID: {}", userId);
            User u = userService.updateUser(token, userId, user);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (NoPermissionException e) {
            log.error("Unauthorized user update: {}", e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            log.error("User with ID not found during update: {}", e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        } catch (DataExistException e) {
            log.error("Data conflict during user update: {}", e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error("Error while updating user: {}", e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{pageNumber}")
    public ResponseEntity getUsers(@RequestHeader("token") String token,@PathVariable("pageNumber") int pageNumber) {
        try {
            log.info("Getting users, page number: {}", pageNumber);

            for (String profileName : activeProfiles.split(",")) {
                System.out.println("Currently active profile - " + profileName);
            }
            List<UserDTO> user = userService.getUsers(token, pageNumber);
            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            log.error("Error while getting users: {}", e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getNamesOfCustomersByPrefix/{prefix}")
    public List<UserNameDTO> getNamesOfCustomersByPrefix(@RequestHeader("token") String token, @PathVariable String prefix) {
        log.info("Getting names of customers by prefix: {}", prefix);
        return userService.getNamesOfCustomersByPrefix(token, prefix);
    }
}
