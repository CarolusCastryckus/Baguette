package com.example.demo1.common.models;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO pour représenter une ligne groupée de réservations :
 * - flightNumber : numéro du vol
 * - reservedDate : date de la réservation
 * - seats        : nombre de sièges réservés
 */
public class ReservationRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String flightNumber;
    private final LocalDate reservedDate;
    private final int seats;

    public ReservationRecord(String flightNumber, LocalDate reservedDate, int seats) {
        this.flightNumber = flightNumber;
        this.reservedDate = reservedDate;
        this.seats        = seats;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public LocalDate getReservedDate() {
        return reservedDate;
    }

    public int getSeats() {
        return seats;
    }

    @Override
    public String toString() {
        return flightNumber + " | " + reservedDate + " | " + seats + " place(s)";
    }
}
