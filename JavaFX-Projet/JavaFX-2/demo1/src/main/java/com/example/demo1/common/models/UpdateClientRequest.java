package com.example.demo1.common.models;

import java.io.Serializable;

public class UpdateClientRequest implements Serializable {
    private final Client client;

    public UpdateClientRequest(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
}
