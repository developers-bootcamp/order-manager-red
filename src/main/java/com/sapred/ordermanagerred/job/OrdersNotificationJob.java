package com.sapred.ordermanagerred.job;

import com.sapred.ordermanagerred.model.Order;
import com.sapred.ordermanagerred.repository.OrderRepository;
import com.sapred.ordermanagerred.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;

public class OrdersNotificationJob {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @Scheduled(cron = "0 0,30 * * * ?")
    public void OrderNotifications() throws NotFoundException {
        System.out.println("OrderNotifications");
        List<Order> ordersToNotify = orderService.getOrdersWithNotificationFlag();
        for (Order order : ordersToNotify) {
            Notification(order);
            order.setNotificationFlag(false);
            orderRepository.save(order);
        }
    }

    private void Notification(Order order) {
        mockNotification(order);
    }

    private void mockNotification(Order order) {
        System.out.println("mock notification sent for order ID: " + order.getId());
    }
}