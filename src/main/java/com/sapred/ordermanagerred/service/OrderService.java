package com.sapred.ordermanagerred.service;
import com.sapred.ordermanagerred.dto.OrderDTO;
import com.sapred.ordermanagerred.dto.ProductCartDTO;
import com.sapred.ordermanagerred.exception.NoPermissionException;
import com.sapred.ordermanagerred.exception.StatusException;
import com.sapred.ordermanagerred.mapper.OrderMapper;
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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
//    @Autowired
//    private RabbitMQProducer rabbitMQProducer;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CurrencyConverterService currencyConverterService;

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


    public List<Order> getOrdersByFilters(Map<String, Object> filterMap, String token, int pageNumber, Criteria criteria, String sortParameter) {

        String companyId = jwtToken.getCompanyIdFromToken(token);

        Map<String, Object> reference = new HashMap<>();
        reference.put("$ref", "Company");
        reference.put("$id", companyId);
        filterMap.put("companyId", reference);
        log.info("filtermap {}", filterMap);

        // Iterate through the filter map and construct filter for each entry
        for (Map.Entry<String, Object> entry : filterMap.entrySet()) {
            String filterName = entry.getKey();
            Object filterValue = entry.getValue();

            criteria = criteria.and(filterName).is(filterValue);
        }


        Query query = new Query(criteria);

        int skip = (pageNumber - 1) * pageSize;
        query.skip(skip);
        query.limit(pageSize);

        Sort.Order sortOrder = new Sort.Order(Sort.Direction.DESC, sortParameter);
        query.with(Sort.by(sortOrder));
        log.info("Executing query: {}", query);
        return mongoTemplate.find(query, Order.class);

    }

    public List<Order> getOrdersFilterByFailedStatus(Map<String, Object> filterMap, String token, int pageNumber, String sortParameter) {

        Criteria criteria = new Criteria();
        List<String> filterValue1 = Collections.singletonList(OrderStatus.CANCELLED.toString());
        criteria = criteria.and(Order.Fields.orderStatus).in(filterValue1);

        return getOrdersByFilters(filterMap, token, pageNumber, criteria, sortParameter);

    }

    public List<Order> getOrdersFilterByStatuses(Map<String, Object> filterMap, String token, int pageNumber, String sortParameter) {

        Criteria criteria = new Criteria();

        List<String> filterValue1 = Arrays.asList(OrderStatus.NEW.toString(), OrderStatus.APPROVED.toString(), OrderStatus.PACKING.toString(), OrderStatus.CHARGING.toString(), OrderStatus.DELIVERED.toString());
        criteria = criteria.and(Order.Fields.orderStatus).in(filterValue1);

        return getOrdersByFilters(filterMap, token, pageNumber, criteria, sortParameter);

    }

    @Transactional
    public String createOrder(String token, Order order) {
        String companyId = jwtToken.getCompanyIdFromToken(token);
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new NotFoundException("Company not found"));
        order.setCompanyId(company);
        String employeeId = jwtToken.getUserIdFromToken(token);
        User employee = userRepository.findById(employeeId).orElseThrow(() -> new NotFoundException("Employee not found"));
        order.setEmployeeId(employee);
        AuditData auditData = new AuditData(LocalDate.now(), null);
        order.setAuditData(auditData);

        if (order.getOrderStatus() != OrderStatus.NEW && order.getOrderStatus() != OrderStatus.APPROVED) {
            log.error("Cannot create order with status '{}'", order.getOrderStatus());
            throw new StatusException("Cannot create order with status other than NEW or APPROVED");
        }
        order.setOrderStatus(OrderStatus.CHARGING);
        String orderId = orderRepository.save(order).getId();
        order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        if (orderId != null) { //  order.getOrderItemsList().stream().
            for (OrderItem element : order.getOrderItemsList()) {
                Product product = productRepository.findOneByIdAndCompanyId(element.getProductId().getId(), company.getId());
                if (product.getInventory() - element.getQuantity() < 0) {
                    order.setOrderStatus(OrderStatus.CANCELLED);
                    orderRepository.save(order);
                    break;//throw exception
                }
                product.setInventory(product.getInventory() - element.getQuantity());
                productRepository.save(product);
            }
//            rabbitMQProducer.sendMessage(OrderMapper.INSTANCE.orderToDTO(order));
        }
        log.info("Order created with ID '{}'", orderId);
        return orderId;
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

    @SneakyThrows
    public List<ProductCartDTO> calculateOrderAmount(String token, Order order) {
        log.info("Calculating order amount");

        RoleOptions roleFromToken = jwtToken.getRoleIdFromToken(token);
        if (roleFromToken == RoleOptions.CUSTOMER)
            throw new NoPermissionException("You do not have the appropriate permission to calculate order");

        String companyId = jwtToken.getCompanyIdFromToken(token);
        String fromCurrency = companyRepository.findById(companyId).get().getCurrency().getCode();
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

    public void updateOrder(String token, Order updateOrder) {
        Order currentOrder = orderRepository.findById(updateOrder.getId()).orElseThrow(() -> new NotFoundException("can't update not found order"));
        if ((currentOrder.getOrderStatus() == OrderStatus.NEW && updateOrder.getOrderStatus() != OrderStatus.APPROVED) || (currentOrder.getOrderStatus() == OrderStatus.PACKING && (updateOrder.getOrderStatus() != OrderStatus.DELIVERED || updateOrder.getOrderStatus() != OrderStatus.CANCELLED))) {
            log.error("can't update from status to status " + currentOrder.getOrderStatus() + " to status " + updateOrder.getOrderStatus());
            throw new StatusException("can't update from status " + currentOrder.getOrderStatus() + " to status " + updateOrder.getOrderStatus());
        }
        if ((currentOrder.getOrderStatus() != OrderStatus.NEW && currentOrder.getOrderStatus() != OrderStatus.PACKING)) {
            throw new StatusException("can't update from status " + currentOrder.getOrderStatus());
        }
        orderRepository.save(updateOrder);
        log.info("update order items");
    }

    public void processOrder(OrderDTO orderDTO) {
        if (orderDTO.getOrderStatus() == OrderStatus.APPROVED) {
            orderDTO.setOrderStatus(OrderStatus.PACKING);
            Order order = OrderMapper.INSTANCE.DTOToOrder(orderDTO);
            order.setNotificationFlag(true);
            orderRepository.save(order);
        } else {
            orderDTO.setOrderStatus(OrderStatus.CANCELLED);
            Order order = OrderMapper.INSTANCE.DTOToOrder(orderDTO);
            order.setNotificationFlag(true);
            orderRepository.save(order);
            for (OrderItem element : order.getOrderItemsList()) {
                Product product = productRepository.findOneByIdAndCompanyId(element.getProductId().getId(), order.getCompanyId().getId());
                product.setInventory(product.getInventory() + element.getQuantity());
                productRepository.save(product);
            }
        }
    }

    public List<Order> getOrdersWithNotificationFlag() {
        return orderRepository.findByNotificationFlag(true);
    }
}