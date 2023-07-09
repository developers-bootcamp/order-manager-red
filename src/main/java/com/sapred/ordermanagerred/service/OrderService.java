package com.sapred.ordermanagerred.service;


import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.CompanyRepository;
import com.sapred.ordermanagerred.repository.OrderRepository;
import com.sapred.ordermanagerred.repository.ProductCategoryRepository;
import com.sapred.ordermanagerred.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.sapred.ordermanagerred.security.JwtToken;

import java.util.*;


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

    @Value("${pageSize}")
    private int pageSize;

    public List<Order> getOrders(String token, String statusId, int pageNumber, String userId) {

        String companyId = jwtToken.getCompanyIdFromToken(token);

        Sort.Order sortOrder = Sort.Order.asc("auditData.updateDate");
        Sort sort = Sort.by(sortOrder);

        Pageable pageable = PageRequest.of(pageNumber, pageSize/* pageSize parameter omitted */, sort);

        Page<Order> pageOrders = orderRepository.findByCompanyId_IdAndOrderStatusIdAndEmployee(companyId, statusId, userId, pageable);
        return pageOrders.getContent();
    }

    // note! it is a function just to fill data
    public void fill() {
        AuditData d = new AuditData(new Date(), new Date());
        List<Order> orders = new ArrayList<Order>();
        for (int i = 200; i < 500; i++) {
            if (i % 3 == 0)
                orders.add(new Order(Integer.toString(i), "employee", "customer", i * 2, new Company("11", "333", 88, d), new AuditData(new Date(), new Date(i * 1000)), "1"));
            else if (i % 3 == 1)
                orders.add(new Order(Integer.toString(i), "custumer", "customer", i * 2, new Company("22", "333", 88, d), new AuditData(new Date(), new Date(i * 1000)), "2"));
            else
                orders.add(new Order(Integer.toString(i), "111", "customer", i * 2, new Company("11", "333", 88, d), new AuditData(new Date(), new Date(i * i)), "3"));
        }
        orderRepository.saveAll(orders);

    }

    public void fillProducts() {
        for (int i = 1; i < 10; i++) {
            AuditData d = new AuditData(new Date(), new Date());
            List<Order> orders = new ArrayList<Order>();
            ProductCategory pc = new ProductCategory(i + "", "name" + i, "desc" + i,
                    companyRepository.findById("1").get(), new AuditData(new Date(), new Date()));
            productCategoryRepository.save(pc);
            Product p = new Product(i + "", "aaa", "aaa", 40, 50,
                    DiscountType.PERCENTAGE,  pc, 4,
                    companyRepository.findById("1").get(), new AuditData(new Date(), new Date()));
            productRepository.save(p);
        }
    }

    public List<Map.Entry<String, Map.Entry<Double, Double>>> calculateOrderAmount(List<Map.Entry<String, Integer>> listOfProducts) {
        List<Map.Entry<String, Map.Entry<Double, Double>>> res = new ArrayList<Map.Entry<String, Map.Entry<Double, Double>>>();
        double sum = 0, globalSum = 0, discount = 0;
        Product p;
        for (Map.Entry<String, Integer> product: listOfProducts) {
            sum = 0;
            p = productRepository.findById(product.getKey()).get();
            if(p.getDiscountType() == DiscountType.PERCENTAGE)
                discount = p.getPrice() * product.getValue() * p.getDiscount() * 0.01;
            else if(p.getDiscountType() == DiscountType.FIXED_AMOUNT)
                discount = p.getDiscount() ;
            sum = p.getPrice() * product.getValue() - discount;
            res.add(new HashMap.SimpleEntry<>(p.getName(), new HashMap.SimpleEntry<>(sum, discount)));
            globalSum += sum;
        }
        res.add(new HashMap.SimpleEntry<>("-1", new HashMap.SimpleEntry<>(globalSum, null)));
        return res;
    }
}

