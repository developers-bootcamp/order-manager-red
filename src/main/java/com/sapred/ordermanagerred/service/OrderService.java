package com.sapred.ordermanagerred.service;


import com.sapred.ordermanagerred.dto.ProductCartDTO;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        AuditData d = AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build();
        List<Order> orders = new ArrayList<Order>();
        for (int i = 200; i < 500; i++) {
            if (i % 3 == 0)
                orders.add(new Order(Integer.toString(i), "employee", "customer", i * 2, new Company("11", "333", 88, d), new AuditData(LocalDate.now(), new Date(i * 1000).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()), "1"));
            else if (i % 3 == 1)
                orders.add(new Order(Integer.toString(i), "custumer", "customer", i * 2, new Company("22", "333", 88, d), new AuditData(LocalDate.now(), new Date(i * 1000).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()), "2"));
            else
                orders.add(new Order(Integer.toString(i), "111", "customer", i * 2, new Company("11", "333", 88, d), new AuditData(LocalDate.now(), new Date(i * i).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()), "3"));
        }
        orderRepository.saveAll(orders);

    }

    public void fillProducts() {
        for (int i = 1; i < 10; i++) {
            AuditData d = AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build();
            List<Order> orders = new ArrayList<Order>();
            ProductCategory pc = new ProductCategory(i + "", "name" + i, "desc" + i,
                    companyRepository.findById("1").get(), AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build());
            productCategoryRepository.save(pc);
            Product p = new Product(i + "", "aaa", "aaa", 40, 50,
                    DiscountType.PERCENTAGE, pc, 4,
                    companyRepository.findById("1").get(), AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build());
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
        else if (product.getDiscountType() == DiscountType.FIXED_AMOUNT)
            discount = product.getDiscount();
        sum = product.getPrice() * orderItem.getQuantity() - discount;
        productCartDTO = ProductCartDTO.builder().name(product.getName())
                .amount(sum).discount(discount).quantity(orderItem.getQuantity()).build();
        listOfCart.add(productCartDTO);
        listOfCart.add(ProductCartDTO.builder().name("Total").amount(order.getTotalAmount() + sum).build());
        return listOfCart;
    }
}

