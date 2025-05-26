package com.example.demo1.client.viewControllers;

import com.example.demo1.client.controllers.MainApplication;
import com.example.demo1.client.models.Flight;
import com.example.demo1.client.services.NetworkService;
import com.example.demo1.common.models.Client;
import com.example.demo1.common.models.ReservationRecord;
import com.example.demo1.common.models.ReservationResponse;
import com.example.demo1.common.models.SuccessResponse;
import com.example.demo1.server.repositories.FlightDatabaseManagement;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

//Dev : Malik
//Pour la Partie 3

public class VoyageViewController implements NetworkService.Listener {

    @FXML private ChoiceBox<String> tripTypeChoice;
    @FXML private ComboBox<String> fromCombo;
    @FXML private ComboBox<String> toCombo;
    @FXML private DatePicker departureDatePicker;
    @FXML private Spinner<Integer> adultSpinner;
    @FXML private Spinner<Integer> childSpinner;
    @FXML private Spinner<Integer> babySpinner;
    @FXML private ChoiceBox<String> seatTypeChoice;
    @FXML private Button researchButton;

    @FXML private TableView<Flight> flightsTable;
    @FXML private TableColumn<Flight, String> flightNumberColumn;
    @FXML private TableColumn<Flight, String> airlineColumn;
    @FXML private TableColumn<Flight, String> departureColumn;
    @FXML private TableColumn<Flight, String> arrivalColumn;
    @FXML private TableColumn<Flight, String> timeColumn;
    @FXML private TableColumn<Flight, Number> priceColumn;
    @FXML private TableColumn<Flight, LocalDate> departureDateColumn;

    @FXML private Button logoutButton;   // ← bouton “Retour à la connexion”
    @FXML private Button nextButton;   // ← bouton “Retour à la connexion”

    // Pour l’onglet “Mes réservations”
    @FXML private TableView<ReservationRecord> reservationTable;
    @FXML private TableColumn<ReservationRecord, String> resFlightColumn;
    @FXML private TableColumn<ReservationRecord, LocalDate> resDateColumn;
    @FXML private TableColumn<ReservationRecord, Integer> resSeatsColumn;
    @FXML private Button btnCancel;


    private final ObservableList<Flight> flights = FXCollections.observableArrayList();
    private final ObservableList<ReservationRecord> reservations = FXCollections.observableArrayList();
    private final NetworkService networkService = new NetworkService();
    private Flight selectedFlight;

    @FXML
    public void initialize() {

        // 1) Connexion + listener
        try {
            networkService.connect("localhost", 7000);
            networkService.setListener(this);
        } catch (IOException e) {
            showError("Impossible de se connecter au serveur.");
        }

        // 2) Navigation
        //logoutButton.setOnAction(evt -> MainApplication.showPassengerView());
        // navigation vers l’écran des sièges
        nextButton.setOnAction(evt -> MainApplication.showPassengerView());

        // 3) Initialisation des contrôles de recherche
        departureDatePicker.setValue(LocalDate.now().plusDays(1));
        initCityCombos();
        initTripTypeChoice();
        initPassengerSpinners();
        initSeatTypeChoice();
        initFlightTable();

        researchButton.setOnAction(e -> onNextClick());

        // 4) Sélection du vol
        flightsTable.getSelectionModel().selectedItemProperty()
                .addListener((obs,o,n) -> selectedFlight = n);

        // 5) Onglet “Mes réservations” : configurer la table et le bouton Annuler
        // 5) Onglet “Mes réservations” : configurer la table et le bouton Annuler
        resFlightColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFlightNumber())
        );
        resDateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getReservedDate())
        );
        resSeatsColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getSeats()).asObject()
        );

