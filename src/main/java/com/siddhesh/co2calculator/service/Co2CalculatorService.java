package com.siddhesh.co2calculator.service;

import com.siddhesh.co2calculator.repository.TransportationMethodRepository;
import com.siddhesh.co2calculator.exception.InvalidTransportationMethodException;
import com.siddhesh.co2calculator.model.TransportationMethod;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class Co2CalculatorService {

    private final TransportationMethodRepository repository;

    public Co2CalculatorService(TransportationMethodRepository repository) {
        this.repository = repository;
    }

    // Retrieves the CO2 emission factor for a given transportation method
    public double getEmissionFactor(String transportationMethod) {
        Optional<TransportationMethod> method = repository.findByName(transportationMethod);
        if (method.isEmpty()) {
            throw new InvalidTransportationMethodException("Transportation method '" + transportationMethod + "' is not supported.");
        }
        return method.get().getCo2EmissionPerKm();  // Return the emission factor (CO2 per km)
    }

    // Calculates the total CO2 emission for a trip given the distance and transportation method
    public double calculateCo2Emission(double distance, String transportationMethod) {
        Optional<TransportationMethod> methodOpt = repository.findByName(transportationMethod);

        if (methodOpt.isEmpty()) {
            throw new InvalidTransportationMethodException("Transportation method '" + transportationMethod + "' is not supported.");
        }

        TransportationMethod method = methodOpt.get();
        // Calculate the total emission and convert grams to kilograms
        return (distance * method.getCo2EmissionPerKm()) / 1000.0;
    }
}
