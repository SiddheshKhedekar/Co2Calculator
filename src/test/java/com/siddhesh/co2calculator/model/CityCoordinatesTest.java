package com.siddhesh.co2calculator.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CityCoordinatesTest {

    @Test
    void testConstructorAndGetters() {
        double latitude = 48.137154;
        double longitude = 11.576124;

        CityCoordinates cityCoordinates = new CityCoordinates(latitude, longitude);

        assertEquals(latitude, cityCoordinates.getLatitude(), 0.000001);
        assertEquals(longitude, cityCoordinates.getLongitude(), 0.000001);
    }
}
