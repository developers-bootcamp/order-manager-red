package com.sapred.ordermanagerred.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBRef;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.sapred.ordermanagerred.dto.ProductCartDTO;
import com.sapred.ordermanagerred.exception.MismatchData;
import com.sapred.ordermanagerred.exception.StatusException;
import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.*;
import com.sapred.ordermanagerred.security.JwtToken;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    @Value("${pageSize}")
    private int pageSize;

    public List<Order> getOrders(String token, String statusId, int pageNumber, String userId) {

        String companyId = jwtToken.getCompanyIdFromToken(token);

        Sort.Order sortOrder = Sort.Order.asc("auditData.updateDate");
        Sort sort = Sort.by(sortOrder);

        Pageable pageable = PageRequest.of(pageNumber, pageSize/* pageSize parameter omitted */, sort);

        Page<Order> pageOrders = orderRepository.findByCompanyId_IdAndOrderStatusAndEmployeeId(companyId, statusId, userId, pageable);
        return pageOrders.getContent();
    }

    public List<Order> getOrdersByFilters(Map<String, Object> filterMap, String token, int pageNumber) {

        MongoCollection<Document> orderCollection = mongoTemplate.getCollection("Order");
        List<Bson> filters = new ArrayList<>();

        // Iterate through the filter map and construct filters for each entry
        for (Map.Entry<String, Object> entry : filterMap.entrySet()) {
            String filterName = entry.getKey();
            Object filterValue = entry.getValue();

            // Add the filter to the list of filters
            filters.add(Filters.eq(filterName, filterValue));
        }

        // Combine all the filters with the AND operator
        Bson finalFilter = Filters.and(filters);

        // Use the final filter to query the collection
        FindIterable<Document> documents = orderCollection.find(finalFilter);

        //print just for check now...
        MongoCursor<Document> cursor = documents.iterator();
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
        return convertFindIterableToOrders(documents);

    }
//    public List<Order> getOrdersByFilters(Map<String, String> filterMap) {
//        Criteria criteria = new Criteria();
//        Query query = new Query();
//
//        for (Map.Entry<String, String> entry : filterMap.entrySet()) {
//            String filterName = entry.getKey();
//            String filterValue = entry.getValue();
//            criteria.and(filterName).is(filterValue);
//        }
//        query.addCriteria(criteria);
//        return orderRepository.find(query);
//    }
    // note! it is a function just to fill data
//    public void fill() {
//        AuditData d = new AuditData(new Date(), new Date());
//        List<Order> orders = new ArrayList<Order>();
//        for (int i = 200; i < 500; i++) {
//            if (i % 3 == 0)
//                orders.add(new Order(Integer.toString(i), "employee", "customer", i * 2, new Company("11", "333", 88, d), new AuditData(new Date(), new Date(i * 1000)), "1"));
//            else if (i % 3 == 1)
//                orders.add(new Order(Integer.toString(i), "custumer", "customer", i * 2, new Company("22", "333", 88, d), new AuditData(new Date(), new Date(i * 1000)), "2"));
//            else
//                orders.add(new Order(Integer.toString(i), "111", "customer", i * 2, new Company("11", "333", 88, d), new AuditData(new Date(), new Date(i * i)), "3"));
//        }
//        orderRepository.saveAll(orders);
//
//    }

    public String createOrder(String token, Order order) {
        String companyId = jwtToken.getCompanyIdFromToken(token);
        if (order.getCompanyId().getId() != companyId)
            throw new MismatchData("the company id is not match to the order's company id");
        if (order.getOrderStatus() != Order.StatusOptions.NEW || order.getOrderStatus() != Order.StatusOptions.APPROVED)
            throw new StatusException("can't create order where status is not NEW or APPROVED ");
        return orderRepository.save(order).getId();
    }

    public void fillProducts() {
        for (int i = 1; i < 10; i++) {
            AuditData d = AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build();
            List<Order> orders = new ArrayList<Order>();
            ProductCategory pc = new ProductCategory(String.valueOf(i), "name" + i, "desc" + i, companyRepository.findById("1").get(), AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build());
            productCategoryRepository.save(pc);
            Product p = new Product(String.valueOf(i), "aaa", "aaa", 40, 50, DiscountType.PERCENTAGE, pc, 4, companyRepository.findById("1").get(), AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build());
            productRepository.save(p);
        }
    }

    public List<ProductCartDTO> calculateOrderAmount(Order order) {
        List<ProductCartDTO> listOfCart = new ArrayList<>();
        ProductCartDTO productCartDTO;
        double sum = 0, discount = 0;
        OrderItem orderItem = order.getOrderItemsList().get(order.getOrderItemsList().size() - 1);
        Product product = productRepository.findById(orderItem.getProductId().getId()).get();
        if (product.getDiscountType() == DiscountType.PERCENTAGE)
            discount = product.getPrice() * orderItem.getQuantity() * product.getDiscount() * 0.01;
        else if (product.getDiscountType() == DiscountType.FIXED_AMOUNT) discount = product.getDiscount();
        sum = product.getPrice() * orderItem.getQuantity() - discount;
        productCartDTO = ProductCartDTO.builder().name(product.getName()).amount(sum).discount(discount).quantity(orderItem.getQuantity()).build();
        listOfCart.add(productCartDTO);
        listOfCart.add(ProductCartDTO.builder().name("Total").amount(order.getTotalAmount() + sum).build());
        return listOfCart;
    }

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
                orders.add(new Order(Integer.toString(i), user2, user3, i * 2, null, Order.StatusOptions.DONE, company1, 143, new Date(), 2, true, d1));
            else if (i % 3 == 1)
                orders.add(new Order(Integer.toString(i), user6, user4, i * 2, null, Order.StatusOptions.CREATED, company1, 263, new Date(), 1, true, d2));
            else
                orders.add(new Order(Integer.toString(i), user7, user5, i * 2, null, Order.StatusOptions.DONE, company1, 324, new Date(), 3, true, d1));
        }
        companyRepository.saveAll(companies);
        roleRepository.saveAll(roles);
        userRepository.saveAll(users);
        orderRepository.saveAll(orders);
    }

    public List<Order> convertFindIterableToOrders(FindIterable<Document> documents)  {


        ObjectMapper objectMapper = new ObjectMapper();

        List<Order> orders = StreamSupport.stream(documents.spliterator(), false)
                .map(document -> {
                    Order order = new Order();
                    order.setId(document.getString("Id"));
                    String orderStatusString = document.getString("orderStatus");
                    Order.StatusOptions orderStatus = Order.StatusOptions.valueOf(orderStatusString);
                    order.setOrderStatus(orderStatus);
                    DBRef employeeDbRef = (DBRef) document.get("employeeId");
                    if (employeeDbRef != null) {
                        User employee = mongoTemplate.findById(employeeDbRef.getId(), User.class);
                        order.setEmployeeId(employee);
                    }
//                    order.setCustomerId(document.get("customerId", User.class));
//

                    order.setTotalAmount(document.getInteger("totalAmount"));

                    // Set the fields for OrderItem, Company, and AuditData
//                    order.setOrderItemsList(document.get("orderItemsList", List.class));
//                    order.setCompanyId(document.get("companyId", Company.class));
                    order.setCreditCardNumber(document.getInteger("creditCardNumber"));
                    order.setExpireOn(document.getDate("ExpireOn"));
                    order.setCvc(document.getInteger("cvc"));
                    order.setNotificationFlag(document.getBoolean("notificationFlag"));
//                    order.setAuditData(document.get("auditData", AuditData.class));

                    return order;
                })
                .collect(Collectors.toList());

        return orders;
    }


}


