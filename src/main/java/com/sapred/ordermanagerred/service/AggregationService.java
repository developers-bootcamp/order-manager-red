package com.sapred.ordermanagerred.service;

import com.mongodb.BasicDBObject;
import com.sapred.ordermanagerred.dto.MonthProductCountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service

public class AggregationService {
    @Autowired
    MongoTemplate mongoTemplate;

    public List<MonthProductCountDto> getTopProduct() {
        LocalDate dateForLastThreeMonth = LocalDate.now().minusMonths(3);
        Aggregation aggregation = Aggregation.newAggregation(

                match(Criteria.where("companyId.$id").is("11")
                        .and("orderStatus").is("DONE")
                        .and("auditData.createDate").gte(dateForLastThreeMonth)),
                unwind("orderItemsList"),
                project("orderItemsList.productId.$id")
                        .andExpression("month(auditData.createDate)").as("month")
                        .and("orderItemsList.quantity").as("quantity"),
                group(Fields.from(Fields.field("month"), Fields.field("id")))
                        .sum("quantity").as("totalQuantity"),
                group("_id.month")
                        .push(new BasicDBObject("productId", "$_id.id")
                                .append("totalQuantity", "$totalQuantity"))
                        .as("productCount"),
                project()
                        .and("_id").as("month")
                        .and("productCount").as("productCount")
        );

        AggregationResults<MonthProductCountDto> results = mongoTemplate
                .aggregate(aggregation, "Order", MonthProductCountDto.class);
        return results.getMappedResults();
    }
}

