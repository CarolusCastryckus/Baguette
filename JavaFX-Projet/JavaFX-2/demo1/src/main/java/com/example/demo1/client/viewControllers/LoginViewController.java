//package com.example.demo1.client.viewControllers;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//import javafx.stage.Stage;
//import javafx.scene.Scene;
//import javafx.fxml.FXMLLoader;
//
////Dev : Rémi
////Gère le mot de passe + renvoie vers la Partie 2
//
//public class LoginViewController {
//
//    @FXML
//    private TextField usernameField;
//    @FXML
//    private PasswordField passwordField;
//
//    // Méthode de gestion du login
//    @FXML
//    private void handleLogin() {
//        String username = usernameField.getText();
//        String password = passwordField.getText();
//
//        // Vérification des informations d'identification
//        if ("admin".equals(username) && "admin".equals(password)) {
//            // Fermeture de la fenêtre de connexion
//            Stage stage = (Stage) usernameField.getScene().getWindow();
//            stage.close();
//
//            // Ouverture de la nouvelle fenêtre de gestion des clients
//            try {
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/demo1/client-view.fxml"));
//                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
//                Stage newStage = new Stage();
//                newStage.setTitle("Voyage Voyage International - Gestion des Clients");
//                newStage.setScene(scene);
//                newStage.show();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            // Si le mot de passe est incorrect, afficher un message d'erreur
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Erreur de connexion");
//            alert.setHeaderText("Nom d'utilisateur ou mot de passe incorrect");
//            alert.showAndWait();
//        }
//    }
//}
//



//Version 2

package com.example.demo1.client.viewControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.example.demo1.client.controllers.MainApplication;

public class LoginViewController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button btnLogin;

    @FXML
    public void initialize() {
        btnLogin.setOnAction(evt -> handleLogin());
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if ("admin".equals(username) && "admin".equals(password)) {
            // Au lieu de fermer/ouvrir des Stage manuellement :
            MainApplication.showClientView();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de connexion");
            alert.setHeaderText("Identifiant ou mot de passe incorrect");
            alert.showAndWait();
        }
    }
}

