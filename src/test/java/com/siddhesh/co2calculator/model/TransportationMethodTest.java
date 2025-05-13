package com.siddhesh.co2calculator.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransportationMethodTest {

    @Test
    void testConstructorAndGetters() {
        String name = "electric-car-small";
        double emission = 50.0;

        TransportationMethod method = new TransportationMethod(name, emission);

        assertEquals(name, method.getName());
        assertEquals(emission, method.getCo2EmissionPerKm(), 0.000001);
    }

    @Test
    void testSetName() {
        TransportationMethod method = new TransportationMethod("diesel-car", 142.0);

        method.setName("updated-car-name");

        assertEquals("updated-car-name", method.getName());
    }
}
