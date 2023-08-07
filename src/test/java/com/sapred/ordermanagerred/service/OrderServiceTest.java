package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.dto.ProductCartDTO;
import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.OrderRepository;
import com.sapred.ordermanagerred.repository.ProductRepository;
import com.sapred.ordermanagerred.resolver.OrderResolver;
import com.sapred.ordermanagerred.resolver.ProductResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


import java.util.List;
import java.util.Optional;

@ExtendWith({MockitoExtension.class, OrderResolver.class, ProductResolver.class})
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    private void setUp() {
        System.out.print("~~~~~~~~~~~~~~ TEST: ");
    }

    @Test
    public void testCalculateOrderAmount_whenSuccessfully_thenReturnsACorrectResult(Order order, Product product) {
        int quantity = 2;
        double price = product.getPrice();
        double discount = product.getDiscount();
        double totalAmount = 50;

        OrderItem orderItem = OrderItem.builder().quantity(quantity).productId(product).build();
        order.setOrderItemsList(List.of(orderItem));
        order.setTotalAmount(totalAmount);

        when(productRepository.findById(orderItem.getProductId().getId())).thenReturn(Optional.of(product));

        List<ProductCartDTO> result = orderService.calculateOrderAmount(order);

        System.out.println(result);
        assertEquals(result.get(0).getName(), product.getName());
        assertEquals(result.get(0).getAmount(), price * quantity - (price * quantity * discount * 0.01));
        assertEquals(result.get(0).getDiscount(), price * quantity * discount * 0.01);
        assertEquals(result.get(0).getQuantity(), quantity);
        assertEquals(result.get(1).getName(), "Total");
        assertEquals(result.get(1).getAmount(), totalAmount + (price * quantity - (price * quantity * discount * 0.01)));

        System.out.println("✅ testCalculateOrderAmount");
    }

}