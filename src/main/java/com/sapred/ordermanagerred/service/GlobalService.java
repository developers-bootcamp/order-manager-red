package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.model.Currency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class GlobalService {

    public List<Currency> getCurrencies() {
        log.info("Retrieving list of currencies");

        List<Currency> currencies = Arrays.asList(Currency.values());

        log.info("Currencies retrieved: {}", currencies);

        return currencies;
    }
}
