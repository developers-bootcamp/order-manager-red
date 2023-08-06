package com.sapred.ordermanagerred.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.sapred.ordermanagerred.dto.MonthProductCountDto;
import com.sapred.ordermanagerred.dto.ProductCountDto;
import com.sapred.ordermanagerred.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
//import org.springframework.data.mongodb.core.aggregation.AggregationFields;
import org.springframework.stereotype.Service;
import org.bson.Document;

//import javax.swing.text.Document;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service

public class AggregationService {
    @Autowired
    MongoTemplate mongoTemplate;


    public void/*Map<String, Map<String, Integer>>*/ getBestSellingProductsByMonth(/*String companyId*/) {
        LocalDate dateForLastThreeMonth = LocalDate.now().minusMonths(3);
        /*MatchOperation matchOperation=match(Criteria.where("companyId.$id").is("11")
                .and("orderStatus").is("DONE")
                .and("auditData.createDate").gte(dateForLastThreeMonth));
        UnwindOperation unwindOperation=unwind("orderItemsList");
        GroupOperation groupOperation=group("month(auditData.createDate)")
                .sum("$orderItemsList.productId").as("sumProduct");*/
        //GroupOperation groupByProductAndCount=group("$orderItemsList.productId")
        //        .sum("orderItemsList.amount").as("totalAmount");
        Aggregation aggregation = Aggregation.newAggregation(
                match(Criteria.where("companyId.$id").is("11").and
                        ("orderStatus").is("DONE").and
                        ("auditData.createDate").gte(dateForLastThreeMonth)),
                unwind("orderItemsList"),
                //group("orderItemsList.productId.$id").sum("orderItemsList.quantity").as("totalAmount"),
                //group("auditData.createDate"),
                group(Fields.from(
                        Fields.field("createDate", "auditData.createDate"),
                        Fields.field("orderItemsList.productId.$id"))
                ).sum("orderItemsList.quantity").as("totalAmount"),

                //project("totalAmount").and("orderItemsList.productId.$id").previousOperation(),
                //        .andExpression("month(auditData.createDate)").as("month")
                //.and("orderItemsList.productId.name").as("productName")
                //.and("$totalAmount").as("amount")
                /*group(Fields.from(
                        Fields.field("month", "month(auditData.createDate)"),
                        Fields.field("productName", "orderItemsList.productId.$id"))
                ).sum("orderItemsList.amount").as("orderItemsList.amount")*/
                /*group("$_id.month").push(new BasicDBObject("productName", "$productName")
                        .append("totalAmount", "$totalAmount")).as("products")*/
                /*project().and("createDate").previousOperation()
                        .and("id").as("id")
                        .and("totalAmount").as("totalAmount"),*/
                sort(Sort.Direction.DESC, "totalAmount")
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(
                aggregation, "Order", Document.class);
        List<Document> mappedResults = results.getMappedResults();
        System.out.println("mappedResults" + mappedResults);


        //return results;
       /* Map<String, Map<String, Integer>> bestSellingProductsMap = new HashMap<>();
        for (Document result : results) {
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
        }
*/


    /*public List<MonthProductCountDto> getTopSoldProductsForLastThreeMonths() {
        public List<BasicDBObject> getTopSoldProductsForLastThreeMonths () {
            LocalDate startDate = LocalDate.now().minusMonths(3);
            LocalDate endDate = LocalDate.now();

            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("auditData.createDate").gte(startDate).lt(endDate)),
                    Aggregation.lookup("orderItem", "orderItemsList", "_id", "orderItems"),
                    Aggregation.unwind("orderItems"),
                    Aggregation.group("orderItems.productId")
                            .first("orderItems.productId.name").as("productName")
                            .sum("orderItems.amount").as("count"),
                    Aggregation.sort(Sort.Direction.DESC, "count"),
                    Aggregation.project("productName", "count")
                    //.andExpression("auditData.createDate.getMonth()").as("month"),
                    //Aggregation.sort(Sort.Direction.ASC, "month"),
                    Aggregation.group("month").push(new BasicDBObject("productName", "$productName")
                            .append("count", "$count")).as("products")
            );

            AggregationResults<BasicDBObject> results = mongoTemplate.aggregate(aggregation, "order", BasicDBObject.class);

            List<BasicDBObject> mappedResults = results.getMappedResults();
            return results.getMappedResults();

            List<MonthProductCountDto> monthProductCountDtoList = new ArrayList<>();

            for (BasicDBObject result : mappedResults) {
                MonthProductCountDto monthProductCountDto = new MonthProductCountDto();
                monthProductCountDto.setMonth(Month.of((Integer) result.get("month")));

                List<BasicDBObject> products = (List<BasicDBObject>) result.get("products");
                List<ProductCountDto> productCountDtoList = new ArrayList<>();

                for (BasicDBObject product : products) {
                    ProductCountDto productCountDto = new ProductCountDto();
                    productCountDto.setProductName((String) product.get("productName"));
                    productCountDto.setCount((Integer) product.get("count"));
                    productCountDtoList.add(productCountDto);
                }

                monthProductCountDto.setProducts(productCountDtoList);
                monthProductCountDtoList.add(monthProductCountDto);
            }

            return monthProductCountDtoList;
*/
    }
}

