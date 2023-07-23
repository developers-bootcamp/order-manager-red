package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.dto.UserDTO;
import com.sapred.ordermanagerred.mapper.UserMapper;
import com.sapred.ordermanagerred.dto.UserNameDTO;
import com.sapred.ordermanagerred.exception.DataExistException;
import com.sapred.ordermanagerred.exception.InvalidDataException;
import com.sapred.ordermanagerred.mapper.UserMapper;
import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.exception.NoPermissionException;
import com.sapred.ordermanagerred.repository.CompanyRepository;
import com.sapred.ordermanagerred.repository.RoleRepository;
import com.sapred.ordermanagerred.repository.UserRepository;
import com.sapred.ordermanagerred.security.JwtToken;
import com.sapred.ordermanagerred.security.PasswordValidator;
import lombok.SneakyThrows;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
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
    private MongoTemplate mongoTemplate;

    public void fill() {
        AuditData d = AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build();
        Role role = new Role("2", RoleOptions.EMPLOYEE, "cust", d);
        roleRepository.save(role);
        Company c = new Company("1", "osherad", 55, d);
        companyRepository.save(c);
        Address a = new Address("0580000000", "mezada 7", "custp");
        User user = User.builder().fullName("cust")
                .password("custp")
                .address(a)
                .roleId(role).companyId(c).auditData(d)
                .build();
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
    public String signUp(String fullName, String companyName, int currency, String email, String password) {
        if (!EmailValidator.getInstance().isValid(email) || !passwordValidator.isValid(password))
            throw new InvalidDataException("the password or the email invalid");
        if (userRepository.existsByAddressEmail(email))
            throw new DataExistException("the email address already exists");
        AuditData auditData = AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build();
        Address address = Address.builder().email(email).build();
        User user = User.builder().fullName(fullName)
                .password(password).address(address)
                .roleId((roleRepository.findFirstByName(RoleOptions.ADMIN)))
                .companyId(createCompany(companyName, currency, auditData))
                .auditData(auditData).build();
        userRepository.save(user);
        return jwtToken.generateToken(user);
    }

    @SneakyThrows
    private Company createCompany(String companyName, int currency, AuditData auditData) {
        if (companyRepository.existsByName(companyName))
            throw new DataExistException("the name of the company already exist");
        Company company = Company.builder().name(companyName).currency(currency).auditData(auditData).build();
        return companyRepository.save(company);
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
    public User addUser(String token, User user) {
        RoleOptions role = jwtToken.getRoleIdFromToken(token);
        System.out.println(role);
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        if (role == RoleOptions.CUSTOMER || !user.getCompanyId().getId().equals(companyIdFromToken) ||
                (role == RoleOptions.EMPLOYEE && user.getRoleId().getName().equals(RoleOptions.ADMIN)))
            throw new UnsupportedOperationException();
        if (userRepository.existsByAddress_Email(user.getAddress().getEmail()) == true)
            throw new IllegalArgumentException();
        user.setAuditData(new AuditData(LocalDate.now(), LocalDate.now()));
        return userRepository.insert(user);
    }


    @Value("${pageSize}")
    private int pageSize;

    public List<UserDTO> getUsers(String token, int numPage) {
        RoleOptions role = jwtToken.getRoleIdFromToken(token);
        System.out.println(role);
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        Pageable pageable = PageRequest.of(numPage, pageSize);
        Page<User> userPage = userRepository.findByCompanyId(companyIdFromToken,pageable);
        return UserMapper.INSTANCE.userToDTO(userPage.getContent());
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
                .set("auditData.updateDate", LocalDate.now());
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);
        return mongoTemplate.findAndModify(query, update, options, User.class);
    }

    public List<UserNameDTO> getNamesOfCustomersByPrefix(String token, String prefix) {
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        List<User> us = userRepository.findByCompanyIdAndRoleIdAndPrefix(companyIdFromToken, "3", prefix);
        List<UserNameDTO> filteredNames = new ArrayList<>();
        for (User user : us)
            filteredNames.add(new UserNameDTO(user.getId(), user.getFullName()));
        return filteredNames;
    }
}
