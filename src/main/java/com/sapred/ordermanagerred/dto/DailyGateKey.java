package com.sapred.ordermanagerred.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyGateKey {
    private String from;
    private String to;
    private LocalDate date;

    @Override
    public String toString() {
        return from + to + date;
    }
}
