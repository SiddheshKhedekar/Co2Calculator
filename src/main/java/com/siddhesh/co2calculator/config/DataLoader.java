package com.siddhesh.co2calculator.config;

import com.siddhesh.co2calculator.model.TransportationMethod;
import com.siddhesh.co2calculator.repository.TransportationMethodRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final TransportationMethodRepository repository;

    public DataLoader(TransportationMethodRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        System.out.println("Loading Transportation methods.");

        // Saving a list of TransportationMethod objects to the repository
        repository.saveAll(List.of(
                new TransportationMethod("diesel-car-small", 142),
                new TransportationMethod("petrol-car-small", 154),
                new TransportationMethod("plugin-hybrid-car-small", 73),
                new TransportationMethod("electric-car-small", 50),
                new TransportationMethod("diesel-car-medium", 171),
                new TransportationMethod("petrol-car-medium", 192),
                new TransportationMethod("plugin-hybrid-car-medium", 110),
                new TransportationMethod("electric-car-medium", 58),
                new TransportationMethod("diesel-car-large", 209),
                new TransportationMethod("petrol-car-large", 282),
                new TransportationMethod("plugin-hybrid-car-large", 126),
                new TransportationMethod("electric-car-large", 73),
                new TransportationMethod("bus-default", 27),
                new TransportationMethod("train-default", 6)
        ));

        System.out.println("Transportation methods loaded successfully.");
    }
}
