package com.example.demo1.client.models;

import javafx.beans.property.*;

//Dev : Ibrahim
//Pour la Partie 4
public class Passenger {
    private final StringProperty id;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final IntegerProperty age;
    private final StringProperty address;
    private final StringProperty country;
    private final StringProperty meal;
    private final BooleanProperty luggage;

    public Passenger(String id, String firstName, String lastName, int age,
                     String address, String country, String meal, boolean luggage) {
        this.id = new SimpleStringProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.age = new SimpleIntegerProperty(age);
        this.address = new SimpleStringProperty(address);
        this.country = new SimpleStringProperty(country);
        this.meal = new SimpleStringProperty(meal);
        this.luggage = new SimpleBooleanProperty(luggage);
    }

    // Getters for properties
    public StringProperty idProperty() { return id; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty lastNameProperty() { return lastName; }
    public IntegerProperty ageProperty() { return age; }
    public StringProperty addressProperty() { return address; }
    public StringProperty countryProperty() { return country; }
    public StringProperty mealProperty() { return meal; }
    public BooleanProperty luggageProperty() { return luggage; }

    // Standard getters
    public String getId() { return id.get(); }
    public String getFirstName() { return firstName.get(); }
    public String getLastName() { return lastName.get(); }
    public int getAge() { return age.get(); }
    public String getAddress() { return address.get(); }
    public String getCountry() { return country.get(); }
    public String getMeal() { return meal.get(); }
    public boolean getLuggage() { return luggage.get(); }

    // Méthode pour déterminer la catégorie du passager
    public String getCategory() {
        int ageValue = getAge();
        if (ageValue < 3) {
            return "Bébé";
        } else if (ageValue < 18) {
            return "Enfant";
        } else {
            return "Adulte";
        }
    }

    // Permet un affichage succinct dans les ListView
    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + " (" + getCategory() + ")";
    }
}




