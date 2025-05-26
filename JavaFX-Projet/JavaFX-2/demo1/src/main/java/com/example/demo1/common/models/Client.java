// ===== File: common/models/Client.java (modifié pour nationalId) =====

package com.example.demo1.common.models;

import java.io.Serializable;

public class Client implements Serializable {

    private String nationalId;
    private String firstName;
    private String lastName;
    private String address;
    private String country;
    private String meal;
    private String luggage;

    public Client(String nationalId, String firstName, String lastName, String address,
                  String country, String meal, String luggage) {
        this.nationalId = nationalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.country = country;
        this.meal = meal;
        this.luggage = luggage;
    }

    public String getNationalId() { return nationalId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getCountry() { return country; }
    public String getMeal() { return meal; }
    public String getLuggage() { return luggage; }

    public void setNationalId(String nationalId) { this.nationalId = nationalId; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setAddress(String address) { this.address = address; }
    public void setCountry(String country) { this.country = country; }
    public void setMeal(String meal) { this.meal = meal; }
    public void setLuggage(String luggage) { this.luggage = luggage; }

    @Override
    public String toString() {
        return "Client{" +
                "nationalId='" + nationalId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", meal='" + meal + '\'' +
                ", luggage='" + luggage + '\'' +
                '}';
    }
}