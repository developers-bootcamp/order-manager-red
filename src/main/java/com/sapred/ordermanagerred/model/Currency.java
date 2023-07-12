package com.sapred.ordermanagerred.model;

public enum Currency {
    EURO(89.52907), SHEKEL(22.70258), FRANC(91.45229), RUBLE(0.97772), DOLLAR(81.98195);

    private double inr;

    private Currency(double inr) {
        this.inr = inr;
    }
}