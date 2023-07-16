package com.sapred.ordermanagerred.service;


import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.OrderRepository;
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
    private JwtToken jwtToken;
    @Value("${pageSize}")
    private int pageSize;

    public List<Order> getOrders(String token, String statusId, int pageNumber, String userId) {

        String companyId = jwtToken.getCompanyIdFromToken(token);

        Sort.Order sortOrder = Sort.Order.asc("auditData.updateDate");
        Sort sort = Sort.by(sortOrder);
        StatusOptions orderStatus = StatusOptions.valueOf(statusId);

        Pageable pageable = PageRequest.of(pageNumber, pageSize/* pageSize parameter omitted */, sort);

        Page<Order> pageOrders = orderRepository.findByCompanyIdAndOrderStatusAndEmployeeId(companyId, orderStatus, userId, pageable);
        return pageOrders.getContent();
    }

    // note! it is a function just to fill data
    public void fill() {
<<<<<<< Updated upstream
        AuditData d = AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build();
=======
        AuditData d1 = new AuditData(new Date(), new Date());
        AuditData d2 = new AuditData(new Date(2023,6,3), new Date());
        AuditData d3 = new AuditData(new Date(2023,5,1), new Date());

>>>>>>> Stashed changes
        List<Order> orders = new ArrayList<Order>();
        Company company1=new Company("11", "PotoNeveYaakov", 88, d3);
        Company company2=new Company("12", "PotoGeula", 88, d2);
        Company company3=new Company("13", "Grafgik", 88, d2);
        Role role1=new Role("101",RoleOptions.ADMIN,"bos",d3);
        Role role2=new Role("102",RoleOptions.EMPLOYEE,"GOOD EMPLOYEE",d2);
        Role role3=new Role("103",RoleOptions.CUSTOMER,"CUSTOMER",d1);

        User user1=new User("1001","Shlomo Cohen","1001",new Address(),role1,company1,d3);

        User user2=new User("1002","Yoram","1002",new Address(),role2,company1,d2);
        User user6=new User("1006","Mendi","1006",new Address(),role2,company1,d2);
        User user7=new User("1007","Morya","1007",new Address(),role2,company1,d2);

        User user3=new User("1003","family Simoni","1003",new Address(),role3,company1,d1);
        User user4=new User("1004","family Markoviz","1004",new Address(),role3,company1,d1);
        User user5=new User("1005","family Chayimoviz","1005",new Address(),role3,company1,d1);

        for (int i = 200; i < 500; i++) {
            if (i % 3 == 0)
<<<<<<< Updated upstream
                orders.add(new Order(Integer.toString(i), "employee", "customer", i * 2, new Company("11", "333", 88, d), new AuditData(LocalDate.now(), new Date(i * 1000).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()), "1"));
            else if (i % 3 == 1)
                orders.add(new Order(Integer.toString(i), "custumer", "customer", i * 2, new Company("22", "333", 88, d), new AuditData(LocalDate.now(),new Date(i * 1000).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()), "2"));
            else
                orders.add(new Order(Integer.toString(i), "111", "customer", i * 2, new Company("11", "333", 88, d), new AuditData(LocalDate.now(),new Date(i * i).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()), "3"));
=======
                orders.add(new Order(Integer.toString(i),"user2","user3", i * 2,null,StatusOptions.DONE, "101", 143,new Date(),2,true,d1));
            else if (i % 3 == 1)
                orders.add(new Order(Integer.toString(i),"1006","1004", i * 2,null,StatusOptions.DONE, "101", 263,new Date(),1,true,d2));
            else
                orders.add(new Order(Integer.toString(i),"1007","1005", i * 2,null,StatusOptions.DONE, "101", 324,new Date(),3,true,d1));
>>>>>>> Stashed changes
        }
        orderRepository.saveAll(orders);

    }
}

