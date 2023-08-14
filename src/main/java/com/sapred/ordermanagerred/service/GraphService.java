package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.dto.TopEmployeeDTO;
import com.sapred.ordermanagerred.model.Order;
import com.sapred.ordermanagerred.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@Slf4j
public class GraphService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<TopEmployeeDTO> topEmployee() {
        log.info("Retrieving top employees based on delivered orders count");

        Aggregation aggregation = newAggregation(
                match(Criteria.where("auditData.createDate").gte(LocalDate.now().minusMonths(3))),
                match(Criteria.where("orderStatus").is(Order.StatusOptions.APPROVED)),
                group("employeeId").count().as("countOfDeliveredOrders"),
                project("countOfDeliveredOrders").and("_id").as("employee"),
                sort(Sort.Direction.DESC, "countOfDeliveredOrders"),
                limit(5)
        );

        AggregationResults<TopEmployeeDTO> result = mongoTemplate.aggregate(
                aggregation, "Order", TopEmployeeDTO.class
        );

        List<TopEmployeeDTO> topEmployees = result.getMappedResults();

        log.info("Top employees retrieved: {}", topEmployees);

        return topEmployees;
    }
}
