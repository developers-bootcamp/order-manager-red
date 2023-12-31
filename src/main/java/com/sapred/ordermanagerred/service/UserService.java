package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.dto.TokenRole;
import com.sapred.ordermanagerred.dto.UserDTO;
import com.sapred.ordermanagerred.dto.UserNameDTO;
import com.sapred.ordermanagerred.exception.DataExistException;
import com.sapred.ordermanagerred.exception.NoPermissionException;
import com.sapred.ordermanagerred.mapper.UserMapper;
import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.CompanyRepository;
import com.sapred.ordermanagerred.repository.RoleRepository;
import com.sapred.ordermanagerred.repository.UserRepository;
import com.sapred.ordermanagerred.security.JwtToken;
import com.sapred.ordermanagerred.security.PasswordValidator;
import lombok.SneakyThrows;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
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

    private int pageSize = 100;

    public RoleOptions getRoleFromToken(String token) {
        RoleOptions roleOptions = jwtToken.getRoleIdFromToken(token);
        return roleOptions;
    }

    @SneakyThrows
    public TokenRole logIn(String email, String password) {
        logger.info("Logging in user with email: {}", email);

        User authenticatedUserEmail = userRepository.getByAddressEmail(email);

        if (authenticatedUserEmail == null) {
            logger.error("User not found with email: {}", email);
            throw new NotFoundException("User not found with email: %s".formatted(email));
        }

        if (!passwordEncoder.matches(password, authenticatedUserEmail.getPassword())) {
            logger.error("Invalid password for user with email: {}", email);
            throw new NoPermissionException("Invalid password for user with email: {}".formatted(email));
        }
        String token = jwtToken.generateToken(authenticatedUserEmail);
        logger.info("User logged in successfully: {}", email);
        RoleOptions roleOptions = jwtToken.getRoleIdFromToken(token);
        TokenRole tokenRole = TokenRole.builder().token(token).role(roleOptions).build();
        return tokenRole;
    }

    @SneakyThrows
    public TokenRole signUp(String fullName, String companyName, Currency currency, String email, String password) {
        logger.info("Signing up user with email: {}", email);

        if (!EmailValidator.getInstance().isValid(email) || !passwordValidator.isValid(password)) {
            logger.error("Invalid email or password for signup: {}", email);
            throw new IllegalArgumentException("the password or the email invalid");
        }

        if (userRepository.existsByAddressEmail(email)) {
            logger.error("Email already exists: {}", email);
            throw new DataExistException("the email address already exists");
        }

        AuditData auditData = AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build();
        Address address = Address.builder().email(email).build();
        User user = User.builder().fullName(fullName).password(passwordEncoder.encode(password)).address(address).roleId((roleRepository.findFirstByName(RoleOptions.ADMIN))).companyId(createCompany(companyName, currency, auditData)).auditData(auditData).build();
        userRepository.save(user);
        String token = jwtToken.generateToken(user);
        logger.info("User signed up successfully: {}", email);
        RoleOptions roleOptions = jwtToken.getRoleIdFromToken(token);
        TokenRole tokenRole = TokenRole.builder().token(token).role(roleOptions).build();
        return tokenRole;
    }

    @SneakyThrows
    private Company createCompany(String companyName, Currency currency, AuditData auditData) {
        logger.info("Creating company: {}", companyName);

        if (companyRepository.existsByName(companyName)) {
            logger.error("Company name already exists: {}", companyName);
            throw new DataExistException("the name of the company already exist");
        }

        Company company = Company.builder().name(companyName).currency(currency).auditData(auditData).build();
        return companyRepository.save(company);
    }

    @SneakyThrows
    public void deleteUser(String token, String userId) {
        logger.info("Deleting user with ID: {}", userId);

        RoleOptions role = jwtToken.getRoleIdFromToken(token);
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        User user = userRepository.findById(userId).orElse(null);

        if (role == RoleOptions.CUSTOMER || !user.getCompanyId().getId().equals(companyIdFromToken) ||
                (role == RoleOptions.EMPLOYEE && user.getRoleId().getName().equals(RoleOptions.ADMIN))) {
            logger.error("Unauthorized deletion for user with ID: {}", userId);
            throw new NoPermissionException("You do not have the appropriate permission to delete user");
        }

        userRepository.deleteById(userId);
        logger.info("User deleted successfully: {}", userId);
    }

    @SneakyThrows
    public void addUser(String token, UserDTO userDTO) {
        logger.info("Adding new user");
        User user = UserMapper.INSTANCE.DTOToUser(userDTO);

        RoleOptions role = jwtToken.getRoleIdFromToken(token);
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        user.setCompanyId(companyRepository.findFirstById(companyIdFromToken));
        user.setFullName(userDTO.getFullName());
        String customerRoleId = roleRepository.findFirstByName(RoleOptions.CUSTOMER).getId();
        if (!user.getRoleId().getId().equals(customerRoleId))
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        String roleId = roleRepository.findFirstByName(RoleOptions.ADMIN).getId();
        if (role == RoleOptions.CUSTOMER || !user.getCompanyId().getId().equals(companyIdFromToken)
                || (role == RoleOptions.EMPLOYEE && !user.getRoleId().getId().equals(customerRoleId))) {
            logger.error("Unauthorized user addition");
            throw new UnsupportedOperationException();
        }

        if (userRepository.existsByAddress_Email(user.getAddress().getEmail())) {
            logger.error("User email already exists: {}", user.getAddress().getEmail());
            throw new IllegalArgumentException();
        }

        user.setAuditData(AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build());
        userRepository.save(user);

        logger.info("User added successfully: {}", user.getId());
    }


    public List<UserDTO> getUsers(String token, int numPage) {
        logger.info("Fetching users");

        RoleOptions role = jwtToken.getRoleIdFromToken(token);
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        Pageable pageable = PageRequest.of(numPage, pageSize);
        Page<User> userPage = userRepository.findByCompanyId(companyIdFromToken, pageable);
        List<UserDTO> userDTOs = UserMapper.INSTANCE.userToDTO(userPage.getContent());

        logger.info("Users fetched successfully");
        return userDTOs;
    }

    @SneakyThrows
    public UserDTO updateUser(String token, String userId, UserDTO userToEdit1) {
        logger.info("Updating user with ID: {}", userId);
        User userToEdit = UserMapper.INSTANCE.DTOToUser(userToEdit1);

        RoleOptions role = jwtToken.getRoleIdFromToken(token);
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        User findUser = userRepository.findById(userId).orElse(null);

        if (findUser == null) {
            logger.error("User not found with ID: {}", userId);
            throw new NotFoundException("User not found");
        }

        if (role == RoleOptions.CUSTOMER || !findUser.getCompanyId().getId().equals(companyIdFromToken) ||
                (role == RoleOptions.EMPLOYEE && findUser.getRoleId().getName().equals(RoleOptions.ADMIN))) {
            logger.error("Unauthorized user update for user with ID: {}", userId);
            throw new NoPermissionException("You do not have the appropriate permission to edit user");
        }

        if (!findUser.getAddress().getEmail().equals(userToEdit.getAddress().getEmail()) &&
                userRepository.existsByAddressEmail(userToEdit.getAddress().getEmail())) {
            logger.error("User email already exists: {}", userToEdit.getAddress().getEmail());
            throw new DataExistException("The email address already exists");
        }

        Query query = new Query(Criteria.where("id").is(userId));
        Update update = new Update()
                .set("fullName", userToEdit.getFullName())
                .set("password", userToEdit.getPassword())
                .set("address", userToEdit.getAddress())
                .set("auditData.updateDate", LocalDate.now());

        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);
        User updatedUser = mongoTemplate.findAndModify(query, update, options, User.class);

        UserDTO updatedUser1 = UserMapper.INSTANCE.userToDTO(updatedUser);
        logger.info("User updated successfully: {}", updatedUser.getId());
        return updatedUser1;
    }

    public List<UserNameDTO> getNamesOfCustomersByPrefix(String token, String prefix) {
        logger.info("Fetching names of customers by prefix: {}", prefix);

        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        List<User> us = userRepository.findByCompanyIdAndRoleIdAndPrefix(companyIdFromToken, "3", prefix);
        List<UserNameDTO> filteredNames = new ArrayList<>();

        for (User user : us)
            filteredNames.add(new UserNameDTO(user.getId(), user.getFullName()));

        logger.info("Names of customers fetched successfully");
        return filteredNames;
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
}
