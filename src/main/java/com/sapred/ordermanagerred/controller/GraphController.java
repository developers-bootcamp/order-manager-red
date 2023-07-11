package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Graph")
public class GraphController {
    @Autowired
    private GraphService graphService;

    @GetMapping("/topEmployee")
    public void topEmployee() {
        graphService.topEmployee();
    }
}
