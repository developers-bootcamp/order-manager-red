package com.sapred.ordermanagerred.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public enum Currency {
    €("EUR"), ₪("ILS"), Fr("CHF"), £("GBP"), $("USD");

    private @Getter @Setter String code;

    private Currency(String code) {
        this.code = code;
    }
}