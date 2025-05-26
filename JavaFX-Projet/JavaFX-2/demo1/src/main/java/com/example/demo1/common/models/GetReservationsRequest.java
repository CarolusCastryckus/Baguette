package com.example.demo1.common.models;

import java.io.Serializable;

/** Contient l’ID du client dont on veut lister les réservations */
public class GetReservationsRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String clientId;
    public GetReservationsRequest(String clientId) {
        this.clientId = clientId;
    }
    public String getClientId() {
        return clientId;
    }
}
