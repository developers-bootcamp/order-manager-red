package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.exception.NoPermissionException;
import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.CompanyRepository;
import com.sapred.ordermanagerred.repository.RoleRepository;
import com.sapred.ordermanagerred.repository.UserRepository;
import com.sapred.ordermanagerred.security.JwtToken;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private MongoTemplate mongoTemplate;

    public void fill() {
        AuditData d = new AuditData(new Date(), new Date());
        Role roles = new Role("2", RoleOptions.EMPLOYEE, "cust", d);
        roleRepository.save(roles);
        Company c = new Company("1", "osherad", 55, d);
        companyRepository.save(c);
        Address a = new Address("0580000000", "mezada 7", "ccc");
        User user = new User("8", "ccc", "pass", a, roles, c, d);
        userRepository.save(user);
    }

    @SneakyThrows
    public String logIn(String email, String password) {
        User authenticatedUserEmail = userRepository.getByAddressEmail(email);
        if (authenticatedUserEmail == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if (!authenticatedUserEmail.getPassword().equals(password))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        String token = jwtToken.generateToken(authenticatedUserEmail);
        return token;
    }


    @SneakyThrows
    public void deleteUser(String token, String userId) {
        RoleOptions role = jwtToken.getRoleIdFromToken(token);
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        User user = userRepository.findById(userId).get();
        if (role == RoleOptions.CUSTOMER || !user.getCompanyId().getId().equals(companyIdFromToken) ||
                (role == RoleOptions.EMPLOYEE && user.getRoleId().getName().equals(RoleOptions.ADMIN)))
            throw new NoPermissionException("You do not have the appropriate permission to delete user");
        userRepository.deleteById(userId);
    }

    @SneakyThrows
    public User editUser(String token, User user) {
        RoleOptions role = jwtToken.getRoleIdFromToken(token);
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        User userTOEdit = userRepository.findById(user.getId()).get();
        if (role == RoleOptions.CUSTOMER || !user.getCompanyId().getId().equals(companyIdFromToken) ||
                (role == RoleOptions.EMPLOYEE && userTOEdit.getRoleId().getName().equals(RoleOptions.ADMIN)))
            throw new NoPermissionException("You do not have the appropriate permission to edit user");
        Query query = new Query(Criteria.where("id").is(user.getId()));
        Update update = new Update()
                .set("fullName", user.getFullName())
                .set("password", user.getPassword())
                .set("address", user.getAddress())
                .set("auditData.updateDate", new Date());
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);
        return mongoTemplate.findAndModify(query, update, options, User.class);
    }

    public List<Map.Entry<String, String>> getNamesOfCustomersByPrefix(String token, String prefix) {
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        List<User> us = userRepository.findByCompanyIdAndRoleIdAndPrefix(companyIdFromToken, "3", prefix);
        List<Map.Entry<String, String>> filteredNames = new ArrayList<>();
        for (User user : us)
            filteredNames.add(new HashMap.SimpleEntry<>(user.getId(), user.getFullName()));
        return filteredNames;
    }
}
