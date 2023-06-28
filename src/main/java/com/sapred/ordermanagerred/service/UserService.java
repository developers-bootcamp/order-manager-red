package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.AddressRepository;
import com.sapred.ordermanagerred.repository.UserRepository;
import com.sapred.ordermanagerred.security.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private JwtToken jwtToken;

    // שימי לב: זו סתם פונקציה שמכניסה נתונים בשביל הבדיקה
    public void fill() {
        AuditData d = new AuditData( new Date(),new Date());
        Role roles = new Role("11", RoleOptions.CUSTOMER, "kkkR",d);
        Company c = new Company("52","gg",55,d);
        Address a = new Address( "0580000000","mezada 7","kamatek@gmail.com");
        addressRepository.save(a);
        User user = new User("123","user","mypass",a,roles,c,d);
        userRepository.save(user);
    }

    public List<User> getAll() {
        ArrayList<User> listAll = new ArrayList<>();
        userRepository.findAll().forEach(u -> listAll.add(u));
        return listAll;
    }


    public ResponseEntity<String> logIn(String email, String password) {
        User authenticatedUserEmail = authenticateUserEmail(email);
        if (authenticatedUserEmail == null)
            return new ResponseEntity<>("Resource not found", HttpStatus.NOT_FOUND); // 404
        if(!authenticateUserPassword(authenticatedUserEmail, password))
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED); // 401
        String token = jwtToken.generateToken(authenticatedUserEmail);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    // אימות מייל משתמש
    public User authenticateUserEmail(String email) {
        List<User> us = getAll();
        User user = us.stream()
                .filter(u -> u.getAddress().getEmail().equals(email))
                .findFirst()
                .orElse(null);
        return user;
    }

    // אימות ססמת משתמש
    public boolean authenticateUserPassword(User user, String password) {
        return user.getPassword().equals(password);
    }



}
