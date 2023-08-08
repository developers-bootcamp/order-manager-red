package com.sapred.ordermanagerred.controller;
import com.sapred.ordermanagerred.dto.MonthProductCountDto;
import com.sapred.ordermanagerred.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    }
}
