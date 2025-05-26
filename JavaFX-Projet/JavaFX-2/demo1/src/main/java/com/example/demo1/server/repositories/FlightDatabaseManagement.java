package com.example.demo1.server.repositories;

import com.example.demo1.client.models.Flight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.Random;

//Dev : Ibrahim - Malik
// Cette classe gère la base de données des vols

public class FlightDatabaseManagement {
    private static FlightDatabaseManagement instance;

    private final String[] airports = {"PAR", "NYC", "LON", "DXB", "TYO"};
    private final String[] airlines = {"Air France", "Delta", "British Airways", "Emirates", "ANA"};
    private final Random random = new Random();

    private FlightDatabaseManagement() {
        createTableIfNotExists();
        if (getFlightCount() == 0) generateRandomFlights();
    }

    public static FlightDatabaseManagement getInstance() {
        if (instance == null) {
            instance = new FlightDatabaseManagement();
        }
        return instance;
    }

    // Utilisation d'une instance de DatabaseConnection
    private Connection getConnection() throws SQLException {
        DatabaseConnection dbConnection = new DatabaseConnection();
        return dbConnection.getConnection();
    }

    private void createTableIfNotExists() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            System.out.println("Connexion à MySQL réussie via DatabaseConnection");
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS flights (
                    flight_number VARCHAR(10) PRIMARY KEY,
                    departure_airport VARCHAR(5),
                    arrival_airport VARCHAR(5),
                    departure_time VARCHAR(10),
                    arrival_time VARCHAR(10),
                    duration VARCHAR(10),
                    price DOUBLE,
                    airline VARCHAR(30),
                    seats_available INT,
                    departure_date DATE
                )
            """);
            System.out.println("Table 'flights' vérifiée/créée");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int getFlightCount() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM flights");
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des vols: " + e.getMessage());
            // La table peut ne pas encore exister
            return 0;
        }
    }

    public void generateRandomFlights() {
        System.out.println("Génération de vols aléatoires...");
        // Générer des vols pour les 30 prochains jours
        for (int day = 0; day < 30; day++) {
            LocalDate date = LocalDate.now().plusDays(day);

            // Pour chaque paire d'aéroports
            for (String departure : airports) {
                for (String arrival : airports) {
                    if (!departure.equals(arrival)) {
                        // Générer 2-5 vols par jour et par paire d'aéroports
                        int flightCount = random.nextInt(4) + 2;
                        for (int i = 0; i < flightCount; i++) {
                            insertFlight(generateRandomFlight(departure, arrival, date));
                        }
                    }
                }
            }
        }
        System.out.println("Génération de vols terminée");
    }

    private Flight generateRandomFlight(String departure, String arrival, LocalDate date) {
        String airline = airlines[random.nextInt(airlines.length)];
        String flightNumber = airline.substring(0, 2).toUpperCase() + (1000 + random.nextInt(9000));

        int departureHour = 6 + random.nextInt(14); // Entre 6h et 20h
        int departureMinute = random.nextInt(60);
        String departureTime = String.format("%02d:%02d", departureHour, departureMinute);

        int durationHours = 1 + random.nextInt(10);
        int durationMinutes = random.nextInt(60);
        String duration = String.format("%dh%02d", durationHours, durationMinutes);

        // Calculer l'heure d'arrivée
        int arrivalHour = (departureHour + durationHours) % 24;
        int arrivalMinute = (departureMinute + durationMinutes) % 60;
        String arrivalTime = String.format("%02d:%02d", arrivalHour, arrivalMinute);

        double price = 100 + random.nextInt(900) + random.nextDouble();
        int seatsAvailable = 10 + random.nextInt(150);

        return new Flight(flightNumber, departure, arrival, departureTime, arrivalTime,
                duration, price, airline, seatsAvailable, date);
    }

    public void insertFlight(Flight flight) {
        String sql = "INSERT INTO flights VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, flight.getFlightNumber());
            pstmt.setString(2, flight.getDepartureAirport());
            pstmt.setString(3, flight.getArrivalAirport());
            pstmt.setString(4, flight.getDepartureTime());
            pstmt.setString(5, flight.getArrivalTime());
            pstmt.setString(6, flight.getDuration());
            pstmt.setDouble(7, flight.getPrice());
            pstmt.setString(8, flight.getAirline());
            pstmt.setInt(9, flight.getSeatsAvailable());
            pstmt.setDate(10, java.sql.Date.valueOf(flight.getDepartureDate()));

            pstmt.executeUpdate();

        } catch (SQLException e) {
            // Ignorer les erreurs de clé dupliquée
            if (!e.getMessage().contains("Duplicate entry")) {
                System.err.println("Erreur lors de l'insertion de vol: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public ObservableList<Flight> searchFlights(String departure, String arrival, LocalDate date) {
        ObservableList<Flight> results = FXCollections.observableArrayList();

        String sql = """
            SELECT * FROM flights 
            WHERE departure_airport = ? 
            AND arrival_airport = ? 
            AND departure_date = ?
        """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, departure);
            pstmt.setString(2, arrival);
            pstmt.setDate(3, java.sql.Date.valueOf(date));

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Flight flight = new Flight(
                        rs.getString("flight_number"),
                        rs.getString("departure_airport"),
                        rs.getString("arrival_airport"),
                        rs.getString("departure_time"),
                        rs.getString("arrival_time"),
                        rs.getString("duration"),
                        rs.getDouble("price"),
                        rs.getString("airline"),
                        rs.getInt("seats_available"),
                        rs.getDate("departure_date").toLocalDate()
                );
                results.add(flight);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de vols: " + e.getMessage());
            e.printStackTrace();
        }

        return results;
    }

    // Méthode utilitaire pour vider/réinitialiser la table (utile pour les tests)
    public void clearFlights() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("TRUNCATE TABLE flights");
            System.out.println("Table 'flights' vidée");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la réinitialisation de la table: " + e.getMessage());
            e.printStackTrace();
        }
    }


}