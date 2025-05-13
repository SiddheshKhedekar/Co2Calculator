package com.siddhesh.co2calculator.service;

import com.siddhesh.co2calculator.exception.InvalidTransportationMethodException;
import com.siddhesh.co2calculator.model.TransportationMethod;
import com.siddhesh.co2calculator.repository.TransportationMethodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

public class Co2CalculationServiceTest {

    @Mock
    private TransportationMethodRepository repository; // Mock the repository for testing

    @InjectMocks
    private Co2CalculatorService service; // Inject the mock repository into the service class

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // Initialize the mocks before each test
    }

    // Test to check the behavior when an invalid transportation method is passed
    @Test
    public void testInvalidTransportationMethod_getEmissionFactor() {
        // Mock repository response to return empty when an invalid transportation method is queried
        when(repository.findByName("invalid-method")).thenReturn(Optional.empty());

        // Assert that the exception is thrown when the service method is called with the invalid method
        InvalidTransportationMethodException exception = assertThrows(InvalidTransportationMethodException.class, () -> {
            service.getEmissionFactor("invalid-method");
        });

        assertEquals("Transportation method 'invalid-method' is not supported.", exception.getMessage());
    }

    @Test
    public void testInvalidTransportationMethod_calculateCo2Emission() {
        when(repository.findByName("unknown")).thenReturn(Optional.empty());

        InvalidTransportationMethodException exception = assertThrows(InvalidTransportationMethodException.class, () -> {
            service.calculateCo2Emission(120, "unknown");
        });

        assertEquals("Transportation method 'unknown' is not supported.", exception.getMessage());
    }

    @Test
    public void testGetEmissionFactor_ValidMethod() {
        TransportationMethod electricCar = new TransportationMethod("electric-car-small", 50.0);
        when(repository.findByName("electric-car-small")).thenReturn(Optional.of(electricCar));

        double emissionFactor = service.getEmissionFactor("electric-car-small");

        assertEquals(50.0, emissionFactor);
    }

    // Test to check if CO2 emissions are correctly calculated for a valid transportation method
    @Test
    public void testCalculateCo2Emission_Valid() {
        double distance = 100.0;
        TransportationMethod dieselCarMedium = new TransportationMethod("diesel-car-medium", 171); // Example transportation method with 171 g/km CO2 emission

        // Mock repository response to return the diesel car transportation method
        when(repository.findByName("diesel-car-medium")).thenReturn(Optional.of(dieselCarMedium));

        // Calculate the CO2 emission for the given distance and transportation method
        double co2Emission = service.calculateCo2Emission(distance, "diesel-car-medium");

        // Assert the correct CO2 emission is calculated (171 * 100 / 1000 = 17.1 kg)
        assertEquals(17.1, co2Emission, 0.01); // Allow a tolerance of 0.01 for floating-point comparison
    }

    @Test
    public void testCalculateCo2Emission_ZeroDistance() {
        TransportationMethod train = new TransportationMethod("train-default", 6.0);
        when(repository.findByName("train-default")).thenReturn(Optional.of(train));

        double result = service.calculateCo2Emission(0.0, "train-default");

        assertEquals(0.0, result);
    }
}
