package com.sapred.ordermanagerred.controller;
import com.sapred.ordermanagerred.dto.MonthProductCountDto;
import com.sapred.ordermanagerred.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/graph")
public class GraphController {
    @Autowired
    private GraphService graphService;

    @GetMapping("/{rangeOfMonths}")
    public ResponseEntity/*List<MonthProductCountDto>*/ getTopProducts(
            @RequestHeader("token") String token, @PathVariable("rangeOfMonths") int rangeOfMonths){
        try{
            List<MonthProductCountDto> list= graphService.getTopProduct(token, rangeOfMonths);
            return ResponseEntity.status(HttpStatus.OK).body(list);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
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
