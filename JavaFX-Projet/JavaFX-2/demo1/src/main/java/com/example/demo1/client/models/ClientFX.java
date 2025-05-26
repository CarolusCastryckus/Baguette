package com.example.demo1.client.models;

import com.example.demo1.common.models.Client;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClientFX {

    private final StringProperty nationalId = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();
    private final StringProperty country = new SimpleStringProperty();
    private final StringProperty meal = new SimpleStringProperty();
    private final StringProperty luggage = new SimpleStringProperty();

    public ClientFX() {}

    public ClientFX(String nationalId, String firstName, String lastName, String address,
                    String country, String meal, String luggage) {
        this.nationalId.set(nationalId);
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.address.set(address);
        this.country.set(country);
        this.meal.set(meal);
        this.luggage.set(luggage);
    }

    public ClientFX(Client client) {
        this.nationalId.set(client.getNationalId());
        this.firstName.set(client.getFirstName());
        this.lastName.set(client.getLastName());
        this.address.set(client.getAddress());
        this.country.set(client.getCountry());
        this.meal.set(client.getMeal());
        this.luggage.set(client.getLuggage());
    }

    public Client toSerializableClient() {
        return new Client(
                getNationalId(), getFirstName(), getLastName(), getAddress(),
                getCountry(), getMeal(), getLuggage()
        );
    }

    public StringProperty nationalIdProperty() { return nationalId; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty addressProperty() { return address; }
    public StringProperty countryProperty() { return country; }
    public StringProperty mealProperty() { return meal; }
    public StringProperty luggageProperty() { return luggage; }

    public String getNationalId() { return nationalId.get(); }
    public String getFirstName() { return firstName.get(); }
    public String getLastName() { return lastName.get(); }
    public String getAddress() { return address.get(); }
    public String getCountry() { return country.get(); }
    public String getMeal() { return meal.get(); }
    public String getLuggage() { return luggage.get(); }

    public void setNationalId(String value) { nationalId.set(value); }
    public void setFirstName(String value) { firstName.set(value); }
    public void setLastName(String value) { lastName.set(value); }
    public void setAddress(String value) { address.set(value); }
    public void setCountry(String value) { country.set(value); }
    public void setMeal(String value) { meal.set(value); }
    public void setLuggage(String value) { luggage.set(value); }

    @Override
    public String toString() {
        return "ClientFX{" +
                "nationalId=" + getNationalId() +
                ", firstName=" + getFirstName() +
                ", lastName=" + getLastName() +
                ", address=" + getAddress() +
                ", country=" + getCountry() +
                ", meal=" + getMeal() +
                ", luggage=" + getLuggage() +
                '}';
    }
}
