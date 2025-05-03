package com.siddhesh.co2calculator.service;

import com.siddhesh.co2calculator.exception.CityNotFoundException;
import com.siddhesh.co2calculator.exception.MissingApiKeyException;
import com.siddhesh.co2calculator.model.CityCoordinates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GeoServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private GeoService geoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        geoService = new GeoService("mock-api-key", restTemplate); // Initialize GeoService with mock RestTemplate
    }

    // Test to check if an exception is thrown when the API key is missing
    @Test
    public void testMissingApiKey() {
        MissingApiKeyException exception = assertThrows(MissingApiKeyException.class, () -> {
            new GeoService(null, restTemplate); // Passing null API key
        });

        assertEquals("API key is missing. Set the ORS_TOKEN environment variable.", exception.getMessage());
    }

    // Test to check if the service throws an exception for invalid city input
    @Test
    public void testGetCityCoordinates_InvalidCity() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("{ \"features\": [] }"); // Simulate no results

        CityNotFoundException exception = assertThrows(CityNotFoundException.class, () -> {
            geoService.getCityCoordinates("InvalidCity"); // Pass an invalid city name
        });

        assertEquals("City 'InvalidCity' not found.", exception.getMessage());
    }

    // Test to check if the service returns correct coordinates for a valid city
    @Test
    public void testGetCityCoordinates() {
        // Simulate a valid response for Berlin city
        String jsonResponse = "{ \"features\": [ { \"geometry\": { \"coordinates\": [13.405, 52.52] } } ] }";
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);

        // Call the method and assert the results
        CityCoordinates coords = geoService.getCityCoordinates("Berlin");

        assertEquals(13.405, coords.getLatitude());
        assertEquals(52.52, coords.getLongitude());
    }
}