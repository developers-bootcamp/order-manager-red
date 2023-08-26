package com.sapred.ordermanagerred;

import com.sapred.ordermanagerred.job.OrdersNotificationJob;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableMongoRepositories
@SpringBootApplication
@EnableScheduling
public class OrderManagerRedApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderManagerRedApplication.class, args);
	}

	@Bean
	public OrdersNotificationJob ordersNotificationJob() {
		return new OrdersNotificationJob();}
}
