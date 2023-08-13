package com.sapred.ordermanagerred.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public enum Currency {
    EURO("EUR"), SHEKEL("ILS"), FRANC("CHF"), LIRA("GBP"), DOLLAR("USD");

    private @Getter @Setter String code;

    private Currency(String code) {
        this.code = code;
    }
}