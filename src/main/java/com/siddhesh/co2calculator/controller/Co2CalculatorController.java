package com.siddhesh.co2calculator.controller;

import com.siddhesh.co2calculator.exception.CityNotFoundException;
import com.siddhesh.co2calculator.exception.InvalidTransportationMethodException;
import com.siddhesh.co2calculator.model.CityCoordinates;
import com.siddhesh.co2calculator.service.Co2CalculatorService;
import com.siddhesh.co2calculator.service.GeoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1")  // Base URL for all endpoints in this controller
public class Co2CalculatorController {

    private final GeoService geoService;
    private final Co2CalculatorService co2CalculatorService;

    public Co2CalculatorController(GeoService geoService, Co2CalculatorService co2CalculatorService) {
        this.geoService = geoService;
        this.co2CalculatorService = co2CalculatorService;
    }

    @GetMapping("/co2-calculator")  // Endpoint to calculate CO2 emissions based on city names and transportation method
    public ResponseEntity<HashMap<String, String>> calculateCo2(
            @RequestParam String start,  // Starting city for the journey
            @RequestParam String end,    // Ending city for the journey
            @RequestParam("transportation-method") String transportMethod) {  // Transportation method used for the journey

        // Initialize response map to return data
        HashMap<String, String> response = new HashMap<>();
        // Retrieve coordinates for both cities
        try {
            CityCoordinates startCoords = geoService.getCityCoordinates(start);
            CityCoordinates endCoords = geoService.getCityCoordinates(end);

            // Calculate distance between the two cities
            double distance = geoService.getDistanceBetweenCities(startCoords, endCoords);

            // Calculate CO2 emissions based on the distance and transport method
            double co2Emission = co2CalculatorService.calculateCo2Emission(distance, transportMethod);
            response.put("response", String.format("Your trip caused %.1fkg of CO2-equivalent.", co2Emission));
            return new ResponseEntity<>(response, HttpStatus.OK);  // Return OK status with the response
        } catch (CityNotFoundException | InvalidTransportationMethodException ex) {
            // Handle known exceptions (invalid city or transport method)
            response.put("response", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);  // Return Bad Request status
        } catch (Exception ex) {
            // Handle any unexpected errors
            response.put("response", "An unexpected error occurred.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  // Return Internal Server Error status
        }
    }

}
