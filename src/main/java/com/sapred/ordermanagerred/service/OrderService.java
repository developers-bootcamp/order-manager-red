package com.sapred.ordermanagerred.service;


import com.sapred.ordermanagerred.dto.ProductCartDTO;
import com.sapred.ordermanagerred.exception.MismatchData;
import com.sapred.ordermanagerred.exception.StatusException;
import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.CompanyRepository;
import com.sapred.ordermanagerred.repository.OrderRepository;
import com.sapred.ordermanagerred.repository.ProductCategoryRepository;
import com.sapred.ordermanagerred.repository.ProductRepository;
import com.sapred.ordermanagerred.security.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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

        Page<Order> pageOrders = orderRepository.findByCompanyId_IdAndOrderStatusAndEmployeeId(companyId, statusId, userId, pageable);
        return pageOrders.getContent();
    }

    public String createOrder(String token, Order order) {
        String companyId = jwtToken.getCompanyIdFromToken(token);
//        if (order.getCompanyId().getId() != companyId)
//            throw new MismatchData("the company id is not match to the order's company id");

//        if (order.getOrderStatus() != Order.StatusOptions.NEW || order.getOrderStatus() != Order.StatusOptions.APPROVED)
//            throw new StatusException("can't create order where status is not NEW or APPROVED ");
//        return orderRepository.save(order).getId();

        if (order.getOrderStatus() == Order.StatusOptions.NEW || order.getOrderStatus() == Order.StatusOptions.APPROVED)
            return orderRepository.save(order).getId();
        throw new StatusException("can't create order where status is not NEW or APPROVED ");

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
}

