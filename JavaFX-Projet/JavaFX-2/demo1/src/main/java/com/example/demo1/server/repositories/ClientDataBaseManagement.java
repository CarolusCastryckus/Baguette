package com.example.demo1.server.repositories;

import com.example.demo1.common.models.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDataBaseManagement {

    private static ClientDataBaseManagement instance;
    private Connection connection;

    public ClientDataBaseManagement() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://java.pypers.tech:3306/Clientsdb?sslMode=DISABLED&allowPublicKeyRetrieval=true",
                    "java",
                    "$zGRA6zbz8z*Mh!R"
            );
            createTableIfNotExists();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ClientDataBaseManagement getInstance() {
        if (instance == null) {
            instance = new ClientDataBaseManagement();
        }
        return instance;
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS clients (" +
                "nationalId VARCHAR(30) PRIMARY KEY, " +
                "firstName TEXT, " +
                "lastName TEXT, " +
                "address TEXT, " +
                "country TEXT, " +
                "meal TEXT, " +
                "luggage TEXT)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addClient(Client client) {
        String sql = "INSERT INTO clients (nationalId, firstName, lastName, address, country, meal, luggage) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.getNationalId());
            stmt.setString(2, client.getFirstName());
            stmt.setString(3, client.getLastName());
            stmt.setString(4, client.getAddress());
            stmt.setString(5, client.getCountry());
            stmt.setString(6, client.getMeal());
            stmt.setString(7, client.getLuggage());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateClient(Client client) {
        String sql = "UPDATE clients SET firstName = ?, lastName = ?, address = ?, country = ?, meal = ?, luggage = ? WHERE nationalId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.getFirstName());
            stmt.setString(2, client.getLastName());
            stmt.setString(3, client.getAddress());
            stmt.setString(4, client.getCountry());
            stmt.setString(5, client.getMeal());
            stmt.setString(6, client.getLuggage());
            stmt.setString(7, client.getNationalId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteClient(Client client) {
        String sql = "DELETE FROM clients WHERE nationalId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.getNationalId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Client client = new Client(
                        rs.getString("nationalId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("address"),
                        rs.getString("country"),
                        rs.getString("meal"),
                        rs.getString("luggage")
                );
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }
}
