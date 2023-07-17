package com.sapred.ordermanagerred.service;


import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;

import javax.swing.text.Document;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
public class AggregationService {
    @Autowired
    MongoTemplate mongoTemplate;
    public Map<String, Map<String, Integer>> getBestSellingProductsByMonth(String companyId) {
        Aggregation aggregation = Aggregation.newAggregation(
                match(Criteria.where("companyId").is(companyId)
                        .and("orderStatusId").is("DELIVERED")
                        .and("auditData.createdAt").gte(LocalDate.now().minusMonths(3))),
                unwind("orderItemsList"),
                group(Fields.from(
                        Fields.field("month", DateOperators.Month.month("$auditData.createDate").toString()),
                        Fields.field("productName", "$orderItemsList.productName")
                )).count().as("count"),
                group("month").push(new BasicDBObject("productName", "$productName")
                        .append("count", "$count")).as("products"),
                project().and("month").previousOperation().and("products.productName").as("productName")
                        .and("products.count").as("count"),
                sort(Sort.Direction.ASC, "month")
        );

        AggregationResults<Document> results =  mongoTemplate.aggregate(
                aggregation, "Order", Document.class);

        Map<String, Map<String, Integer>> bestSellingProductsMap = new HashMap<>();
        /*for (Document result : results) {
            String month = result.get("_id", Document.class).get("month").toString();
            List<Document> products = result.get("products", List.class);
            Map<String, Integer> productCountMap = new HashMap<>();
            for (Document product : products) {
                String productName = product.getString("productName");
                int count = product.getInte
                ger("count");
                productCountMap.put(productName, count);
            }
            bestSellingProductsMap.put(month, productCountMap);
        }*/

        return bestSellingProductsMap;
    }
}
