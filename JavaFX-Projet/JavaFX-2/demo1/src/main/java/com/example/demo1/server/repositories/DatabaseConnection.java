package com.example.demo1.server.repositories;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private static final String URL = "jdbc:mysql://java.pypers.tech:3306/Clientsdb?sslMode=DISABLED&allowPublicKeyRetrieval=true";
    private static final String USER = "java";
    private static final String PASSWORD = "$zGRA6zbz8z*Mh!R";

    DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
