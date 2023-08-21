package com.sapred.ordermanagerred.model;

public enum OrderStatus {
    NEW,
    APPROVED,
    CANCELLED,
    CHARGING,
    PACKING,
    DELIVERED,
//    this 2 constant are just for now ... because the db filled with these statuses and otherwise my query doesn't work
    CREATED,
    DONE,
}
