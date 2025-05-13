package com.siddhesh.co2calculator.config;

import com.siddhesh.co2calculator.model.TransportationMethod;
import com.siddhesh.co2calculator.repository.TransportationMethodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DataLoaderTest {

    private TransportationMethodRepository repository;
    private DataLoader dataLoader;

    @BeforeEach
    void setUp() {
        repository = mock(TransportationMethodRepository.class);
        dataLoader = new DataLoader(repository);
    }

    @Test
    void testRun_shouldSaveAllTransportationMethods() {
        dataLoader.run();

        ArgumentCaptor<List<TransportationMethod>> captor = ArgumentCaptor.forClass(List.class);
        verify(repository, times(1)).saveAll(captor.capture());

        List<TransportationMethod> savedMethods = captor.getValue();

        assertEquals(14, savedMethods.size(), "Expected 14 transportation methods");

        assertTrue(savedMethods.stream().anyMatch(m -> m.getName().equals("diesel-car-small") && m.getCo2EmissionPerKm() == 142));
        assertTrue(savedMethods.stream().anyMatch(m -> m.getName().equals("train-default") && m.getCo2EmissionPerKm() == 6));
    }
}
