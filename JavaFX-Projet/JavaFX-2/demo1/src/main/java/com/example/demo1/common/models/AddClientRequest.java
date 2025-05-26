package com.example.demo1.common.models;

import java.io.Serializable;

public class AddClientRequest implements Serializable {
    private final Client client;

    public AddClientRequest(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
}
