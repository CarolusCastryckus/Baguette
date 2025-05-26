package com.example.demo1.common.models;

import java.io.Serializable;
import java.util.List;

/** Transport des enregistrements de réservation renvoyés par le serveur */
public class GetReservationsResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<ReservationRecord> reservations;
    public GetReservationsResponse(List<ReservationRecord> reservations) {
        this.reservations = reservations;
    }
    public List<ReservationRecord> getReservations() {
        return reservations;
    }
}
