package com.example.demo1.server.repositories;

import com.example.demo1.common.models.ReservationRecord;
import com.example.demo1.common.models.ReservationResponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;

/**
 * Gère la table 'reservations' et met à jour seats_available dans flights.
 */
public class ReservationDatabaseManagement {
    private static ReservationDatabaseManagement instance;

    private ReservationDatabaseManagement() {
        createTableIfNotExists();
    }

    public static ReservationDatabaseManagement getInstance() {
        if (instance == null) instance = new ReservationDatabaseManagement();
        return instance;
    }

    private Connection getConnection() throws SQLException {
        return new DatabaseConnection().getConnection();
    }

    private void createTableIfNotExists() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("""
              CREATE TABLE IF NOT EXISTS reservations (
                flight_number  VARCHAR(10),
                client_id       VARCHAR(20),
                reserved_date   DATE,
                PRIMARY KEY(flight_number, client_id, reserved_date),
                FOREIGN KEY(flight_number) REFERENCES flights(flight_number),
                FOREIGN KEY(client_id)     REFERENCES clients(national_id)
              )
            """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Réserve `seats` places pour `clientId` sur `flightNumber`. */
    public ReservationResponse reserveSeats(String flightNumber, String clientId, int seats) {
        String dec = "UPDATE flights SET seats_available = seats_available - ? WHERE flight_number = ? AND seats_available >= ?";
        String ins = "INSERT INTO reservations(flight_number, client_id, reserved_date) VALUES (?, ?, ?)";
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement p = conn.prepareStatement(dec)) {
                p.setInt(1, seats);
                p.setString(2, flightNumber);
                p.setInt(3, seats);
                if (p.executeUpdate() == 0) {
                    conn.rollback();
                    return new ReservationResponse(false, "Pas assez de places restantes.");
                }
            }

            try (PreparedStatement p = conn.prepareStatement(ins)) {
                Date today = Date.valueOf(LocalDate.now());
                for (int i = 0; i < seats; i++) {
                    p.setString(1, flightNumber);
                    p.setString(2, clientId);
                    p.setDate(3, today);
                    p.addBatch();
                }
                p.executeBatch();
            }

            conn.commit();
            return new ReservationResponse(true, "Réservation OK !");
        } catch (SQLException ex) {
            return new ReservationResponse(false, "Erreur serveur : " + ex.getMessage());
        }
    }

    /** Annule `seats` places pour `clientId` sur `flightNumber`. */
    public ReservationResponse cancelSeats(String flightNumber, String clientId, int seats) {
        String del = "DELETE FROM reservations WHERE flight_number = ? AND client_id = ? LIMIT ?";
        String inc = "UPDATE flights SET seats_available = seats_available + ? WHERE flight_number = ?";
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement p = conn.prepareStatement(del)) {
                p.setString(1, flightNumber);
                p.setString(2, clientId);
                p.setInt(3, seats);
                if (p.executeUpdate() == 0) {
                    conn.rollback();
                    return new ReservationResponse(false, "Aucune réservation à annuler.");
                }
            }

            try (PreparedStatement p = conn.prepareStatement(inc)) {
                p.setInt(1, seats);
                p.setString(2, flightNumber);
                p.executeUpdate();
            }

            conn.commit();
            return new ReservationResponse(true, "Annulation OK !");
        } catch (SQLException ex) {
            return new ReservationResponse(false, "Erreur serveur : " + ex.getMessage());
        }
    }

    /** Récupère toutes les réservations d'un client (pour l'onglet Mes Réservations). */
    public ObservableList<ReservationRecord> getReservationsForClient(String clientId) {
        ObservableList<ReservationRecord> list = FXCollections.observableArrayList();
        String sql = "SELECT flight_number, reserved_date, COUNT(*) AS seats " +
                "FROM reservations WHERE client_id = ? GROUP BY flight_number, reserved_date";
        try (Connection conn = getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {
            p.setString(1, clientId);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                list.add(new ReservationRecord(
                        rs.getString("flight_number"),
                        rs.getDate("reserved_date").toLocalDate(),
                        rs.getInt("seats")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
