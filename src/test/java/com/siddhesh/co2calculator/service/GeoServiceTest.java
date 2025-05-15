package com.siddhesh.co2calculator.service;

import com.siddhesh.co2calculator.exception.CityNotFoundException;
import com.siddhesh.co2calculator.exception.MissingApiKeyException;
import com.siddhesh.co2calculator.model.CityCoordinates;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
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

    @Test
    public void testGetCityCoordinates_NullCity() {
        CityNotFoundException exception = assertThrows(CityNotFoundException.class, () -> {
            geoService.getCityCoordinates(null);
        });

        assertEquals("City name cannot be empty.", exception.getMessage());
    }

    @Test
    public void testGetCityCoordinates_EmptyCity() {
        CityNotFoundException exception = assertThrows(CityNotFoundException.class, () -> {
            geoService.getCityCoordinates(" ");
        });

        assertEquals("City name cannot be empty.", exception.getMessage());
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

    @Test
    public void testGetCityCoordinates_ApiReturnsNull() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            geoService.getCityCoordinates("Berlin");
        });

        assertEquals("Invalid response from API", exception.getMessage());
    }

    @Test
    public void testGetCityCoordinates_HttpClientErrorException() {
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenThrow(HttpClientErrorException.class);

        CityNotFoundException exception = assertThrows(CityNotFoundException.class, () -> {
            geoService.getCityCoordinates("Berlin");
        });

        assertEquals("City not found or invalid API response.", exception.getMessage());
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

    @Test
    public void testGetCityCoordinates_MultipleFeatures() {
        String jsonResponse = "{ \"features\": [ { \"geometry\": { \"coordinates\": [13.405, 52.52] } }, { \"geometry\": { \"coordinates\": [2.3522, 48.8566] } } ] }";
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);

        // Testing if the first city coordinates (Berlin) are returned correctly
        CityCoordinates coords = geoService.getCityCoordinates("Berlin");

        assertEquals(13.405, coords.getLatitude());
        assertEquals(52.52, coords.getLongitude());
    }

    @Test
    public void testGetCityCoordinates_MalformedJson() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("{ \"features\": [ { \"geometry\": { \"coordinates\": \"not-an-array\" } } ] }");

        JSONException exception = assertThrows(JSONException.class, () -> {
            geoService.getCityCoordinates("Berlin");
        });

        assertTrue(exception.getMessage().contains("coordinates"));
    }

    @Test
    public void testGetCityCoordinates_EmptyResponse() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            geoService.getCityCoordinates("Berlin");
        });

        assertEquals("Invalid response from API", exception.getMessage());
    }

    @Test
    public void testGetDistanceBetweenCities_Valid() {
        CityCoordinates start = new CityCoordinates(13.4050, 52.5200); // Berlin
        CityCoordinates end = new CityCoordinates(11.5820, 48.1351);   // Munich

        String jsonResponse = "{ \"distances\": [[0, 584000]] }"; // e.g., 584 km

        when(restTemplate.exchange(
                eq("https://api.openrouteservice.org/v2/matrix/driving-car"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(new ResponseEntity<>(jsonResponse, HttpStatus.OK));

        double distance = geoService.getDistanceBetweenCities(start, end);

        assertEquals(584.0, distance); // Because we divided by 1000 in the method
    }

    @Test
    public void testGetDistanceBetweenCities_HttpClientErrorException() {
        CityCoordinates start = new CityCoordinates(13.405, 52.52);
        CityCoordinates end = new CityCoordinates(9.993, 53.551);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenThrow(HttpClientErrorException.class);

        assertThrows(Exception.class, () -> {
            geoService.getDistanceBetweenCities(start, end);
        });
    }
}