package com.example.demo1.common.models;
import java.io.Serializable;

public class CancelReservationRequest implements Serializable {
    private final String flightNumber;
    private final String clientId;
    private final int seats;
    public CancelReservationRequest(String flightNumber, String clientId, int seats) {
        this.flightNumber = flightNumber;
        this.clientId     = clientId;
        this.seats        = seats;
    }
    public String getFlightNumber() { return flightNumber; }
    public String getClientId()     { return clientId; }
    public int    getSeats()        { return seats; }
}
