package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.service.AggregationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.Document;
import java.util.Map;

@RestController
@RequestMapping("/Aggregation")
public class AggregationController {
    @Autowired
    AggregationService aggregationService;
}
