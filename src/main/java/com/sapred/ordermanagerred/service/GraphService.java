package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.dto.TopEmployeeDTO;
import com.sapred.ordermanagerred.model.User;
import com.sapred.ordermanagerred.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.aggregation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class GraphService {
//    @Autowired
//    private OrderRepository orderRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public void topEmployee() {
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
            Aggregation aggregation = newAggregation(
          match(Criteria.where("orderStatusId").is("Done").and("auditData.createDate").gte(threeMonthsAgo)),
          group("employee").count().as("CountOfDeliveredOrders"),
          project("CountOfDeliveredOrders").and("employeeId").previousOperation(),
          sort(Sort.Direction.DESC, "CountOfDeliveredOrders"),
          limit(5)
          );
        AggregationResults<TopEmployeeDTO> result = mongoTemplate.aggregate(
                aggregation, "Order", TopEmployeeDTO.class);
//        List<TopEmployeeDTO> x = result.getMappedResults();
//        for (TopEmployeeDTO employee : result.getMappedResults()) {
//            System.out.println(employee.getUser().getFullName());
//        }
        List<TopEmployeeDTO> resultList = result.getMappedResults();
        for (TopEmployeeDTO employee : resultList) {
            System.out.println("Employee FullName: " + employee.getUser().getFullName());
        }

    }
}
