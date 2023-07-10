package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.Exception.DataExistException;
import com.sapred.ordermanagerred.Exception.InvalidDataException;
import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.AddressRepository;
import com.sapred.ordermanagerred.exception.NoPermissionException;
import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.CompanyRepository;
import com.sapred.ordermanagerred.repository.RoleRepository;
import com.sapred.ordermanagerred.repository.UserRepository;
import com.sapred.ordermanagerred.security.JwtToken;
import com.sapred.ordermanagerred.security.PasswordValidator;
import org.apache.commons.validator.routines.EmailValidator;
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

import java.security.PrivateKey;
import java.util.Date;
import java.util.List;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtToken jwtToken;
    @Autowired
    private PasswordValidator passwordValidator;

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

    public String signUp(String fullName, String companyName, String email, String password) {
        if (!EmailValidator.getInstance().isValid(email) || !passwordValidator.isValid(password))
            throw new InvalidDataException("the password or the email invalid");
        if (userRepository.existsByAddressEmail(email))
            throw new DataExistException("some of the data already exists");
        AuditData auditData = new AuditData(new Date(), new Date());
        Address address = new Address();
        address.setEmail(email);
        User user = new User();
        user.setAddress(address);
        user.setAuditData(auditData);
        user.setFullName(fullName);
        user.setCompanyId(createCompany(companyName, auditData));
        user.setPassword(password);
        user.setRoleId(roleRepository.findFirstByName(RoleOptions.ADMIN));
        userRepository.save(user);
        return jwtToken.generateToken(user);

    }

    private Company createCompany(String companyName, AuditData auditData) {
        if (companyRepository.existsByName(companyName))
            throw new DataExistException("the name of the company already exist");
        Company company = new Company();
        company.setName(companyName);
        company.setAuditData(auditData);
        companyRepository.save(company);
        return companyRepository.findByName(companyName);
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
    public User editUser(String token, String userId, User user) {
        RoleOptions role = jwtToken.getRoleIdFromToken(token);
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        User userTOEdit = userRepository.findById(userId).get();
        if (role == RoleOptions.CUSTOMER || !user.getCompanyId().getId().equals(companyIdFromToken) ||
                (role == RoleOptions.EMPLOYEE && userTOEdit.getRoleId().getName().equals(RoleOptions.ADMIN)))
            throw new NoPermissionException("You do not have the appropriate permission to edit user");
        Query query = new Query(Criteria.where("id").is(userId));
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
