package com.siddhesh.co2calculator.controller;

import com.siddhesh.co2calculator.exception.CityNotFoundException;
import com.siddhesh.co2calculator.exception.InvalidTransportationMethodException;
import com.siddhesh.co2calculator.model.CityCoordinates;
import com.siddhesh.co2calculator.service.Co2CalculatorService;
import com.siddhesh.co2calculator.service.GeoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class Co2CalculatorControllerTest {

    @Mock
    private GeoService geoService; // Mock the GeoService to simulate the external API for city coordinates

    @Mock
    private Co2CalculatorService co2CalculatorService; // Mock the Co2CalculatorService to simulate CO2 emission calculations

    @InjectMocks
    private Co2CalculatorController co2CalculatorController; // Inject the mocked services into the controller

    private MockMvc mockMvc; // MockMvc to simulate HTTP requests and test controller methods

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks before each test
        mockMvc = MockMvcBuilders.standaloneSetup(co2CalculatorController).build(); // Set up MockMvc for the controller
    }

    // Test case to handle invalid transportation methods
    @Test
    public void testCalculateCo2_InvalidTransportationMethod() throws Exception {
        // Mock the response for city coordinates (Hamburg and Berlin)
        CityCoordinates berlin = new CityCoordinates(13.405, 52.52);
        CityCoordinates hamburg = new CityCoordinates(9.9937, 53.5511);
        when(geoService.getCityCoordinates("Berlin")).thenReturn(berlin); // Mock Berlin coordinates
        when(geoService.getCityCoordinates("Hamburg")).thenReturn(hamburg); // Mock Hamburg coordinates

        // Mock the response to throw an exception when an invalid transportation method is used
        when(co2CalculatorService.calculateCo2Emission(Mockito.anyDouble(), Mockito.anyString()))
                .thenThrow(new InvalidTransportationMethodException("Invalid transportation method"));

        // Perform the test by calling the controller method with invalid transportation method
        mockMvc.perform(get("/api/v1/co2-calculator")
                        .param("start", "Hamburg") // Starting city
                        .param("end", "Berlin") // Destination city
                        .param("transportation-method", "invalid-method")) // Invalid transportation method
                .andExpect(status().isBadRequest()) // Expect HTTP 400 (Bad Request)
                .andExpect(jsonPath("$.response").value("Invalid transportation method")); // Expect error message in response
    }

    @Test
    public void testCalculateCo2_Success() throws Exception {
        // Mock city coordinates (Berlin and Hamburg)
        CityCoordinates berlin = new CityCoordinates(13.405, 52.52);
        CityCoordinates hamburg = new CityCoordinates(9.9937, 53.5511);

        // Mock successful city coordinate responses
        when(geoService.getCityCoordinates("Berlin")).thenReturn(berlin);
        when(geoService.getCityCoordinates("Hamburg")).thenReturn(hamburg);

        // Mock the response for successful CO2 emission calculation
        double distance = 300.0; // Example distance in km
        double co2Emission = 49.2; // Example CO2 emission

        when(geoService.getDistanceBetweenCities(hamburg, berlin)).thenReturn(distance);
        when(co2CalculatorService.calculateCo2Emission(distance, "diesel-car-medium")).thenReturn(co2Emission);

        // Perform the test by calling the controller method with valid data
        mockMvc.perform(get("/api/v1/co2-calculator")
                        .param("start", "Hamburg")
                        .param("end", "Berlin")
                        .param("transportation-method", "diesel-car-medium"))
                .andExpect(status().isOk()) // Expect HTTP 200 (OK)
                .andExpect(jsonPath("$.response").value("Your trip caused 49.2kg of CO2-equivalent.")); // Validate the response message
    }

    @Test
    public void testCalculateCo2_CityNotFound() throws Exception {
        // Mock city coordinates to throw an exception for a non-existing city
        when(geoService.getCityCoordinates("NonExistentCity")).thenThrow(new CityNotFoundException("City not found"));

        // Perform the test by calling the controller method with a non-existing city
        mockMvc.perform(get("/api/v1/co2-calculator")
                        .param("start", "NonExistentCity") // Invalid start city
                        .param("end", "Berlin") // Valid destination city
                        .param("transportation-method", "diesel-car-medium")) // Valid transportation method
                .andExpect(status().isBadRequest()) // Expect HTTP 400 (Bad Request)
                .andExpect(jsonPath("$.response").value("City not found")); // Expect error message in response
    }

    @Test
    public void testCalculateCo2_InternalServerError() throws Exception {
        // Mock geoService to throw an unexpected error
        when(geoService.getCityCoordinates("Berlin")).thenThrow(new RuntimeException("Unexpected error"));

        // Perform the test by calling the controller method, expecting an internal server error
        mockMvc.perform(get("/api/v1/co2-calculator")
                        .param("start", "Hamburg")
                        .param("end", "Berlin")
                        .param("transportation-method", "diesel-car-medium"))
                .andExpect(status().isInternalServerError()) // Expect HTTP 500 (Internal Server Error)
                .andExpect(jsonPath("$.response").value("An unexpected error occurred.")); // Expect error message in response
    }
}

