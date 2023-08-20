package com.sapred.ordermanagerred;

import com.sapred.ordermanagerred.dto.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = "${rabbitmq.queue.namePayment}")
    public void consume(OrderDTO message) {
        LOGGER.info(String.format("Received message -> %s ", message));
    }

}