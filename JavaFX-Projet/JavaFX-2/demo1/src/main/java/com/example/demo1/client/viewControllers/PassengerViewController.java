package com.example.demo1.client.viewControllers;

import com.example.demo1.client.controllers.MainApplication;
import com.example.demo1.client.models.Passenger;
//import com.example.demo1.client.models.VoyageModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

//Dev : Ibrahim
//Pour la Partie 4
public class PassengerViewController implements Initializable {

    @FXML private TextField idField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField ageField;
    @FXML private TextField addressField;
    @FXML private ComboBox<String> countryCombo;
    @FXML private ComboBox<String> mealCombo;
    @FXML private CheckBox luggageCheckBox;

    @FXML private Button applyButton;
    @FXML private Button nextButton;

    //ListView pour les catégories
    @FXML private ListView<Passenger> adultListView;
    @FXML private ListView<Passenger> childListView;
    @FXML private ListView<Passenger> babyListView;

    // Liste principale de passagers
    private ObservableList<Passenger> passengers = FXCollections.observableArrayList();
    private Passenger selectedPassenger;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // --- navigation vers la vue Voyages ---
         nextButton.setOnAction(evt ->
            MainApplication.showVoyageView());

        // Initialiser les ComboBox
        countryCombo.setItems(FXCollections.observableArrayList(
                "USA", "France", "UK", "Germany", "Japan", "China", "Australia", "Other"
        ));
        countryCombo.setValue("USA");

        mealCombo.setItems(FXCollections.observableArrayList(
                "Basic", "Kosher", "Halal"
        ));
        mealCombo.setValue("Basic");

        // Ajout de listeners pour la sélection sur chaque ListView
        adultListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                clearOtherSelections("adult");
                selectedPassenger = newSelection;
                fillForm(selectedPassenger);
            }
        });
        childListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                clearOtherSelections("child");
                selectedPassenger = newSelection;
                fillForm(selectedPassenger);
            }
        });
        babyListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                clearOtherSelections("baby");
                selectedPassenger = newSelection;
                fillForm(selectedPassenger);
            }
        });
    }

    // Méthode pour vider les sélections dans les ListView autres que celle en cours
    private void clearOtherSelections(String current) {
        if (!"adult".equals(current)) {
            adultListView.getSelectionModel().clearSelection();
        }
        if (!"child".equals(current)) {
            childListView.getSelectionModel().clearSelection();
        }
        if (!"baby".equals(current)) {
            babyListView.getSelectionModel().clearSelection();
        }
    }

    // Remplir le formulaire avec les données du passager sélectionné
    private void fillForm(Passenger passenger) {
        idField.setText(passenger.getId());
        firstNameField.setText(passenger.getFirstName());
        lastNameField.setText(passenger.getLastName());
        ageField.setText(String.valueOf(passenger.getAge()));
        addressField.setText(passenger.getAddress());
        countryCombo.setValue(passenger.getCountry());
        mealCombo.setValue(passenger.getMeal());
        luggageCheckBox.setSelected(passenger.getLuggage());
    }

    @FXML
    private void handleApply(ActionEvent event) {
        try {
            // Vérifier les champs requis
            if (idField.getText().isEmpty() || firstNameField.getText().isEmpty() ||
                    lastNameField.getText().isEmpty() || ageField.getText().isEmpty() ||
                    addressField.getText().isEmpty() || countryCombo.getValue() == null ||
                    mealCombo.getValue() == null) {
                showAlert("Please fill all fields");
                return;
            }

            // Vérification que l'âge est un nombre
            int age;
            try {
                age = Integer.parseInt(ageField.getText());
            } catch (NumberFormatException e) {
                showAlert("Age must be a number");
                return;
            }

            // Créer ou mettre à jour le passager
            Passenger passenger = new Passenger(
                    idField.getText(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    age,
                    addressField.getText(),
                    countryCombo.getValue(),
                    mealCombo.getValue(),
                    luggageCheckBox.isSelected()
            );

            // Ajouter ou mettre à jour dans la liste principale
            if (selectedPassenger != null) {
                int index = passengers.indexOf(selectedPassenger);
                if (index >= 0) {
                    passengers.set(index, passenger);
                } else {
                    passengers.add(passenger);
                }
            } else {
                passengers.add(passenger);
            }

            // Mettre à jour les ListView par catégorie
            updateListViews();

            // Réinitialiser la sélection et le formulaire
            selectedPassenger = null;
            clearFields();

        } catch (Exception e) {
            showAlert("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleNext(ActionEvent event) {
        // Pour l'instant, simplement effacer les champs
        clearFields();
    }

    // Méthode pour re-classer les passagers dans les ListView en fonction de leur catégorie
    private void updateListViews() {
        // On vide d'abord chaque ListView
        adultListView.getItems().clear();
        childListView.getItems().clear();
        babyListView.getItems().clear();

        for (Passenger p : passengers) {
            switch (p.getCategory()) {
                case "Adulte":
                    adultListView.getItems().add(p);
                    break;
                case "Enfant":
                    childListView.getItems().add(p);
                    break;
                case "Bébé":
                    babyListView.getItems().add(p);
                    break;
            }
        }
    }

    private void clearFields() {
        idField.clear();
        firstNameField.clear();
        lastNameField.clear();
        ageField.clear();
        addressField.clear();
        countryCombo.setValue("USA");
        mealCombo.setValue("Basic");
        luggageCheckBox.setSelected(false);

        // Effacer aussi les sélections dans les ListView
        adultListView.getSelectionModel().clearSelection();
        childListView.getSelectionModel().clearSelection();
        babyListView.getSelectionModel().clearSelection();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
