package com.sapred.ordermanagerred.controller;
import com.sapred.ordermanagerred.dto.MonthProductCountDto;
import com.sapred.ordermanagerred.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/graph")
public class GraphController {
    @Autowired
    private GraphService graphService;

    @GetMapping("/{rangeOfMonths}")
    public List<MonthProductCountDto> getTopProducts(
            @RequestHeader("token") String token, @PathVariable("rangeOfMonths") int rangeOfMonths){
        return graphService.getTopProduct(token, rangeOfMonths);
    }
}
