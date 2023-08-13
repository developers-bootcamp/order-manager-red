package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.dto.UserDTO;
import com.sapred.ordermanagerred.mapper.UserMapper;
import com.sapred.ordermanagerred.dto.UserNameDTO;
import com.sapred.ordermanagerred.exception.DataExistException;
import com.sapred.ordermanagerred.exception.InvalidDataException;
import com.sapred.ordermanagerred.mapper.UserMapper;
import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.exception.NoPermissionException;
import com.sapred.ordermanagerred.model.Currency;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.*;

@Service
public class UserService {
    public UserService(){
        this.passwordEncoder=new BCryptPasswordEncoder();
    }
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

    private BCryptPasswordEncoder passwordEncoder;

    public void fill() {
        AuditData d = AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build();
        Role role = new Role("2", RoleOptions.EMPLOYEE, "cust", d);
        roleRepository.save(role);
        Company c = new Company("1", "osherad", Currency.SHEKEL, d);
        companyRepository.save(c);
        Address a = new Address("0580000000", "mezada 7", "custp");
        User user = User.builder().fullName("cust")
                .password("custp")
                .address(a)
                .roleId(role).companyId(c).auditData(d)
                .build();
        userRepository.save(user);
    }

    public void fillRoles() {
        AuditData d = AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build();
        Role role1 = new Role("1", RoleOptions.ADMIN, "admin", d);
        roleRepository.save(role1);
        Role role2 = new Role("2", RoleOptions.EMPLOYEE, "employee", d);
        roleRepository.save(role2);
        Role role3 = new Role("3", RoleOptions.CUSTOMER, "customer", d);
        roleRepository.save(role3);
    }

    @SneakyThrows
    public String logIn(String email, String password) {
        User authenticatedUserEmail = userRepository.getByAddressEmail(email);
        if (authenticatedUserEmail == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if(!passwordEncoder.matches(password, authenticatedUserEmail.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        String token = jwtToken.generateToken(authenticatedUserEmail);
        return token;
    }

    @SneakyThrows
    public String signUp(String fullName, String companyName, Currency currency, String email, String password) {
        if (!EmailValidator.getInstance().isValid(email) || !passwordValidator.isValid(password))
            throw new InvalidDataException("the password or the email invalid");
        if (userRepository.existsByAddressEmail(email))
            throw new DataExistException("the email address already exists");
        AuditData auditData = AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build();
        Address address = Address.builder().email(email).build();
        User user = User.builder().fullName(fullName)
                .password(passwordEncoder.encode(password))
                .address(address)
                .roleId((roleRepository.findFirstByName(RoleOptions.ADMIN)))
                .companyId(createCompany(companyName, currency, auditData))
                .auditData(auditData).build();
        userRepository.save(user);
        return jwtToken.generateToken(user);
    }

    @SneakyThrows
    private Company createCompany(String companyName,Currency currency, AuditData auditData) {
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
    public User updateUser(String token, String userId, User userToEdit) {
        RoleOptions role = jwtToken.getRoleIdFromToken(token);
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        User findUser = userRepository.findById(userId).orElse(null);
        if(findUser == null)
            throw new NotFoundException("User not found");
        if (role == RoleOptions.CUSTOMER || !findUser.getCompanyId().getId().equals(companyIdFromToken) ||
                (role == RoleOptions.EMPLOYEE && findUser.getRoleId().getName().equals(RoleOptions.ADMIN)))
            throw new NoPermissionException("You do not have the appropriate permission to edit user");
        if (userRepository.existsByAddressEmail(userToEdit.getAddress().getEmail()))
            throw new DataExistException("The email address already exists");
        Query query = new Query(Criteria.where("id").is(userId));
        Update update = new Update()
                .set("fullName", userToEdit.getFullName())
                .set("password", userToEdit.getPassword())
                .set("address", userToEdit.getAddress())
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
