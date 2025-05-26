//package com.example.demo1.client.repositories;
//
//import com.example.demo1.common.models.CancelReservationRequest;
//import com.example.demo1.common.models.GetReservationsRequest;
//import com.example.demo1.common.models.ReserveFlightRequest;
//import com.example.demo1.common.models.SearchFlightsRequest;
//import com.example.demo1.common.network.ObjectSocket;
//import com.example.demo1.common.network.RequestPacket;
//
//import java.io.IOException;
//import java.net.Socket;
//import java.time.LocalDate;
//
//public class NetworkRepository {
//    private ObjectSocket objectSocket;
//    private Listener listener;
//
//    public void setListener(Listener listener) {
//        this.listener = listener;
//    }
//
//    public void connectToServer(String host, int port) throws IOException {
//        Socket socket = new Socket(host, port);
//        this.objectSocket = new ObjectSocket(socket);
//        startListening();
//    }
//
//    public void sendRequest(RequestPacket packet) throws IOException {
//        objectSocket.write(packet);
//    }
//
//    private void startListening() {
//        Thread thread = new Thread(() -> {
//            try {
//                while (true) {
//                    Object response = objectSocket.read();
//                    if (listener != null) listener.onResponseReceived(response);
//                }
//            } catch (Exception e) {
//                System.err.println("[CLIENT] Erreur réception: " + e.getMessage());
//            }
//        });
//        thread.start();
//    }
//
//    /** Recherche des vols sur le serveur */
//    public void searchFlights(String from, String to, LocalDate date) throws IOException {
//        SearchFlightsRequest req = new SearchFlightsRequest(from, to, date);
//        sendRequest(new RequestPacket(RequestPacket.SEARCH_FLIGHTS, req));
//    }
//
//    /** Réserve un certain nombre de sièges */
//    public void reserveFlight(String flightNumber, String clientId, int seats) throws IOException {
//        ReserveFlightRequest req = new ReserveFlightRequest(flightNumber, clientId, seats);
//        sendRequest(new RequestPacket(RequestPacket.RESERVE_FLIGHT, req));
//    }
//
//    /** Annule une réservation */
//    public void cancelReservation(String flightNumber, String clientId, int seats) throws IOException {
//        CancelReservationRequest req = new CancelReservationRequest(flightNumber, clientId, seats);
//        sendRequest(new RequestPacket(RequestPacket.CANCEL_RESERVATION, req));
//    }
//
//    /** Demande la liste des réservations du client */
//    public void getMyReservations(String clientId) throws IOException {
//        GetReservationsRequest req = new GetReservationsRequest(clientId);
//        sendRequest(new RequestPacket(RequestPacket.GET_RESERVATIONS, req));
//    }
//
//    public interface Listener {
//        void onResponseReceived(Object response);
//    }
//}


// Version 2

package com.example.demo1.client.repositories;

import com.example.demo1.common.models.CancelReservationRequest;
import com.example.demo1.common.models.GetReservationsRequest;
import com.example.demo1.common.models.ReserveFlightRequest;
import com.example.demo1.common.models.SearchFlightsRequest;
import com.example.demo1.common.network.ObjectSocket;
import com.example.demo1.common.network.RequestPacket;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;

public class NetworkRepository {
    private ObjectSocket objectSocket;
    private Listener listener;

    /** Définit le listener qui sera appelé à chaque paquet reçu */
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    /** Ouvre la connexion au serveur et démarre le thread d'écoute */
    public void connectToServer(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        this.objectSocket = new ObjectSocket(socket);
        startListening();
    }

    /** Envoie un RequestPacket au serveur */
    public void sendRequest(RequestPacket packet) throws IOException {
        objectSocket.write(packet);
    }

    /** Thread dédié à la réception des RequestPacket en continu */
    private void startListening() {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    // On lit directement un RequestPacket
                    RequestPacket packet = (RequestPacket) objectSocket.read();
                    if (listener != null) {
                        listener.onResponseReceived(packet);
                    }
                }
            } catch (Exception e) {
                System.err.println("[CLIENT] Erreur réception: " + e.getMessage());
            }
        }, "NetworkRepository-Listener");
        thread.setDaemon(true);
        thread.start();
    }

    /*───────────────────────────────────────────────────────────────────────────*/
    /*  Méthodes pour construire et envoyer les différentes requêtes au serveur  */
    /*───────────────────────────────────────────────────────────────────────────*/

    /** Recherche des vols sur le serveur */
    public void searchFlights(String from, String to, LocalDate date) throws IOException {
        SearchFlightsRequest req = new SearchFlightsRequest(from, to, date);
        sendRequest(new RequestPacket(RequestPacket.SEARCH_FLIGHTS, req));
    }

    /** Réserve un certain nombre de sièges */
    public void reserveFlight(String flightNumber, String clientId, int seats) throws IOException {
        ReserveFlightRequest req = new ReserveFlightRequest(flightNumber, clientId, seats);
        sendRequest(new RequestPacket(RequestPacket.RESERVE_FLIGHT, req));
    }

    /** Annule une réservation */
    public void cancelReservation(String flightNumber, String clientId, int seats) throws IOException {
        CancelReservationRequest req = new CancelReservationRequest(flightNumber, clientId, seats);
        sendRequest(new RequestPacket(RequestPacket.CANCEL_RESERVATION, req));
    }

    /** Demande la liste des réservations du client */
    public void getMyReservations(String clientId) throws IOException {
        GetReservationsRequest req = new GetReservationsRequest(clientId);
        sendRequest(new RequestPacket(RequestPacket.GET_RESERVATIONS, req));
    }

    /**
     * Listener à implémenter pour recevoir les paquets en retour.
     * Le payload est récupérable via packet.getPayload() et le type
     * via packet.getType().
     */
    public interface Listener {
        void onResponseReceived(RequestPacket packet);
    }
}


