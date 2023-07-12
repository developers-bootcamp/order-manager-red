package com.sapred.ordermanagerred.service;


import com.sapred.ordermanagerred.dto.ProductCartDTO;
import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.model.Currency;
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
                    DiscountType.PERCENTAGE, pc, 4,
                    companyRepository.findById("1").get(), new AuditData(new Date(), new Date()));
            productRepository.save(p);
        }
    }

    public List<ProductCartDTO> calculateOrderAmount(Order order) {
        List<ProductCartDTO> listOfCart = new ArrayList<ProductCartDTO>();
        double sum = 0, totalSum = 0, discount = 0, totalDiscount = 0;
        int totalQuantity = 0;
        Product product;
        for (OrderItem orderItem : order.getOrderItemsList()) {
            sum = 0;
            product = productRepository.findById(orderItem.getProductId().getId()).get();
            if (product.getDiscountType() == DiscountType.PERCENTAGE)
                discount = product.getPrice() * orderItem.getQuantity() * product.getDiscount() * 0.01;
            else if (product.getDiscountType() == DiscountType.FIXED_AMOUNT)
                discount = product.getDiscount();
            sum = product.getPrice() * orderItem.getQuantity() - discount;
            listOfCart.add(new ProductCartDTO(product.getName(), sum, discount, orderItem.getQuantity()));
            totalSum += sum;
            totalDiscount += discount;
            totalQuantity += orderItem.getQuantity();
        }
        listOfCart.add(new ProductCartDTO("Total", totalSum, totalDiscount, totalQuantity));
        return listOfCart;
    }

    public List<Currency> getCurrencies() {
        List<Currency> currencyList = new ArrayList<>();
        return Arrays.stream(Currency.values()).toList();
    }


}

