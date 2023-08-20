package com.sapred.ordermanagerred;

import com.sapred.ordermanagerred.dto.OrderDTO;
import com.sapred.ordermanagerred.mapper.OrderMapper;
import com.sapred.ordermanagerred.model.Order;
import com.sapred.ordermanagerred.model.OrderItem;
import com.sapred.ordermanagerred.model.OrderStatus;
import com.sapred.ordermanagerred.model.Product;
import com.sapred.ordermanagerred.repository.OrderRepository;
import com.sapred.ordermanagerred.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = "${rabbitmq.queue.namePayment}")
    public void consume(OrderDTO message) {
        LOGGER.info(String.format("Received message -> %s ", message));
        if (message.getOrderStatus()== OrderStatus.APPROVED){
            message.setOrderStatus(OrderStatus.PACKING);
            Order order= OrderMapper.INSTANCE.DTOToOrder(message);
            orderRepository.save(order);
        }
        else {
            message.setOrderStatus(OrderStatus.CANCELLED);
            Order order= OrderMapper.INSTANCE.DTOToOrder(message);
            orderRepository.save(order);
            for (OrderItem element : order.getOrderItemsList()) {
                Product product = (Product) productRepository.findOneByIdAndCompanyId(element.getProductId().getId(), order.getCompanyId().getId());
                product.setInventory(product.getInventory()+element.getQuantity());
                productRepository.save(product);
            }
        }
    }

}