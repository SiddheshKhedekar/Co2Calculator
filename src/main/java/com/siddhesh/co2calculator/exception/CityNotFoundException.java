package com.siddhesh.co2calculator.exception;
// Custom exception for when a city is not found
public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException(String message) {
        super(message);
    }
}

