package com.example.demo1.common.models;
import java.io.Serializable;

public class ReservationResponse implements Serializable {
    private final boolean success;
    private final String message;
    public ReservationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    public boolean isSuccess() { return success; }
    public String  getMessage() { return message; }
}
