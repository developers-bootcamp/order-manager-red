package com.sapred.ordermanagerred.service;


import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.CompanyRepository;
import com.sapred.ordermanagerred.repository.OrderRepository;
import com.sapred.ordermanagerred.repository.RoleRepository;
import com.sapred.ordermanagerred.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.sapred.ordermanagerred.security.JwtToken;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;


@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtToken jwtToken;
//    @Value("${pageSize}")
//    private int pageSize;

//    public List<Order> getOrders(String token, String statusId, int pageNumber, String userId) {
//
//        String companyId = jwtToken.getCompanyIdFromToken(token);
//
//        Sort.Order sortOrder = Sort.Order.asc("auditData.updateDate");
//        Sort sort = Sort.by(sortOrder);
////        StatusOptions orderStatus = StatusOptions.valueOf(statusId);
//
//        Pageable pageable = PageRequest.of(pageNumber, pageSize/* pageSize parameter omitted */, sort);
//
//        Page<Order> pageOrders = orderRepository.findByCompanyId_IdAndOrderStatusAndEmployeeId(companyId, statusId, userId, pageable);
//        return pageOrders.getContent();
//    }

    // note! it is a function just to fill data
    public void fill() {
        List<Company> companies = new ArrayList<Company>();
        List<Role> roles = new ArrayList<Role>();
        List<User> users = new ArrayList<User>();
        List<Order> orders = new ArrayList<Order>();

        AuditData d = AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build();
        AuditData d1 = new AuditData(LocalDate.now(), LocalDate.now());
        AuditData d2 = new AuditData(LocalDate.of(2023, 6, 3), LocalDate.now());
        AuditData d3 = new AuditData(LocalDate.of(2023, 5, 1), LocalDate.now());

        Company company1 = new Company("11", "Poto", 88, d3);
        Company company2 = new Company("12", "PotoGeula", 88, d2);
        Company company3 = new Company("13", "Grafgik", 88, d2);
        companies.add(company1);
        companies.add(company2);
        companies.add(company3);
        Role role1 = new Role("101", RoleOptions.ADMIN, "bos", d3);
        Role role2 = new Role("102", RoleOptions.EMPLOYEE, "GOOD EMPLOYEE", d2);
        Role role3 = new Role("103", RoleOptions.CUSTOMER, "CUSTOMER", d1);
        roles.add(role1);
        roles.add(role2);
        roles.add(role3);
        User user1 = new User("1001", "Shlomo Cohen", "1001", new Address(), role1, company1, d3);
        User user2 = new User("1002", "Yoram", "1002", new Address(), role2, company1, d2);
        User user6 = new User("1006", "Mendi", "1006", new Address(), role2, company1, d2);
        User user7 = new User("1007", "Morya", "1007", new Address(), role2, company1, d2);

        User user3 = new User("1003", "family Simoni", "1003", new Address(), role3, company1, d1);
        User user4 = new User("1004", "family Markoviz", "1004", new Address(), role3, company1, d1);
        User user5 = new User("1005", "family Chayimoviz", "1005", new Address(), role3, company1, d1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
        users.add(user6);
        users.add(user7);
        for (int i = 200; i < 500; i++) {
            if (i % 3 == 0)
                orders.add(new Order(Integer.toString(i), user2, user3, i * 2, null, StatusOptions.DONE, company1, 143, new Date(), 2, true, d1));
            else if (i % 3 == 1)
                orders.add(new Order(Integer.toString(i), user6, user4, i * 2, null, StatusOptions.CREATED, company1, 263, new Date(), 1, true, d2));
            else
                orders.add(new Order(Integer.toString(i), user7, user5, i * 2, null, StatusOptions.DONE, company1, 324, new Date(), 3, true, d1));
        }
        companyRepository.saveAll(companies);
        roleRepository.saveAll(roles);
        userRepository.saveAll(users);
        orderRepository.saveAll(orders);

    }
}