// n’oublie pas de lier ton ObservableList au TableView :
        reservationTable.setItems(reservations);

        btnCancel.setOnAction(e -> {
            ReservationRecord sel = reservationTable.getSelectionModel().getSelectedItem();
            if (sel != null) {
                try {
                    networkService.cancelReservation(sel.getFlightNumber(), sel.getSeats());
                } catch (IOException ex) {
                    showError("Erreur réseau : " + ex.getMessage());
                }
            }
        });
    }


    private void initTripTypeChoice() {
        tripTypeChoice.getItems().addAll("One way", "Round trip");
        tripTypeChoice.setValue("One way");
    }

    private void initCityCombos() {
        List<String> airportCodes = List.of("PAR", "NYC", "LON", "DXB", "TYO");
        fromCombo.getItems().addAll(airportCodes);
        toCombo.getItems().addAll(airportCodes);
        fromCombo.setValue("PAR");
        toCombo.setValue("NYC");
    }

    private void initPassengerSpinners() {
        adultSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        childSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 0));
        babySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 3, 0));
    }

    private void initSeatTypeChoice() {
        seatTypeChoice.getItems().addAll("ECONOMY", "BUSINESS");
        seatTypeChoice.setValue("ECONOMY");
    }

    private void initFlightTable() {
        flightNumberColumn.setCellValueFactory(data -> data.getValue().flightNumberProperty());
        airlineColumn.setCellValueFactory(data -> data.getValue().airlineProperty());
        departureColumn.setCellValueFactory(data -> data.getValue().departureAirportProperty());
        arrivalColumn.setCellValueFactory(data -> data.getValue().arrivalAirportProperty());
        timeColumn.setCellValueFactory(data -> data.getValue().durationProperty());
        priceColumn.setCellValueFactory(data -> data.getValue().priceProperty());
        departureDateColumn.setCellValueFactory(data -> data.getValue().departureDateProperty());
    }

    private void onNextClick() {
        if (fromCombo.getValue().equals(toCombo.getValue())) {
            showError("Les villes de départ et d'arrivée doivent être différentes");
            return;
        }
        // on appelle maintenant le service réseau
        try {
            networkService.searchFlights(
                    fromCombo.getValue(),
                    toCombo.getValue(),
                    departureDatePicker.getValue()
            );
        } catch (IOException ex) {
            showError("Erreur réseau : " + ex.getMessage());
        }
    }

    private void searchFlights() {
        flights.setAll(FlightDatabaseManagement.getInstance().searchFlights(
                fromCombo.getValue(),
                toCombo.getValue(),
                departureDatePicker.getValue()
        ));

        if (flights.isEmpty()) {
            showInfo("Information", "Aucun vol disponible pour ces critères");
        } else {
            flightsTable.setItems(flights);
        }
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).show();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    @Override
    public void onClientAdded(SuccessResponse response) {
        /* non utilisé ici */
    }

    @Override
    public void onClientUpdated(SuccessResponse response) {
        /* non utilisé ici */
    }

    @Override
    public void onClientDeleted(SuccessResponse response) {
        /* non utilisé ici */
    }

    @Override
    public void onClientListReceived(List<Client> clients) {
        /* non utilisé ici */
    }

    /** réception de la liste des vols depuis le serveur */
    @Override
    public void onFlightListReceived(List<Flight> flights) {
        Platform.runLater(() -> {
            this.flights.setAll(flights);
            if (flights.isEmpty()) {
                showInfo("Information", "Aucun vol disponible pour ces critères");
            } else {
                flightsTable.setItems(this.flights);
            }
        });

    }

    /** réponse de réservation (non utilisé ici, à implémenter plus tard si besoin) */
    @Override
    public void onReservationResponse(ReservationResponse response) {
        // après annulation : rafraîchir réservations + vols
        Platform.runLater(() -> {
            showInfo("", response.getMessage());
            try {
                networkService.searchFlights(
                        fromCombo.getValue(), toCombo.getValue(), departureDatePicker.getValue());
                networkService.loadMyReservations();
            } catch (IOException e) {
                showError("Erreur réseau : " + e.getMessage());
            }
        });
    }

    /** réception de la liste des réservations (non utilisé ici) */
    @Override
    public void onReservationsListReceived(List<ReservationRecord> recs) {
        Platform.runLater(() -> reservations.setAll(recs));
    }
}