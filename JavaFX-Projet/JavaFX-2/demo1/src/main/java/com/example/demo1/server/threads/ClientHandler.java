package com.example.demo1.server.threads;

import com.example.demo1.common.models.*;
import com.example.demo1.common.network.ObjectSocket;
import com.example.demo1.common.network.RequestPacket;
import com.example.demo1.server.repositories.ClientDataBaseManagement;
import com.example.demo1.server.repositories.FlightDatabaseManagement;
import com.example.demo1.server.repositories.ReservationDatabaseManagement;

import java.io.IOException;
import java.net.Socket;
import java.util.List;


//Cette classe Gère les requêtes entrantes de chaque client

public class ClientHandler implements Runnable {
    private final ObjectSocket objectSocket;
    private final ClientDataBaseManagement clientDb;

    public ClientHandler(Socket socket) throws IOException {
        this.objectSocket = new ObjectSocket(socket);
        this.clientDb = new ClientDataBaseManagement();
    }

    @Override
    public void run() {
        try {
            while (true) {
                RequestPacket packet = objectSocket.read();
                System.out.println("[SERVER] Paquet reçu : " + packet);

                switch (packet.getType()) {
                    case RequestPacket.ADD_CLIENT -> {
                        AddClientRequest req = (AddClientRequest) packet.getPayload();
                        System.out.println("[SERVER] Contenu du client (ajout) : " + req.getClient());
                        clientDb.addClient(req.getClient());
                        objectSocket.write(new RequestPacket(RequestPacket.SUCCESS_RESPONSE, new SuccessResponse("Client ajouté")));
                    }

                    case RequestPacket.UPDATE_CLIENT -> {
                        UpdateClientRequest req = (UpdateClientRequest) packet.getPayload();
                        System.out.println("[SERVER] Contenu du client (modif) : " + req.getClient());
                        clientDb.updateClient(req.getClient());
                        objectSocket.write(new RequestPacket(RequestPacket.SUCCESS_RESPONSE, new SuccessResponse("Client mis à jour")));
                    }

                    case RequestPacket.DELETE_CLIENT -> {
                        DeleteClientRequest req = (DeleteClientRequest) packet.getPayload();
                        System.out.println("[SERVER] Contenu du client (suppression) : " + req.getClient());
                        clientDb.deleteClient(req.getClient());
                        objectSocket.write(new RequestPacket(RequestPacket.SUCCESS_RESPONSE, new SuccessResponse("Client supprimé")));
                    }

                    case RequestPacket.GET_ALL_CLIENTS -> {
                        System.out.println("[SERVER] ➤ Requête de récupération de tous les clients");
                        List<Client> clients = clientDb.getAllClients();
                        System.out.println("[SERVER] Liste des clients récupérés : " + clients.size());
                        for (Client client : clients) {
                            System.out.println("[SERVER] Client : " + client); // Debugging line
                        }
                        objectSocket.write(new RequestPacket(RequestPacket.CLIENT_LIST_RESPONSE, clients));
                    }

                    // 7) Recherche de vols
                    case RequestPacket.SEARCH_FLIGHTS -> {
                        SearchFlightsRequest req = (SearchFlightsRequest) packet.getPayload();
                        var flights = FlightDatabaseManagement
                                .getInstance()
                                .searchFlights(req.getFrom(), req.getTo(), req.getDate());
                        objectSocket.write(
                                new RequestPacket(RequestPacket.FLIGHT_LIST_RESPONSE, flights)
                        );
                    }

                    // 9) Réserver des places
                    case RequestPacket.RESERVE_FLIGHT -> {
                        ReserveFlightRequest req = (ReserveFlightRequest) packet.getPayload();
                        ReservationResponse resp = ReservationDatabaseManagement
                                .getInstance()
                                .reserveSeats(req.getFlightNumber(),
                                        req.getClientId(),
                                        req.getSeats());
                        objectSocket.write(
                                new RequestPacket(RequestPacket.RESERVATION_RESPONSE, resp)
                        );
                    }

                    // 10) Annuler une réservation
                    case RequestPacket.CANCEL_RESERVATION -> {
                        CancelReservationRequest req = (CancelReservationRequest) packet.getPayload();
                        ReservationResponse resp = ReservationDatabaseManagement
                                .getInstance()
                                .cancelSeats(req.getFlightNumber(),
                                        req.getClientId(),
                                        req.getSeats());
                        objectSocket.write(
                                new RequestPacket(RequestPacket.RESERVATION_RESPONSE, resp)
                        );
                    }

                    // 12) Obtenir les réservations d’un client
                    case RequestPacket.GET_RESERVATIONS -> {
                        GetReservationsRequest req = (GetReservationsRequest) packet.getPayload();
                        var list = ReservationDatabaseManagement
                                .getInstance()
                                .getReservationsForClient(req.getClientId());
                        objectSocket.write(
                                new RequestPacket(RequestPacket.RESERVATIONS_LIST_RESPONSE, list)
                        );
                    }




                    default -> {
                        System.out.println("[SERVER] Type de requête inconnu : " + packet.getType());
                    }
                }

            }
        } catch (Exception e) {
            System.out.println("[SERVER] Client déconnecté.");
            e.printStackTrace(); // <-- Ajoute aussi ça si tu veux voir l’erreur exacte en détail
        }
    }

}
