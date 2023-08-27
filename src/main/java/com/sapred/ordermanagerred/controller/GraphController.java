package com.sapred.ordermanagerred.controller;

import com.mongodb.client.AggregateIterable;
import com.sapred.ordermanagerred.dto.TopEmployeeDTO;
import com.sapred.ordermanagerred.service.GraphService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Graph")
@CrossOrigin("http://localhost:3000")
public class GraphController {
    @Autowired
    private GraphService graphService;

    @GetMapping("/topEmployee")
    public List<TopEmployeeDTO> topEmployee(@RequestHeader("token") String token) {
       return graphService.topEmployee(token);
    }
}
