package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.dto.ProductCartDTO;
import com.sapred.ordermanagerred.exception.StatusException;
import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.model.Currency;
import com.sapred.ordermanagerred.repository.CompanyRepository;
import com.sapred.ordermanagerred.repository.OrderRepository;
import com.sapred.ordermanagerred.repository.ProductRepository;
import com.sapred.ordermanagerred.repository.UserRepository;
import com.sapred.ordermanagerred.resolver.OrderResolver;
import com.sapred.ordermanagerred.resolver.ProductResolver;
import com.sapred.ordermanagerred.security.JwtToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDate;
import java.util.*;

@ExtendWith({MockitoExtension.class, OrderResolver.class, ProductResolver.class})
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private JwtToken jwtToken;

    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CurrencyConverterService currencyConverterService;
    @Mock
    private MongoTemplate mongoTemplate;

    @BeforeEach
    private void setUp() {
        System.out.print("~~~~~~~~~~~~~~ TEST: ");
    }

    @Test
    public void testCalculateOrderAmount_whenSuccessfully_thenReturnsACorrectResult(Order order, Product product) {
        String token = "token";
        int quantity = 2;
        double price = product.getPrice();
        double discount = product.getDiscount();
        double totalAmount = 50;
        String companyId = "1";

        OrderItem orderItem = OrderItem.builder().quantity(quantity).productId(product).build();
        order.setOrderItemsList(List.of(orderItem));
        order.setTotalAmount(totalAmount);
        Currency currency = Currency.DOLLAR;
        order.setCurrency(currency);
        Company company = Company.builder().id("id").currency(currency).build();

        when(productRepository.findById(orderItem.getProductId().getId())).thenReturn(Optional.of(product));
        when(jwtToken.getRoleIdFromToken(token)).thenReturn(RoleOptions.EMPLOYEE);
        when(jwtToken.getCompanyIdFromToken(token)).thenReturn(companyId);
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(currencyConverterService.convertCurrency("USD", "USD")).thenReturn(1.0);

        List<ProductCartDTO> result = orderService.calculateOrderAmount(token, order);

        assertEquals(result.get(0).getName(), product.getName());
        assertEquals(result.get(0).getAmount(), price * quantity - (price * quantity * discount * 0.01));
        assertEquals(result.get(0).getDiscount(), price * quantity * discount * 0.01);
        assertEquals(result.get(0).getQuantity(), quantity);
        assertEquals(result.get(1).getName(), "Total");
        assertEquals(result.get(1).getAmount(), totalAmount + (price * quantity - (price * quantity * discount * 0.01)));

        System.out.println("✅ testCalculateOrderAmount");
    }

    @Test
    public void testUpdateOrder_whenStatusTransitionIsValid_thenOrderIsUpdated() {
        String token = "token";
        String orderId = "order-id";

        Order currentOrder = new Order();
        currentOrder.setId(orderId);
        currentOrder.setOrderStatus(OrderStatus.NEW);

        Order updateOrder = new Order();
        updateOrder.setId(orderId);
        updateOrder.setOrderStatus(OrderStatus.APPROVED);


        when(orderRepository.findById(orderId)).thenReturn(Optional.of(currentOrder));

        orderService.updateOrder(token, updateOrder);

        verify(orderRepository).save(updateOrder);
    }

    @Test
    public void testUpdateOrder_whenStatusTransitionIsInvalid_thenExceptionIsThrown() {

        String token = "token";
        String orderId = "order-id";

        Order currentOrder = new Order();
        currentOrder.setId(orderId);
        currentOrder.setOrderStatus(OrderStatus.APPROVED);

        Order updateOrder = new Order();
        updateOrder.setId(orderId);
        updateOrder.setOrderStatus(OrderStatus.DELIVERED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(currentOrder));

        assertThrows(StatusException.class, () -> orderService.updateOrder(token, updateOrder));
    }
//
//    @Test
//    public void testCreateOrder_withValidStatus_thenOrderIsCreated() {
//
//        String token = "token";
//        String companyId = "company-id";
//        String employeeId = "employee-id";
//
//        Order order = new Order();
//        order.setOrderStatus(OrderStatus.NEW);
//
//        Company company = new Company();
//        company.setId(companyId);
//
//        User employee = new User();
//        employee.setId(employeeId);
//
//        when(jwtToken.getCompanyIdFromToken(token)).thenReturn(companyId);
//        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
//        when(jwtToken.getUserIdFromToken(token)).thenReturn(employeeId);
//        when(userRepository.findById(employeeId)).thenReturn(Optional.of(employee));
//
//        when(orderRepository.save(any())).thenReturn(order);
//
//        // Call the method and assert
//        String orderId = orderService.createOrder(token, order);
//
//        assertEquals(orderId, order.getId());
//        assertEquals(order.getCompanyId(), company);
//        assertEquals(order.getEmployeeId(), employee);
//        assertNotNull(order.getAuditData());
//        assertEquals(order.getOrderStatus(), OrderStatus.NEW);
//
//        verify(orderRepository).save(order);
//    }

    @Test
    public void testCreateOrder_withInvalidStatus_thenExceptionIsThrown() {

        String token = "token";

        Order order = new Order();
        order.setOrderStatus(OrderStatus.PACKING);

        assertThrows(StatusException.class, () -> orderService.createOrder(token, order));

        verifyNoInteractions(companyRepository, userRepository, orderRepository);
    }


    @Test
    public void testGetOrdersByFilters_withValidInput_thenQueryExecutedAndResultsReturned() {

        String token = "token";
        String companyId = "company-id";
        int pageNumber = 1;
        int pageSize = 10;

        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("filterKey", "filterValue");

        List<Order> expectedResults = Collections.singletonList(new Order());

        when(jwtToken.getCompanyIdFromToken(token)).thenReturn(companyId);
        when(mongoTemplate.find(any(Query.class), eq(Order.class))).thenReturn(expectedResults);

        List<Order> results = orderService.getOrdersByFilters(filterMap, token,  pageNumber);

        assertEquals(results, expectedResults);

        verify(jwtToken).getCompanyIdFromToken(token);
        verify(mongoTemplate).find(any(Query.class), eq(Order.class));
    }
    }

