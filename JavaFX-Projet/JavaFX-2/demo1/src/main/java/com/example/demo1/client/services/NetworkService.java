//package com.example.demo1.client.services;
//
//import com.example.demo1.client.repositories.NetworkRepository;
//import com.example.demo1.common.models.*;
//import com.example.demo1.common.network.RequestPacket;
//
//import java.io.IOException;
//import java.util.List;
//
//
//// Cette classe est le Service client qui envoie des requêtes (RequestPacket) au serveur
//// et écoute les réponses via NetworkRepository.
//public class NetworkService implements NetworkRepository.Listener {
//
//    private final NetworkRepository repository;
//    private Listener listener;
//
//    public NetworkService() {
//        this.repository = new NetworkRepository();
//        this.repository.setListener(this);
//    }
//
//    public void setListener(Listener listener) {
//        this.listener = listener;
//    }
//
//    public void connect(String host, int port) throws IOException {
//        repository.connectToServer(host, port);
//    }
//
//    public void addClient(Client client) throws IOException {
//        AddClientRequest request = new AddClientRequest(client);
//        RequestPacket packet = new RequestPacket(RequestPacket.ADD_CLIENT, request);
//        repository.sendRequest(packet);
//    }
//
//    public void updateClient(Client client) throws IOException {
//        RequestPacket packet = new RequestPacket(RequestPacket.UPDATE_CLIENT, new UpdateClientRequest(client));
//        repository.sendRequest(packet);
//    }
//
//    public void deleteClient(Client client) throws IOException {
//        RequestPacket packet = new RequestPacket(RequestPacket.DELETE_CLIENT, new DeleteClientRequest(client));
//        repository.sendRequest(packet);
//    }
//
//    public void getAllClients() throws IOException {
//        GetAllClientsRequest request = new GetAllClientsRequest();
//        RequestPacket packet = new RequestPacket(RequestPacket.GET_ALL_CLIENTS, request);
//        repository.sendRequest(packet);
//    }
//
//
//    /**
//     * Cette méthode est appelée lorsque le service (NetwokrService)  reçoit une réponse du serveur.
//     * Elle répond en fonction du type de réponse.
//     * @param response
//     */
//    @Override
//    public void onResponseReceived(Object response) {
//
//        // Permet de déboguer la réception des réponses
//        //System.out.println("[CLIENT] Réponse reçue du serveur : " + response);
//
//        if (response instanceof SuccessResponse success && listener != null) {
//            String message = success.getMessage().toLowerCase();
//            if (message.contains("ajout")) {
//                listener.onClientAdded(success);
//            } else if (message.contains("modifié")) {
//                listener.onClientUpdated(success);
//            } else if (message.contains("supprimé")) {
//                listener.onClientDeleted(success);
//            }
//        } else if (response instanceof RequestPacket packet) {
//            if (packet.getType() == RequestPacket.CLIENT_LIST_RESPONSE) {
//                Object payload = packet.getPayload();
//                if (payload instanceof List<?> list && !list.isEmpty() && list.get(0) instanceof Client) {
//                    listener.onClientListReceived((List<Client>) list);
//                }
//            }
//        }
//    }
//
//    public interface Listener {
//        void onClientAdded(SuccessResponse response);
//        void onClientUpdated(SuccessResponse response);
//        void onClientDeleted(SuccessResponse response);
//        void onClientListReceived(List<Client> clients);
//    }
//
//}
//


//Version 2

package com.example.demo1.client.services;

