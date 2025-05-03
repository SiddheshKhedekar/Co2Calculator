package com.siddhesh.co2calculator.exception;
// Custom exception for invalid transportation methods
public class InvalidTransportationMethodException extends RuntimeException {
    public InvalidTransportationMethodException(String message) {
        super(message);
    }
}
