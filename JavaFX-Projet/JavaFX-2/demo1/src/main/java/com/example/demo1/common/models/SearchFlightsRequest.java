package com.example.demo1.common.models;

import java.time.LocalDate;
import java.io.Serializable;

public class SearchFlightsRequest implements Serializable {
    private final String from;
    private final String to;
    private final LocalDate date;

    public SearchFlightsRequest(String from, String to, LocalDate date) {
        this.from = from;
        this.to   = to;
        this.date = date;
    }

    public String getFrom() { return from; }
    public String getTo()   { return to; }
    public LocalDate getDate() { return date; }
}
