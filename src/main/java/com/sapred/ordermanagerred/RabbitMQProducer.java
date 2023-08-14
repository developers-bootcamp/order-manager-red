package com.sapred.ordermanagerred;


/*import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
//import pl.akolata.model.OrderTaxiMessage;

import static com.sapred.ordermanagerred.config.RabbitMQConfig.*;
//import static pl.akolata.util.MessagingLoggingUtil.logSendMessage;

//DirectExchangeProducer
@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage() {
        sendMsg(ROUTING_KEY_TAXI_NORMAL_SMAL);
        sendMsg(ROUTING_KEY_TAXI_ECO_LARGE);
        sendMsg("not-matching");
    }

    private void sendMsg(String routingKey) {
        OrderTaxiMessage message = new OrderTaxiMessage();
        rabbitTemplate.convertAndSend(EXCHANGE_TAXI_DIRECT, routingKey, message);
        //logSendMessage(EXCHANGE_TAXI_DIRECT, routingKey, message);
    }

}*/

import com.sapred.ordermanagerred.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class RabbitMQProducer {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;
    private static  final Logger LOGGER= LoggerFactory.getLogger(RabbitMQProducer.class);
    private RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public void sendMessage(Order message){
//        LOGGER.info(String.format("message sent: ",t);
        rabbitTemplate.convertAndSend(exchange,routingKey,message);
    }
}