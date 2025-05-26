package com.example.demo1.client.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("Voyage Voyage International");
        showLoginView();      // démarrage sur l'écran de login
        primaryStage.show();
    }

    private static void showView(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(
                    MainApplication.class.getResource(fxmlPath)
            );
            primaryStage.setTitle(title);
            primaryStage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour afficher la vue de connexion
    public static void showLoginView() {
        showView(
                "/com/example/demo1/login-view.fxml",
                "Connexion – Voyage Voyage International"
        );
    }

    // Méthode pour afficher la vue des clients
    public static void showClientView() {
        showView(
                "/com/example/demo1/client-view.fxml",
                "Gestion des Clients – Voyage Voyage International"
        );
    }

    // Méthode pour afficher la vue des passagers
    public static void showPassengerView() {
        showView(
                "/com/example/demo1/passenger-view.fxml",
                "Gestion des Passagers – Voyage Voyage International"
        );
    }

    // Méthode pour afficher la vue des voyages
    public static void showVoyageView() {
        showView(
                "/com/example/demo1/voyage-view.fxml",
                "Gestion des Voyages – Voyage Voyage International"
        );
    }

    // Méthode pour afficher la vue des sièges
    public static void showSeatView() {
        showView(
                "/com/example/demo1/seat-view.fxml",
                "Gestion des Sièges – Voyage Voyage International"
        );
    }



    public static void main(String[] args) {
        launch(args);
    }
}
