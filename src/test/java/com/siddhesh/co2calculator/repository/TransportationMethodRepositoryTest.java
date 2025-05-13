package com.siddhesh.co2calculator.repository;

import com.siddhesh.co2calculator.model.TransportationMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TransportationMethodRepositoryTest {

    @Autowired
    private TransportationMethodRepository repository;

    @Test
    @DisplayName("Should save and retrieve TransportationMethod by name")
    void testFindByName() {
        // Given
        TransportationMethod method = new TransportationMethod("electric-car-small", 50.0);
        repository.save(method);

        // When
        Optional<TransportationMethod> found = repository.findByName("electric-car-small");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("electric-car-small");
        assertThat(found.get().getCo2EmissionPerKm()).isEqualTo(50.0);
    }

    @Test
    @DisplayName("Should return empty when name not found")
    void testFindByName_NotFound() {
        Optional<TransportationMethod> result = repository.findByName("non-existing");

        assertThat(result).isEmpty();
    }
}
