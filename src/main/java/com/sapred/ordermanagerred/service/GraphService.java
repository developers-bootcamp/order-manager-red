package com.sapred.ordermanagerred.service;

import com.mongodb.client.AggregateIterable;
import com.sapred.ordermanagerred.dto.TopEmployeeDTO;
import com.sapred.ordermanagerred.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.aggregation.*;
import org.bson.Document;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Sort;
import org.bson.Document;
import java.time.LocalDate;
import java.util.*;

import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Projections.slice;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@Service
public class GraphService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    //    private Document threeMonthsAgo() {
//        return new Document("$date", new Document("$subtract", Arrays.asList(new Document("$date", new Document("$now", new Document())), new Document("$numberLong", 3 * 30 * 24 * 60 * 60))).toJson());
//    }
    public List<TopEmployeeDTO> topEmployee() {
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        Aggregation aggregation = newAggregation(
                match(
                        new Criteria().where("orderStatus").is("DONE").and("auditData.createDate").gte(LocalDate.now().minusMonths(3))
                ),
                group("employeeId").count().as("CountOfDeliveredOrders"),
                lookup("User", "_id.employeeId", "employeeId", "employee"),
                unwind("employee"),
                sort(Sort.by(Sort.Direction.DESC, "CountOfDeliveredOrders")),
                limit(5)
        );
        AggregationResults<Document> groupResults = mongoTemplate.aggregate(aggregation, "Order", Document.class);
        System.out.println(groupResults.getMappedResults());

        List<TopEmployeeDTO> result = new ArrayList<>();
        for (Document document : groupResults) {
            TopEmployeeDTO topEmployeeDTO = new TopEmployeeDTO();
            topEmployeeDTO.setCountOfDeliveredOrders(document.getInteger("CountOfDeliveredOrders"));
            System.out.println("CountOfDeliveredOrders " + topEmployeeDTO.getCountOfDeliveredOrders());
            result.add(topEmployeeDTO);
        }
        return  result;
//
    }
}
