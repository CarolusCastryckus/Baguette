package com.example.demo1.client.viewControllers;

import com.example.demo1.client.controllers.MainApplication;
import com.example.demo1.client.models.ClientFX;
import com.example.demo1.client.models.Flight;
import com.example.demo1.client.services.NetworkService;
import com.example.demo1.common.models.Client;
import com.example.demo1.common.models.ReservationRecord;
import com.example.demo1.common.models.ReservationResponse;
import com.example.demo1.common.models.SuccessResponse;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ClientViewController implements NetworkService.Listener {

    @FXML
    private TextField nationalIdField, firstNameField, lastNameField, addressField, countryField, searchField;

    @FXML
    private ComboBox<String> mealComboBox;

    @FXML
    private CheckBox luggageCheckBox;

    @FXML
    private TableView<ClientFX> clientTable;

    @FXML
    private TableColumn<ClientFX, String> nationalIdColumn, firstNameColumn, lastNameColumn,
            addressColumn, countryColumn, mealColumn, luggageColumn;

    @FXML
    private Label messageLabel;

    @FXML
    private Button createClientButton, updateClientButton, deleteClientButton;

    @FXML
     private Button btnToVoyage;    // ← injection du bouton “Aller aux Passagers”

    private final ObservableList<ClientFX> clientList = FXCollections.observableArrayList();
    private final NetworkService networkService = new NetworkService();

    @FXML
    private void initialize() {

        // --- mise en place de la navigation vers la vue Passagers ---
             btnToVoyage.setOnAction(evt -> MainApplication.showVoyageView());
        try {
            networkService.connect("localhost", 7000);
            networkService.setListener(this);
            networkService.getAllClients();
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Erreur : impossible de se connecter au serveur.");
        }

        mealComboBox.setItems(FXCollections.observableArrayList(
                "Aucun", "Pizza", "Pâtes", "Salade", "Viande", "Poisson", "Végétarien"
        ));
        mealComboBox.setValue("Aucun");

        nationalIdColumn.setCellValueFactory(cellData -> cellData.getValue().nationalIdProperty());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        countryColumn.setCellValueFactory(cellData -> cellData.getValue().countryProperty());
        mealColumn.setCellValueFactory(cellData -> cellData.getValue().mealProperty());
        luggageColumn.setCellValueFactory(cellData -> cellData.getValue().luggageProperty());

        clientTable.setItems(clientList);

        // Filtrage dynamique basé sur le nationalId
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterClientsByNationalId(newVal));

        clientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                nationalIdField.setText(newVal.getNationalId());
                firstNameField.setText(newVal.getFirstName());
                lastNameField.setText(newVal.getLastName());
                addressField.setText(newVal.getAddress());
                countryField.setText(newVal.getCountry());
                mealComboBox.setValue(newVal.getMeal());
                luggageCheckBox.setSelected("Yes".equals(newVal.getLuggage()));
                nationalIdField.setDisable(true);
            } else {
                nationalIdField.setDisable(false);
            }
            toggleButtons();
        });

        addFieldListeners();
        toggleButtons();
    }

    private void filterClientsByNationalId(String query) {
        ObservableList<ClientFX> filteredList = FXCollections.observableArrayList();

        if (query.isEmpty()) {
            // Si la recherche est vide, afficher tous les clients
            filteredList.addAll(clientList);
        } else {
            for (ClientFX client : clientList) {
                if (client.getNationalId().contains(query)) {
                    filteredList.add(client);
                }
            }
        }

        clientTable.setItems(filteredList); // Met à jour la TableView avec les résultats filtrés
    }

    private void addFieldListeners() {
        nationalIdField.textProperty().addListener((obs, oldVal, newVal) -> toggleButtons());
        firstNameField.textProperty().addListener((obs, oldVal, newVal) -> toggleButtons());
        lastNameField.textProperty().addListener((obs, oldVal, newVal) -> toggleButtons());
        addressField.textProperty().addListener((obs, oldVal, newVal) -> toggleButtons());
        countryField.textProperty().addListener((obs, oldVal, newVal) -> toggleButtons());
        mealComboBox.valueProperty().addListener((obs, oldVal, newVal) -> toggleButtons());
        luggageCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> toggleButtons());
    }

    private void toggleButtons() {
        boolean fieldsFilled = !nationalIdField.getText().isEmpty()
                && !firstNameField.getText().isEmpty()
                && !lastNameField.getText().isEmpty()
                && !addressField.getText().isEmpty()
                && !countryField.getText().isEmpty()
                && mealComboBox.getValue() != null;

        createClientButton.setDisable(!fieldsFilled);
        ClientFX selectedClient = clientTable.getSelectionModel().getSelectedItem();
        updateClientButton.setDisable(selectedClient == null);
        deleteClientButton.setDisable(selectedClient == null);
    }

    @FXML
    private void handleCreateClient() {
        if (createClientButton.isDisabled()) {
            messageLabel.setText("Tous les champs doivent être remplis !");
            return;
        }

        ClientFX newClientFX = new ClientFX(
                nationalIdField.getText(),
                firstNameField.getText(),
                lastNameField.getText(),
                addressField.getText(),
                countryField.getText(),
                mealComboBox.getValue(),
                luggageCheckBox.isSelected() ? "Yes" : "No"
        );

        clientList.add(newClientFX);

        try {
            Client clientToSend = newClientFX.toSerializableClient();
            networkService.addClient(clientToSend);
        } catch (Exception e) {
            messageLabel.setText("Erreur lors de l'envoi au serveur : " + e.getMessage());
            e.printStackTrace();
        }

        messageLabel.setText("Client créé avec succès !");
        clearFields();
        toggleButtons();
    }

    @FXML
    private void handleUpdateClient() {
        ClientFX selectedClient = clientTable.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            selectedClient.setFirstName(firstNameField.getText());
            selectedClient.setLastName(lastNameField.getText());
            selectedClient.setAddress(addressField.getText());
            selectedClient.setCountry(countryField.getText());
            selectedClient.setMeal(mealComboBox.getValue());
            selectedClient.setLuggage(luggageCheckBox.isSelected() ? "Yes" : "No");

            try {
                Client clientToUpdate = selectedClient.toSerializableClient();
                networkService.updateClient(clientToUpdate);
                messageLabel.setText("Modification envoyée au serveur !");
            } catch (Exception e) {
                messageLabel.setText("Erreur lors de la mise à jour : " + e.getMessage());
                e.printStackTrace();
            }

            clientTable.refresh();
        } else {
            messageLabel.setText("Veuillez sélectionner un client à mettre à jour.");
        }
        toggleButtons();
    }

    @FXML
    private void handleDeleteClient() {
        ClientFX selectedClient = clientTable.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            try {
                Client clientToDelete = selectedClient.toSerializableClient();
                networkService.deleteClient(clientToDelete);
                clientList.remove(selectedClient);
                messageLabel.setText("Suppression envoyée au serveur !");
            } catch (Exception e) {
                messageLabel.setText("Erreur lors de la suppression : " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            messageLabel.setText("Veuillez sélectionner un client à supprimer.");
        }
        toggleButtons();
    }
    @FXML
    private void handleSearchClient() {
        String query = searchField.getText(); // Récupère le texte saisi dans le champ de recherche
        filterClientsByNationalId(query);  // Applique le filtre sur la liste des clients
    }

    private void clearFields() {
        nationalIdField.clear();
        firstNameField.clear();
        lastNameField.clear();
        addressField.clear();
        countryField.clear();
        mealComboBox.setValue(null);
        luggageCheckBox.setSelected(false);
        nationalIdField.setDisable(false);
    }

    @Override
    public void onClientAdded(SuccessResponse response) {
        messageLabel.setText(response.getMessage());
    }

    @Override
    public void onClientUpdated(SuccessResponse response) {
        messageLabel.setText(response.getMessage());
    }

    @Override
    public void onClientDeleted(SuccessResponse response) {
        messageLabel.setText(response.getMessage());
    }

    @Override
    public void onClientListReceived(List<Client> clients) {
        clientList.clear();
        for (Client client : clients) {
            clientList.add(new ClientFX(client));
        }
        clientTable.refresh();
    }

    @Override
    public void onFlightListReceived(List<Flight> flights) {

    }

    @Override
    public void onReservationResponse(ReservationResponse response) {

    }

    @Override
    public void onReservationsListReceived(List<ReservationRecord> reservations) {

    }

}