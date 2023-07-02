package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.dto.UserDTO;
import com.sapred.ordermanagerred.exception.NoPermissionException;
import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.CompanyRepository;
import com.sapred.ordermanagerred.repository.RoleRepository;
import com.sapred.ordermanagerred.repository.UserRepository;
import com.sapred.ordermanagerred.security.JwtToken;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public void fill() {
        AuditData d = new AuditData(new Date(), new Date());
        Role roles = new Role("1", RoleOptions.CUSTOMER, "cust", d);
        roleRepository.save(roles);
        Company c = new Company("1", "osherad", 55, d);
        companyRepository.save(c);
        Address a = new Address("0580000000", "mezada 7", "emailCust@gmail.com");
        User user = new User("4", "new full name", "passCust", a, roles, c, d);
        userRepository.save(user);
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


    @SneakyThrows
    public void deleteUser(String token, String userId) {
        RoleOptions role = jwtToken.getRoleIdFromToken(token);
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        User user = userRepository.findById(userId).get();
        if (role == RoleOptions.CUSTOMER || !user.getCompanyId().getId().equals(companyIdFromToken))
            throw new NoPermissionException("You do not have the appropriate permission to delete user");
        userRepository.deleteById(userId);
    }

    @SneakyThrows
    public void editUser(String token, User user) {
        RoleOptions role = jwtToken.getRoleIdFromToken(token);
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        User userTOEdit = userRepository.findById(user.getId()).get();
        if (role == RoleOptions.CUSTOMER || !user.getCompanyId().getId().equals(companyIdFromToken))
            throw new NoPermissionException("You do not have the appropriate permission to edit user");
        userTOEdit.setFullName(user.getFullName());
        Address address = new Address(user.getAddress().getPhone(),
                user.getAddress().getName(), user.getAddress().getEmail());
        userTOEdit.setAddress(address);
        userTOEdit.getAuditData().setUpdateDate(new Date());
        userRepository.save(user);
    }

    public List<Map.Entry<String, String>> getAllNamesOfCustomers(String token) {
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        List<User> us = userRepository.getAllByCompanyId(companyIdFromToken)
                .stream().filter(u -> u.getRoleId().getName().equals(RoleOptions.CUSTOMER)).toList();
        List<Map.Entry<String, String>> listNames = new ArrayList<>();
        for (User user : us)
            listNames.add(new HashMap.SimpleEntry<>(user.getId(), user.getFullName()));
        return listNames;
    }



}
