package com.siddhesh.co2calculator.service;

import com.siddhesh.co2calculator.exception.CityNotFoundException;
import com.siddhesh.co2calculator.exception.MissingApiKeyException;
import com.siddhesh.co2calculator.model.CityCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.json.JSONArray;

@Service
public class GeoService {

    // API Key to authenticate requests to the OpenRouteService API
    @Value("${ORS_TOKEN}")
    private String apiKey;

    private final RestTemplate restTemplate;

    // Constructor to initialize GeoService with the API key and RestTemplate instance
    @Autowired
    public GeoService(@Value("${ORS_TOKEN}") String apiKey, RestTemplate restTemplate) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new MissingApiKeyException("API key is missing. Set the ORS_TOKEN environment variable.");
        }
        this.apiKey = apiKey;
        this.restTemplate = restTemplate;
    }

    // Retrieves the geographical coordinates (latitude and longitude) for a given city name
    public CityCoordinates getCityCoordinates(String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new CityNotFoundException("City name cannot be empty.");
        }

        String url = "https://api.openrouteservice.org/geocode/search?api_key=" + apiKey + "&text=" + city + "&layers=locality";

        try {
            // Send a GET request to the OpenRouteService API to get city coordinates
            String response = restTemplate.getForObject(url, String.class);
            if (response == null || response.isEmpty()) {
                throw new IllegalArgumentException("Invalid response from API");
            }

            // Parse the response to extract city coordinates
            JSONObject jsonResponse = new JSONObject(response);

            // Check if the response contains valid city data
            if (!jsonResponse.has("features") || jsonResponse.getJSONArray("features").length() == 0) {
                throw new CityNotFoundException("City '" + city + "' not found.");
            }

            // Extract coordinates (latitude and longitude)
            JSONArray coordinates = jsonResponse.getJSONArray("features").getJSONObject(0)
                    .getJSONObject("geometry").getJSONArray("coordinates");

            // Return the coordinates in a CityCoordinates object
            return new CityCoordinates(coordinates.getDouble(0), coordinates.getDouble(1));
        } catch (HttpClientErrorException e) {
            // Handle case where city is not found or API response is invalid
            throw new CityNotFoundException("City not found or invalid API response.");
        }
    }

    // Calculates the distance between two cities (start and end) using the OpenRouteService API
    public double getDistanceBetweenCities(CityCoordinates start, CityCoordinates end) {
        String url = "https://api.openrouteservice.org/v2/matrix/driving-car";

        // Prepare the JSON body for the API request with the locations and metrics
        JSONObject body = new JSONObject();
        body.put("locations", new double[][]{
                {start.getLatitude(), start.getLongitude()},
                {end.getLatitude(), end.getLongitude()}
        });
        body.put("metrics", new String[]{"distance"});

        try {
            // Set headers for the request, including the API key for authentication
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", apiKey);

            // Create the request entity with the headers and body
            HttpEntity<String> requestEntity = new HttpEntity<>(body.toString(), headers);

            // Make the POST request to the API and retrieve the response
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            // Parse the JSON response to extract the distance
            JSONObject jsonResponse = new JSONObject(responseEntity.getBody());

            // Return the distance between the cities in kilometers
            return jsonResponse.getJSONArray("distances").getJSONArray(0).getDouble(1) / 1000.0; // Convert from meters to kilometers
        } catch (HttpClientErrorException e) {
            // Handle cases where the API request fails (e.g., service unavailable)
            throw new RestClientException("Failed to fetch distance. API may be down.");
        }
    }
}
