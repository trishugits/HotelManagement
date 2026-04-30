package com.capg.hotel.repositories;

import com.capg.hotel.entities.Hotel;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HotelRepositoryTest {

    @Autowired
    private HotelRepository hotelRepository;

    // Helper Method (safe: creates NEW records, does not affect existing IDs)
    private Hotel save(String name, String location, String description) {
        Hotel hotel = new Hotel(null, name, location, description);
        return hotelRepository.saveAndFlush(hotel);
    }

    // =======================================================
    // ✅ CORRECT SCENARIOS 
    // =======================================================

    @Test
    void testSaveHotel_valid() {
        Hotel saved = save(
                "Grand Plaza Hotel TEST",
                "Downtown City Center TEST",
                "Luxury hotel test entry."
        );

        assertNotNull(saved.getHotelId());
        assertEquals("Grand Plaza Hotel TEST", saved.getName());
    }

    @Test
    void testSaveHotel_persistsFields() {
        Hotel saved = save(
                "Oceanfront Resort TEST",
                "Beachfront Paradise TEST",
                "Test description"
        );

        Optional<Hotel> found =
                hotelRepository.findById(saved.getHotelId());

        assertTrue(found.isPresent());
    }

    @Test
    void testFindById_valid() {
        Optional<Hotel> result = hotelRepository.findById(1);

        assertTrue(result.isPresent());
        assertEquals("Grand Plaza Hotel", result.get().getName());
    }

    @Test
    void testUpdateHotel_valid() {
        Hotel saved = save(
                "Urban Skyline Suites TEST",
                "Metropolitan Area TEST",
                "Test desc"
        );

        saved.setName("Updated Skyline Suites TEST");
        hotelRepository.saveAndFlush(saved);

        Hotel updated = hotelRepository
                .findById(saved.getHotelId())
                .orElseThrow();

        assertEquals("Updated Skyline Suites TEST", updated.getName());
    }

    // =======================================================
    // ✅ PAGINATION 
    // =======================================================

    @Test
    void testPagination_firstPage() {
        Page<Hotel> page =
                hotelRepository.findAll(PageRequest.of(0, 2));

        assertEquals(2, page.getContent().size());
        assertTrue(page.getTotalElements() >= 1); // safer
    }

    @Test
    void testPagination_secondPage() {
        Page<Hotel> page =
                hotelRepository.findAll(PageRequest.of(1, 2));

        assertEquals(2, page.getContent().size());
    }

    // =======================================================
    // ✅ FIND BY NAME 
    // =======================================================

    @Test
    void testFindByName_valid() {
        Page<Hotel> result =
                hotelRepository.findByName(
                        "Seaside Retreat Lodge",
                        PageRequest.of(0, 10)
                );

        assertThat(result.getContent()).isNotEmpty();
    }

    // =======================================================
    // ✅ FIND BY LOCATION 
    // =======================================================

    @Test
    void testFindByLocation_multipleMatches() {
        Page<Hotel> result =
                hotelRepository.findByLocation(
                        "Downtown City Center",
                        PageRequest.of(0, 10)
                );

        assertThat(result.getContent()).isNotEmpty();
    }

    // =======================================================
    // ❌ NEGATIVE CASES
    // =======================================================

    @Test
    void testFindById_notFound() {
        assertTrue(hotelRepository.findById(99999).isEmpty());
    }

    @Test
    void testFindByName_notFound() {
        Page<Hotel> result =
                hotelRepository.findByName(
                        "Unknown Hotel XYZ",
                        PageRequest.of(0, 10)
                );

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByLocation_notFound() {
        Page<Hotel> result =
                hotelRepository.findByLocation(
                        "Unknown City XYZ",
                        PageRequest.of(0, 10)
                );

        assertTrue(result.isEmpty());
    }

    // =======================================================
    // 🚫 VALIDATION TESTS
    // =======================================================

    @Test
    void testSaveHotel_blankName_shouldFail() {
        Hotel hotel = new Hotel(null, "", "Delhi", "Valid description");

        assertThrows(
                ConstraintViolationException.class,
                () -> hotelRepository.saveAndFlush(hotel)
        );
    }

    @Test
    void testSaveHotel_nullName_shouldFail() {
        Hotel hotel = new Hotel(null, null, "Delhi", "Valid description");

        assertThrows(
                ConstraintViolationException.class,
                () -> hotelRepository.saveAndFlush(hotel)
        );
    }

    @Test
    void testSaveHotel_shortDescription_shouldFail() {
        Hotel hotel = new Hotel(null, "Hotel", "Delhi", "abc");

        assertThrows(
                ConstraintViolationException.class,
                () -> hotelRepository.saveAndFlush(hotel)
        );
    }

    @Test
    void testSaveHotel_nullEntity_shouldFail() {
        assertThrows(
                Exception.class,
                () -> hotelRepository.saveAndFlush(null)
        );
    }
}