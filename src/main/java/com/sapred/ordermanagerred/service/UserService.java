package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.AddressRepository;
import com.sapred.ordermanagerred.repository.CompanyRepository;
import com.sapred.ordermanagerred.repository.RoleRepository;
import com.sapred.ordermanagerred.repository.UserRepository;
import com.sapred.ordermanagerred.security.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    RoleRepository roleRepository;
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
        List<User> listAll = userRepository.findAll();
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

    public ResponseEntity<String> signUp(String fullName, String companyName, String email, String password) {
        try {
            if (userRepository.existsByAddressEmail(email) ||
                    companyRepository.existsByName(companyName))
                return ResponseEntity.status(HttpStatus.CONFLICT).body("some of the data already exists");
            AuditData auditData = new AuditData(new Date(), new Date());
            Address address = new Address();
            address.setEmail(email);
            Company company = new Company();
            company.setName(companyName);
            company.setAuditData(auditData);
            companyRepository.insert(company);
            Role role = new Role();
            role.setName(RoleOptions.ADMIN);
            role.setAuditData(auditData);
            roleRepository.insert(role);
            User user = new User();
            user.setAddress(address);
            user.setAuditData(auditData);
            user.setFullName(fullName);
            user.setCompanyId(company);
            user.setPassword(password);
            user.setRoleId(role);
            userRepository.insert(user);
            String token = jwtToken.generateToken(user);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("it's this its don't go well");
        }
    }




}
