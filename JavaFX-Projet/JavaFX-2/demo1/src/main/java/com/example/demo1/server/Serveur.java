package com.example.demo1.server;

import com.example.demo1.server.threads.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {

    private static final int PORT = 7000;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[SERVER] Serveur démarré sur le port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[SERVER] Nouveau client connecté : " + clientSocket);

                // On crée un handler pour ce client (plus de liste partagée)
                ClientHandler handler = new ClientHandler(clientSocket);
                new Thread(handler).start(); // Lancement d’un thread pour ce client
            }

        } catch (IOException e) {
            System.err.println("[SERVER] Erreur : " + e.getMessage());
        }
    }
}
