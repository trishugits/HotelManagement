package com.capg.hotel.repositories;

import com.capg.hotel.entities.Amenity;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
public class AmenityRepositoryTest {

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private TestEntityManager entityManager;

    // Helper method
    private Amenity save(String name, String desc) {
        Amenity a = new Amenity();
        a.setName(name);
        a.setDescription(desc);
        return amenityRepository.save(a);
    }

    // =======================================================
    // ✅ CORRECT SCENARIOS
    // =======================================================

    @Test
    public void testSaveAmenity_valid() {
        Amenity saved = save("Wi-Fi", "High-speed internet");
        assertThat(saved).isNotNull();
        assertThat(saved.getAmenityId()).isNotNull();
    }

    @Test
    public void testSaveAmenity_persistsFields() {
        Amenity saved = save("Gym", "Fitness center");
        entityManager.flush();
        entityManager.clear();

        Amenity found = amenityRepository.findById(saved.getAmenityId()).orElseThrow();
        assertThat(found.getName()).isEqualTo("Gym");
        assertThat(found.getDescription()).isEqualTo("Fitness center");
    }

    @Test
    public void testUpdateAmenity_valid() {
        Amenity saved = save("Old", "Old Desc");

        saved.setName("New");
        saved.setDescription("New Desc");
        amenityRepository.saveAndFlush(saved);

        entityManager.clear();

        Amenity updated = amenityRepository.findById(saved.getAmenityId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("New");
        assertThat(updated.getDescription()).isEqualTo("New Desc");
    }

    @Test
    public void testFindByName_valid() {
        save("Spa", "Relaxation");
        Optional<Amenity> found = amenityRepository.findByName("Spa");

        assertThat(found).isPresent();
        assertThat(found.get().getDescription()).isEqualTo("Relaxation");
    }

    // =======================================================
    // ❌ INCORRECT SCENARIOS (valid input but no match)
    // =======================================================

    @Test
    public void testFindByName_notFound() {
        Optional<Amenity> found = amenityRepository.findByName("NonExistent");
        assertThat(found).isEmpty();
    }

    @Test
    public void testFindByName_caseSensitive() {
        save("Tennis Court", "Outdoor");
        Optional<Amenity> found = amenityRepository.findByName("tennis court");

        assertThat(found).isEmpty();
    }

    @Test
    public void testUpdateDoesNotCreateNewRecord() {
        Amenity saved = save("Parking", "Secure");
        long countBefore = amenityRepository.count();

        saved.setName("Updated Parking");
        amenityRepository.saveAndFlush(saved);

        assertThat(amenityRepository.count()).isEqualTo(countBefore);
    }

    // =======================================================
    // 🚫 INVALID SCENARIOS (should throw validation errors)
    // =======================================================

    @Test
    public void testSaveAmenity_nullName_shouldFail() {
        Amenity a = new Amenity();
        a.setDescription("Desc");

        assertThatThrownBy(() -> {
            amenityRepository.saveAndFlush(a);
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void testSaveAmenity_emptyName_shouldFail() {
        Amenity a = new Amenity();
        a.setName(""); // invalid
        a.setDescription("Desc");

        assertThatThrownBy(() -> {
            amenityRepository.saveAndFlush(a);
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void testUpdateAmenity_nullName_shouldFail() {
        Amenity saved = save("Valid Name", "Desc");
        saved.setName(null);

        assertThatThrownBy(() -> {
            amenityRepository.saveAndFlush(saved);
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void testUpdateAmenity_emptyName_shouldFail() {
        Amenity saved = save("Valid Name", "Desc");
        saved.setName("");

        assertThatThrownBy(() -> {
            amenityRepository.saveAndFlush(saved);
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void testSaveAmenity_nullEntity_shouldFail() {
        assertThatThrownBy(() -> {
            amenityRepository.saveAndFlush(null);
        }).isInstanceOf(Exception.class);
    }
}