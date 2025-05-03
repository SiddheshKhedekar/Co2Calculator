package com.siddhesh.co2calculator.dto;

public class Co2EmissionResponse {

    private String start;  // Starting city for the trip
    private String end;    // Ending city for the trip
    private double distanceKm;  // Distance between the start and end cities in kilometers
    private double co2Kg;  // CO2 emission in kilograms for the trip

    public Co2EmissionResponse(String start, String end, double distanceKm, double co2Kg) {
        this.start = start;
        this.end = end;
        this.distanceKm = distanceKm;
        this.co2Kg = co2Kg;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public double getCo2Kg() {
        return co2Kg;
    }

}
