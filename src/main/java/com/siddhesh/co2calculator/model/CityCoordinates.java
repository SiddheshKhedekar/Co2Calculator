package com.siddhesh.co2calculator.model;
// Represents the coordinates (latitude and longitude) of a city
public class CityCoordinates {

    private double latitude;  // Latitude of the city
    private double longitude; // Longitude of the city

    public CityCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

}