import com.example.demo1.client.models.Flight;
import com.example.demo1.client.repositories.NetworkRepository;
import com.example.demo1.common.models.*;
import com.example.demo1.common.network.RequestPacket;
import javafx.application.Platform;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class NetworkService implements NetworkRepository.Listener {

    private final NetworkRepository repository;
    private Listener listener;
    private String currentClientId;

    public NetworkService() {
        this.repository = new NetworkRepository();
        this.repository.setListener(this);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    /** Connexion au serveur */
    public void connect(String host, int port) throws IOException {
        repository.connectToServer(host, port);
    }

    /** Doit être appelé juste après le login, pour qu’on sache quel client réserve */
    public void setClientId(String clientId) {
        this.currentClientId = clientId;
    }

    /*───────────────────────────*/
    /*  Méthodes client existantes  */
    /*───────────────────────────*/

    public void addClient(Client client) throws IOException {
        repository.sendRequest(new RequestPacket(RequestPacket.ADD_CLIENT,
                new AddClientRequest(client)));
    }

    public void updateClient(Client client) throws IOException {
        repository.sendRequest(new RequestPacket(RequestPacket.UPDATE_CLIENT,
                new UpdateClientRequest(client)));
    }

    public void deleteClient(Client client) throws IOException {
        repository.sendRequest(new RequestPacket(RequestPacket.DELETE_CLIENT,
                new DeleteClientRequest(client)));
    }

    public void getAllClients() throws IOException {
        repository.sendRequest(new RequestPacket(RequestPacket.GET_ALL_CLIENTS,
                new GetAllClientsRequest()));
    }

    /*──────────────────────────────────────────────────*/
    /*  Nouvelles méthodes pour vols & réservations   */
    /*──────────────────────────────────────────────────*/

    /** Recherche de vols */
    public void searchFlights(String from, String to, LocalDate date) throws IOException {
        repository.searchFlights(from, to, date);
    }

    /** Réserver des sièges */
    public void reserveFlight(String flightNumber, int seats) throws IOException {
        repository.reserveFlight(flightNumber, currentClientId, seats);
    }

    /** Annuler une réservation */
    public void cancelReservation(String flightNumber, int seats) throws IOException {
        repository.cancelReservation(flightNumber, currentClientId, seats);
    }

    /** Charger mes réservations */
    public void loadMyReservations() throws IOException {
        repository.getMyReservations(currentClientId);
    }

    //──────────────────────────────────────────────────────────────────────────────
    // C’est ici qu’on reçoit **tous** les RequestPacket en provenance du serveur
    //──────────────────────────────────────────────────────────────────────────────
    @Override
    public void onResponseReceived(RequestPacket packet) {
        Platform.runLater(() -> {
            switch (packet.getType()) {

                // ─── Clients existant ──────────────────────────────────────────
                case RequestPacket.SUCCESS_RESPONSE -> {
                    SuccessResponse resp = (SuccessResponse) packet.getPayload();
                    String msg = resp.getMessage().toLowerCase();
                    if (msg.contains("ajout")) {
                        listener.onClientAdded(resp);
                    } else if (msg.contains("modifié")) {
                        listener.onClientUpdated(resp);
                    } else if (msg.contains("supprimé")) {
                        listener.onClientDeleted(resp);
                    }
                }
                case RequestPacket.CLIENT_LIST_RESPONSE -> {
                    List<Client> clients = (List<Client>) packet.getPayload();
                    listener.onClientListReceived(clients);
                }

                // ─── Vols ────────────────────────────────────────────────────
                case RequestPacket.FLIGHT_LIST_RESPONSE -> {
                    List<Flight> flights = (List<Flight>) packet.getPayload();
                    listener.onFlightListReceived(flights);
                }

                // ─── Réservations ─────────────────────────────────────────────
                case RequestPacket.RESERVATION_RESPONSE -> {
                    ReservationResponse resp = (ReservationResponse) packet.getPayload();
                    listener.onReservationResponse(resp);
                }
                case RequestPacket.RESERVATIONS_LIST_RESPONSE -> {
                    List<ReservationRecord> recs = (List<ReservationRecord>) packet.getPayload();
                    listener.onReservationsListReceived(recs);
                }

                default -> {
                    System.err.println("[SERVICE] Réponse non gérée : " + packet.getType());
                }
            }
        });
    }

    /** Interface de callback pour le contrôleur */
    public interface Listener {
        // Clients
        void onClientAdded(SuccessResponse response);
        void onClientUpdated(SuccessResponse response);
        void onClientDeleted(SuccessResponse response);
        void onClientListReceived(List<Client> clients);

        // Vols
        void onFlightListReceived(List<Flight> flights);

        // Réservations
        void onReservationResponse(ReservationResponse response);
        void onReservationsListReceived(List<ReservationRecord> reservations);
    }
}

