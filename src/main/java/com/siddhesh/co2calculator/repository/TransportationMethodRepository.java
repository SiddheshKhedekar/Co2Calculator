package com.siddhesh.co2calculator.repository;

import com.siddhesh.co2calculator.model.TransportationMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TransportationMethodRepository extends JpaRepository<TransportationMethod, Long> {
    Optional<TransportationMethod> findByName(String name); // Method to find a transportation method by its name

}