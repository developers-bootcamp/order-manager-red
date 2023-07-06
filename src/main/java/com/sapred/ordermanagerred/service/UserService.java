package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.dto.UserDTO;
import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.CompanyRepository;
import com.sapred.ordermanagerred.repository.RoleRepository;
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
    private JwtToken jwtToken;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CompanyRepository companyRepository;

    // שימי לב: זו סתם פונקציה שמכניסה נתונים בשביל הבדיקה
    public void fill() {
        AuditData d = new AuditData(new Date(), new Date());
        Role roles = new Role("1", RoleOptions.CUSTOMER, "cust", d);
        roleRepository.save(roles);
        Role roles2 = new Role("2", RoleOptions.ADMIN, "admin", d);
        roleRepository.save(roles2);
        Company c = new Company("1", "osherad", 55, d);
        companyRepository.save(c);
        Address a = new Address("0580000000", "mezada 7", "emailCust@gmail.com");
        User user = new User("4", "new full name", "passCust", a, roles, c, d);
        User user2 = new User("2", "new name", "passadmin", a, roles2, c, d);
        userRepository.save(user);
        userRepository.save(user2);
    }

    public ResponseEntity<String> logIn(String email, String password) {
        User authenticatedUserEmail = userRepository.getByAddressEmail(email);
        if (authenticatedUserEmail == null)
            return new ResponseEntity<>("Resource not found", HttpStatus.NOT_FOUND); // 404
        if(!authenticatedUserEmail.getPassword().equals(password))
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED); // 401
        String token = jwtToken.generateToken(authenticatedUserEmail);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }


    public ResponseEntity<Boolean> deleteCustomer(String token, String customerId) {
        RoleOptions role = jwtToken.getRoleIdFromToken(token);
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        User user = userRepository.findById(customerId).get();
        // אם הוא לא מנהל או שהוא לא מהחברה של הלקוח שאותו הוא רוצה למחוק
        if (role == RoleOptions.CUSTOMER || !user.getCompanyId().getId().equals(companyIdFromToken))
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        try {
            userRepository.deleteById(customerId);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Boolean> editCustomer(String token, UserDTO customer) {
        RoleOptions role = jwtToken.getRoleIdFromToken(token);
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        User user = userRepository.findById(customer.getId()).get();
        // אם הוא לא מנהל או שהוא לא מהחברה של הלקוח שאותו הוא רוצה לערוך
        if (role == RoleOptions.CUSTOMER || !user.getCompanyId().getId().equals(companyIdFromToken))
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        try {
            user.setFullName(customer.getFullName());
            Address address = new Address(customer.getPhone(), customer.getAddress(), customer.getEmail());
            user.setAddress(address);
            user.getAuditData().setUpdateDate(new Date());
            userRepository.save(user);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    public List<String> getAllNames(String token) {
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        List<User> us = userRepository.getAllByCompanyId(companyIdFromToken)
                .stream()
                .filter(u -> u.getRoleId().getName().equals(RoleOptions.CUSTOMER))
                .toList();
        List<String> listNames = new ArrayList<>();
        for (User user : us)
            listNames.add(user.getFullName());
        return listNames;
    }



}
