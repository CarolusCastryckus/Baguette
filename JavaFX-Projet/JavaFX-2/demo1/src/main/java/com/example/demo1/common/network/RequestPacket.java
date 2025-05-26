// ===== File: common/network/RequestPacket.java =====

package com.example.demo1.common.network;

import java.io.Serializable;

// Cette classe représente un paquet de requête qui sera envoyé entre le client et le serveur.
public class RequestPacket implements Serializable {
    public static final int ADD_CLIENT = 1;
    public static final int UPDATE_CLIENT = 2;
    public static final int DELETE_CLIENT = 3;
    public static final int SUCCESS_RESPONSE = 4;
    public static final int GET_ALL_CLIENTS = 5;
    public static final int CLIENT_LIST_RESPONSE = 6;

    // Vols
    public static final int SEARCH_FLIGHTS = 7;
    public static final int FLIGHT_LIST_RESPONSE = 8;

    // Réservations
    public static final int RESERVE_FLIGHT = 9;
    public static final int CANCEL_RESERVATION = 10;
    public static final int RESERVATION_RESPONSE = 11;
    public static final int GET_RESERVATIONS = 12;
    public static final int RESERVATIONS_LIST_RESPONSE = 13;

    private final int type;
    private final Object payload;

    public RequestPacket(int type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    public int getType() {
        return type;
    }

    public Object getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "RequestPacket{type=" + type + ", payload=" + payload + "}";
    }

}