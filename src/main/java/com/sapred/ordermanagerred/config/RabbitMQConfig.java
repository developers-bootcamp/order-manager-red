package com.sapred.ordermanagerred.config;




import ch.qos.logback.classic.pattern.MessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//RabbitMqDirectExchangeConfig
@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_TAXI_DIRECT = "x.taxi.direct";
    public static final String ROUTING_KEY_TAXI_NORMAL_SMALL = "taxi.normal.small";
    public static final String ROUTING_KEY_TAXI_ECO_SMALL = "taxi.eco.small";
    public static final String ROUTING_KEY_TAXI_NORMAL_LARGE = "taxi.normal.large";
    public static final String ROUTING_KEY_TAXI_ECO_LARGE = "taxi.eco.large";

    @Bean
    public DirectExchange exchangeTaxiDirect() {
        return new DirectExchange(EXCHANGE_TAXI_DIRECT);
    }

    @Bean
    public Declarables directExchangeBindings(
            DirectExchange exchangeTaxiDirect,
            Queue queueTaxiNormalSmall,
            Queue queueTaxiEcoSmall,
            Queue queueTaxiNormalLarge,
            Queue queueTaxiEcoLarge) {
        return new Declarables(
                BindingBuilder.bind(queueTaxiNormalSmall).to(exchangeTaxiDirect).with(ROUTING_KEY_TAXI_NORMAL_SMALL),
                BindingBuilder.bind(queueTaxiEcoSmall).to(exchangeTaxiDirect).with(ROUTING_KEY_TAXI_ECO_SMALL),
                BindingBuilder.bind(queueTaxiNormalLarge).to(exchangeTaxiDirect).with(ROUTING_KEY_TAXI_NORMAL_LARGE),
                BindingBuilder.bind(queueTaxiEcoLarge).to(exchangeTaxiDirect).with(ROUTING_KEY_TAXI_ECO_LARGE)
        );
    }
}

/*import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.queue.name}")
    private String queue;
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;
    @Bean
    public Queue queue() {
        return new Queue(queue);
    }
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }
    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(routingKey);
    }
    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate=new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
    @Bean
    public MessageConverter messageConverter()
    {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

        return new Jackson2JsonMessageConverter(mapper);
    }
}*/


