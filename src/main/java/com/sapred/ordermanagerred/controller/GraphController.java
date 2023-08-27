package com.sapred.ordermanagerred.controller;

import com.mongodb.client.AggregateIterable;
import com.sapred.ordermanagerred.dto.DynamicGraph;
import com.sapred.ordermanagerred.dto.MonthProductCountDto;
import com.sapred.ordermanagerred.model.Order;
import com.sapred.ordermanagerred.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.bson.Document;
import com.sapred.ordermanagerred.dto.TopEmployeeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/graph")
@CrossOrigin("http://localhost:3000")
@Slf4j
public class GraphController {
    @Autowired
    private GraphService graphService;

    @GetMapping("/topEmployee")
    public List<TopEmployeeDTO> topEmployee() {
        log.debug("Entering topEmployee method");
        return graphService.topEmployee();
    }

    @GetMapping("/{rangeOfMonths}")
    public List<MonthProductCountDto> getTopProducts(
            @RequestHeader("token") String token, @PathVariable("rangeOfMonths") int rangeOfMonths) {
        log.debug("Entering getTopProducts method");
        return graphService.getTopProduct(token, rangeOfMonths);
    }

    @GetMapping("/dynamicGraph/{subject}/{field}")
    public List<DynamicGraph> dynamicGraph(@RequestHeader("token") String token, @PathVariable("subject") String subject, @PathVariable("field") String field) {
        log.debug("Entering dynamicGraph method");
        return graphService.dynamicGraph(token, subject, field);
    }

}
