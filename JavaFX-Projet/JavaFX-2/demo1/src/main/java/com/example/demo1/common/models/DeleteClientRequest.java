package com.example.demo1.common.models;

import java.io.Serializable;

public class DeleteClientRequest implements Serializable {
    private final Client client;

    public DeleteClientRequest(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
}
