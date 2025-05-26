package com.example.demo1.common.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


// Cette classe facilite les échanges d’objets entre client et serveur via sockets.
public class ObjectSocket {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public ObjectSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    // PROF : faut mettre synconized sur les méthodes write et read
    public void write(Object obj) throws IOException {
        out.writeObject(obj);
        out.flush();
    }

    @SuppressWarnings("unchecked")
    public <T> T read() throws IOException, ClassNotFoundException {
        return (T) in.readObject();
    }

    public void close() {
        try {
            in.close();
        } catch (IOException ignored) {}
        try {
            out.close();
        } catch (IOException ignored) {}
        try {
            socket.close();
        } catch (IOException ignored) {}
    }
}