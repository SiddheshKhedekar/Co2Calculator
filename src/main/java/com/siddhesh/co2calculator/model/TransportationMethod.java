package com.siddhesh.co2calculator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// Represents a transportation method with a name and associated CO2 emission per kilometer
@Entity
public class TransportationMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID for the entity
    private Long id;

    private String name; // Name of the transportation method
    private double co2EmissionPerKm; // CO2 emission per kilometer for the transportation method

    public TransportationMethod(String name, double co2EmissionPerKm) {
        this.name = name;
        this.co2EmissionPerKm = co2EmissionPerKm;
    }

    public double getCo2EmissionPerKm() {
        return co2EmissionPerKm;
    }

}
