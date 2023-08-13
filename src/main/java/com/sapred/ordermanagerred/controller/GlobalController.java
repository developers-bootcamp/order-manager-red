package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.model.Currency;
import com.sapred.ordermanagerred.service.GlobalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/global")
@CrossOrigin("http://localhost:3000")
@Slf4j
public class GlobalController {

    @Autowired
    private GlobalService globalService;

    @GetMapping("/getCurrencies")
    public ResponseEntity<List<Currency>> getCurrencies() {
        log.debug("Entering getCurrencies method");
        return new ResponseEntity<>(globalService.getCurrencies(), HttpStatus.OK);
    }
}
