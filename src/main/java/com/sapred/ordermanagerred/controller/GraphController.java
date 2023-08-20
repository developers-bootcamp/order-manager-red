package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.dto.MonthProductCountDto;
import com.sapred.ordermanagerred.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sapred.ordermanagerred.dto.TopEmployeeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Month;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/status")
    public ResponseEntity<Map<Month, Map<Integer,Integer>>> getStatus(@RequestHeader("token") String token) {
        int monthAmount = 5;
        try{
            return ResponseEntity.ok(graphService.getStatus(token, monthAmount));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
