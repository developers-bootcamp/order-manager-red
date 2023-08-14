package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.dto.TopEmployeeDTO;
import com.sapred.ordermanagerred.service.GraphService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Graph")
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
}
