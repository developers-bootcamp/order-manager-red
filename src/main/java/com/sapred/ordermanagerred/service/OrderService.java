package com.sapred.ordermanagerred.service;


import com.sapred.ordermanagerred.dto.ProductCartDTO;
import com.sapred.ordermanagerred.exception.NoPermissionException;
import com.sapred.ordermanagerred.exception.StatusException;
import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.*;
import com.sapred.ordermanagerred.security.JwtToken;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private JwtToken jwtToken;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private CurrencyConverterService currencyConverterService;

    @Autowired
    private UserRepository userRepository;

    @Value("${pageSize}")
    private int pageSize;

    public List<Order> getOrders(String token, String statusId, int pageNumber, String userId) {
        log.info("Retrieving orders with status '{}' for user ID '{}' and page number '{}'", statusId, userId, pageNumber);

        String companyId = jwtToken.getCompanyIdFromToken(token);

        Sort.Order sortOrder = Sort.Order.asc("auditData.updateDate");
        Sort sort = Sort.by(sortOrder);

        Pageable pageable = PageRequest.of(pageNumber, pageSize /* pageSize parameter omitted */, sort);

        Page<Order> pageOrders = orderRepository.findByCompanyId_IdAndOrderStatusAndEmployeeId(companyId, statusId, userId, pageable);
        List<Order> orders = pageOrders.getContent();

        log.info("Retrieved {} orders", orders.size());

        return orders;
    }

    public String createOrder(String token, Order order) {
        log.info("Creating order");

        String companyId = jwtToken.getCompanyIdFromToken(token);
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new NotFoundException("Company not found"));
        order.setCompanyId(company);
        String employeeId = jwtToken.getUserIdFromToken(token);
        User employee = userRepository.findById(employeeId).orElseThrow(() -> new NotFoundException("Employee not found"));
        order.setEmployeeId(employee);
        AuditData auditData = new AuditData(LocalDate.now(), null);
        order.setAuditData(auditData);

        if (order.getOrderStatus() != Order.StatusOptions.NEW && order.getOrderStatus() != Order.StatusOptions.APPROVED) {
            log.error("Cannot create order with status '{}'", order.getOrderStatus());
            throw new StatusException("Cannot create order with status other than NEW or APPROVED");
        }

        String orderId = orderRepository.save(order).getId();
        log.info("Order created with ID '{}'", orderId);
        return orderId;
    }

    public void fillProducts() {
        log.info("Filling products");

        for (int i = 1; i < 10; i++) {
            AuditData d = AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build();
            List<Order> orders = new ArrayList<Order>();
            ProductCategory pc = new ProductCategory(String.valueOf(i), "name" + i, "desc" + i, companyRepository.findById("1").get(), AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build());
            productCategoryRepository.save(pc);
            Product p = new Product(String.valueOf(i), "aaa", "aaa", 40, 50, DiscountType.PERCENTAGE, pc, 4, companyRepository.findById("1").get(), AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build());
            productRepository.save(p);
        }

        log.info("Products filled");
    }

    @SneakyThrows
    public List<ProductCartDTO> calculateOrderAmount(String token, Order order) {
        log.info("Calculating order amount");

        RoleOptions roleFromToken = jwtToken.getRoleIdFromToken(token);
        if (roleFromToken == RoleOptions.CUSTOMER)
            throw new NoPermissionException("You do not have the appropriate permission to calculate order");

        Company company = companyRepository.findById(order.getCompanyId().getId()).get();
        if (company == null) throw new NotFoundException("the company not exist in data");

        String fromCurrency = company.getCurrency().getCode();
        String toCurrency = order.getCurrency().getCode();
        double rate = currencyConverterService.convertCurrency(fromCurrency, toCurrency);

        List<ProductCartDTO> listOfCart = new ArrayList<>();
        double sum = 0, discount = 0;
        OrderItem orderItem = order.getOrderItemsList().get(order.getOrderItemsList().size() - 1);
        Product product = productRepository.findById(orderItem.getProductId().getId()).get();
        double price = product.getPrice() * rate;
        if (product.getDiscountType() == DiscountType.PERCENTAGE)
            discount = price * orderItem.getQuantity() * product.getDiscount() * 0.01;
        else if (product.getDiscountType() == DiscountType.FIXED_AMOUNT) discount = product.getDiscount() * rate;
        sum = price * orderItem.getQuantity() - discount;
        ProductCartDTO productCartDTO = ProductCartDTO.builder().name(product.getName()).amount(sum).discount(discount).quantity(orderItem.getQuantity()).build();
        listOfCart.add(productCartDTO);
        listOfCart.add(ProductCartDTO.builder().name("Total").amount(order.getTotalAmount() + sum).build());
        log.info("Order amount calculated");
        return listOfCart;
    }
}
