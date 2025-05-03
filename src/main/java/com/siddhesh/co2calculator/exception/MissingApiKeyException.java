package com.siddhesh.co2calculator.exception;
// Custom exception for missing API keys
public class MissingApiKeyException extends RuntimeException {
    public MissingApiKeyException(String message) {
        super(message);
    }
}

