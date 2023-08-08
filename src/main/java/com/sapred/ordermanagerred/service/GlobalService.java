package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.model.Currency;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GlobalService {

    public List<Currency> getCurrencies() {
        return Arrays.stream(Currency.values()).toList();
    }
}
