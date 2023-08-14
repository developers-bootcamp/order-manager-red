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
import java.time.LocalDate;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service

public class GraphService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private JwtToken jwtToken;
    @Autowired
    private ObjectMapper objectMapper;


    public List<MonthProductCountDto> getTopProduct(String token, int rangeOfMonths) {
        String companyId = jwtToken.getCompanyIdFromToken(token);
        Order.StatusOptions statusDone = Order.StatusOptions.DONE;
        LocalDate dateForLastMonth = LocalDate.now().minusMonths(rangeOfMonths);
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
                                .append("totalQuantity", "$totalQuantity"))
                        .as("productCount"),
                project()
                        .and("_id").as("month")
                        .and("productCount").as("productCount")
        );
        AggregationResults<Document> result = mongoTemplate.aggregate(
                aggregation,
                "Order",
                Document.class
        );
        System.out.println(result.getMappedResults());

        AggregationResults<MonthProductCountDto> results = mongoTemplate
                .aggregate(aggregation, "Order", MonthProductCountDto.class);
        return results.getMappedResults();
    }
}
