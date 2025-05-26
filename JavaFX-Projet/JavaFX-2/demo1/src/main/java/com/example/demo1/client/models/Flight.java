package com.example.demo1.client.models;

import javafx.beans.property.*;
import java.time.LocalDate;

//Dev : Malik
//Pour la Partie 3
public class Flight {
    private final StringProperty flightNumber;
    private final StringProperty departureAirport;
    private final StringProperty arrivalAirport;
    private final StringProperty departureTime;
    private final StringProperty arrivalTime;
    private final StringProperty duration;
    private final DoubleProperty price;
    private final StringProperty airline;
    private final IntegerProperty seatsAvailable;
    private final ObjectProperty<LocalDate> departureDate;

    public Flight(String flightNumber, String departureAirport, String arrivalAirport,
                  String departureTime, String arrivalTime, String duration,
                  double price, String airline, int seatsAvailable,
                  LocalDate departureDate) {
        this.flightNumber = new SimpleStringProperty(flightNumber);
        this.departureAirport = new SimpleStringProperty(departureAirport);
        this.arrivalAirport = new SimpleStringProperty(arrivalAirport);
        this.departureTime = new SimpleStringProperty(departureTime);
        this.arrivalTime = new SimpleStringProperty(arrivalTime);
        this.duration = new SimpleStringProperty(duration);
        this.price = new SimpleDoubleProperty(price);
        this.airline = new SimpleStringProperty(airline);
        this.seatsAvailable = new SimpleIntegerProperty(seatsAvailable);
        this.departureDate = new SimpleObjectProperty<>(departureDate);
    }

    // Properties (pour TableView)
    public StringProperty flightNumberProperty() { return flightNumber; }
    public StringProperty departureAirportProperty() { return departureAirport; }
    public StringProperty arrivalAirportProperty() { return arrivalAirport; }
    public StringProperty departureTimeProperty() { return departureTime; }
    public StringProperty arrivalTimeProperty() { return arrivalTime; }
    public StringProperty durationProperty() { return duration; }
    public DoubleProperty priceProperty() { return price; }
    public StringProperty airlineProperty() { return airline; }
    public IntegerProperty seatsAvailableProperty() { return seatsAvailable; }
    public ObjectProperty<LocalDate> departureDateProperty() { return departureDate; }

    // Getters
    public String getFlightNumber() { return flightNumber.get(); }
    public String getDepartureAirport() { return departureAirport.get(); }
    public String getArrivalAirport() { return arrivalAirport.get(); }
    public String getDepartureTime() { return departureTime.get(); }
    public String getArrivalTime() { return arrivalTime.get(); }
    public String getDuration() { return duration.get(); }
    public double getPrice() { return price.get(); }
    public String getAirline() { return airline.get(); }
    public int getSeatsAvailable() { return seatsAvailable.get(); }
    public LocalDate getDepartureDate() { return departureDate.get(); }
}