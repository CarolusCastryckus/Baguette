package com.example.demo1.common.models;

import java.io.Serializable;

public class SuccessResponse implements Serializable {
    private final String message;

    public SuccessResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
