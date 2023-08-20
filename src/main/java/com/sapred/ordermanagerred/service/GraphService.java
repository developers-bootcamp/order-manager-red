package com.sapred.ordermanagerred.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.sapred.ordermanagerred.dto.MonthProductCountDto;
import com.sapred.ordermanagerred.model.Order;
import com.sapred.ordermanagerred.security.JwtToken;
import jdk.security.jarsigner.JarSignerException;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.io.ObjectInputStream;

import com.sapred.ordermanagerred.dto.TopEmployeeDTO;
import com.sapred.ordermanagerred.model.Order;
import com.sapred.ordermanagerred.model.OrderStatus;
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
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@Slf4j
public class GraphService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private JwtToken jwtToken;

    public List<TopEmployeeDTO> topEmployee() {
        log.info("Retrieving top employees based on delivered orders count");

        Aggregation aggregation = newAggregation(
                match(Criteria.where("auditData.createDate").gte(LocalDate.now().minusMonths(3))),
                match(Criteria.where("orderStatus").is(OrderStatus.APPROVED)),
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

    public List<MonthProductCountDto> getTopProduct(String token, int rangeOfMonths) {
        String companyId = jwtToken.getCompanyIdFromToken(token);
        OrderStatus statusDone = OrderStatus.DELIVERED;
        LocalDate dateForLastMonth = LocalDate.now().minusMonths(rangeOfMonths);
        log.info("Retrieving the company's products with a count of their popularity according to the orders");

        Aggregation aggregation = Aggregation.newAggregation(

                match(Criteria.where("companyId.$id").is(companyId)
                        .and("orderStatus").is(statusDone)
                        .and("auditData.createDate").gte(dateForLastMonth)),
                unwind("orderItemsList"),
                project("orderItemsList.productId")
                        .andExpression("month(auditData.createDate)").as("month")
                        .and("orderItemsList.quantity").as("quantity"),
                group(Fields.from(Fields.field("month"), Fields.field("productId")))
                        .sum("quantity").as("totalQuantity"),
                group("_id.month")
                        .push(new BasicDBObject("productId", "$_id.productId")
                                .append("totalQuantity", "$totalQuantity")).as("productCount"),
                project()
                        .and("_id").as("month")
                        .and("productCount").as("productCount")
        );

        AggregationResults<MonthProductCountDto> results = mongoTemplate
                .aggregate(aggregation, "Order", MonthProductCountDto.class);

        List<MonthProductCountDto> topProduct = results.getMappedResults();

        log.info("Top products retrieved: {}", topProduct);

        return topProduct;
    }

    public Map<Month, Map<Integer, Integer>> getStatus(String token, Integer monthAmount) {
        String companyId = jwtToken.getCompanyIdFromToken(token);
        LocalDate currentDate = LocalDate.now();
        LocalDate MonthsAgo = currentDate.minusMonths(monthAmount);
        Aggregation aggregation = newAggregation(
                match(Criteria.where("auditData.createDate").gte(MonthsAgo)),
                match(Criteria.where("companyId").is(companyId)),
                project()
                        .andExpression("month(auditData.createDate)").as("month")
                        .and("orderStatusId").as("orderStatusId"),
                group("month")
                        .sum(ConditionalOperators.when(ComparisonOperators.valueOf("orderStatusId").equalToValue("0")).then(1).otherwise(0)).as("cancelled")
                        .sum(ConditionalOperators.when(ComparisonOperators.valueOf("orderStatusId").equalToValue("1")).then(1).otherwise(0)).as("delivered"),
                project()
                        .and("_id").as("month")
                        .and("cancelled").as("cancelled")
                        .and("delivered").as("delivered")
        );
        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "Order", Document.class);
        List<Document> mappedResults = results.getMappedResults();
        System.out.println("Results" + results);
        System.out.println("mappedResults" + mappedResults);

        Map<Month, Map<Integer, Integer>> resultMap = new HashMap<>();
        for (Document mappedResult : mappedResults) {
            Month month = Month.of(mappedResult.getInteger("month"));
            int cancelled = mappedResult.getInteger("cancelled", 0);
            int delivered = mappedResult.getInteger("delivered", 0);
            Map<Integer, Integer> tempMap = new HashMap<>();
            tempMap.put(cancelled, delivered);
            resultMap.put(month, tempMap);
        }
        return resultMap;
    }
}
