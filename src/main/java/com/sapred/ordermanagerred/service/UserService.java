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
import org.springframework.web.server.ResponseStatusException;

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
        User user = new User("8", "dasi", "passCst", a, roles, c, d);
        userRepository.save(user);
    }

    @SneakyThrows
    public String logIn(String email, String password) {
        User authenticatedUserEmail = userRepository.getByAddressEmail(email);
        if (authenticatedUserEmail == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if(!authenticatedUserEmail.getPassword().equals(password))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        String token = jwtToken.generateToken(authenticatedUserEmail);
        return token;
    }


    @SneakyThrows
    public void deleteUser(String token, String userId) {
        RoleOptions role = jwtToken.getRoleIdFromToken(token);
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        User user = userRepository.getById(userId);
        if (role == RoleOptions.CUSTOMER || !user.getCompanyId().getId().equals(companyIdFromToken))
            throw new NoPermissionException("You do not have the appropriate permission to delete user");
        userRepository.deleteById(userId);
    }

    @SneakyThrows
    public void editUser(String token, User user) {
        RoleOptions role = jwtToken.getRoleIdFromToken(token);
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        User userTOEdit = userRepository.getById(user.getId());
        if (role == RoleOptions.CUSTOMER || !user.getCompanyId().getId().equals(companyIdFromToken))
            throw new NoPermissionException("You do not have the appropriate permission to edit user");
        userTOEdit.setFullName(user.getFullName());
        Address address = new Address(user.getAddress().getPhone(),
                user.getAddress().getName(), user.getAddress().getEmail());
        userTOEdit.setAddress(address);
        userTOEdit.getAuditData().setUpdateDate(new Date());
        userRepository.save(userTOEdit);
    }

    public List<Map.Entry<String, String>> getNamesOfCustomersByPrefix(String token, String prefix) {
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        List<User> us = userRepository.findByCompanyId_IdAndRoleId_Id(companyIdFromToken, "1");
        List<Map.Entry<String, String>> filteredNames = new ArrayList<>();
        for (User user : us)
            if (user.getFullName().toLowerCase().startsWith(prefix.toLowerCase()))
                filteredNames.add(new HashMap.SimpleEntry<>(user.getId(), user.getFullName()));
        return filteredNames;
    }
}
