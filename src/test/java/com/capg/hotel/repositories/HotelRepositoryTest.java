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

    // Helper Method
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
                "Grand Plaza Hotel",
                "Downtown City Center",
                "Luxury hotel with stunning views of the city."
        );

        assertNotNull(saved);
        assertNotNull(saved.getHotelId());
        assertEquals("Grand Plaza Hotel", saved.getName());
        assertEquals("Downtown City Center", saved.getLocation());
    }

    @Test
    void testSaveHotel_persistsFields() {
        Hotel saved = save(
                "Oceanfront Resort & Spa",
                "Beachfront Paradise",
                "Relaxing resort with spa facilities, steps away from the ocean."
        );

        Optional<Hotel> found =
                hotelRepository.findById(saved.getHotelId());

        assertTrue(found.isPresent());
        assertEquals("Oceanfront Resort & Spa", found.get().getName());
        assertEquals("Beachfront Paradise", found.get().getLocation());
    }

    @Test
    void testFindById_valid() {
        // Hotel with ID 1 exists in real DB as "Grand Plaza Hotel"
        Optional<Hotel> result =
                hotelRepository.findById(1);

        assertTrue(result.isPresent());
        assertEquals("Grand Plaza Hotel", result.get().getName());
        assertEquals("Downtown City Center", result.get().getLocation());
    }

    @Test
    void testUpdateHotel_valid() {
        Hotel saved = save(
                "Urban Skyline Suites",
                "Metropolitan Area",
                "Chic suites with panoramic views of the city skyline."
        );

        saved.setName("Updated Skyline Suites");
        saved.setLocation("Luxury Skyline");
        saved.setDescription("Updated premium skyline suites.");

        hotelRepository.saveAndFlush(saved);

        Hotel updated = hotelRepository
                .findById(saved.getHotelId())
                .orElseThrow();

        assertEquals("Updated Skyline Suites", updated.getName());
        assertEquals("Luxury Skyline", updated.getLocation());
    }

    @Test
    void testPagination_firstPage() {
        // Real DB has 49 hotels, page size 2 → first page always has 2
        Page<Hotel> page =
                hotelRepository.findAll(PageRequest.of(0, 2));

        assertEquals(2, page.getContent().size());
        assertTrue(page.getTotalElements() > 0);
        assertTrue(page.getTotalPages() >= 1);
    }

    @Test
    void testPagination_secondPage() {
        // Real DB has 49 hotels, second page with size 2 always has 2
        Page<Hotel> page =
                hotelRepository.findAll(PageRequest.of(1, 2));

        assertEquals(2, page.getContent().size());
    }

    @Test
    void testCountHotels_valid() {
        // Save 2 new hotels and verify count increases by exactly 2
        long countBefore = hotelRepository.count();

        save("Test Count Hotel One",
                "Test Location One",
                "Valid description for count test one.");

        save("Test Count Hotel Two",
                "Test Location Two",
                "Valid description for count test two.");

        long countAfter = hotelRepository.count();

        assertEquals(countBefore + 2, countAfter);
    }

    @Test
    void testFindByName_valid() {
        // "Seaside Retreat Lodge" exists in real DB (hotelId 4, 14, 24)
        Page<Hotel> result =
                hotelRepository.findByName(
                        "Seaside Retreat Lodge",
                        PageRequest.of(0, 10)
                );

        assertThat(result.getContent()).isNotEmpty();
        result.getContent().forEach(h ->
                assertEquals("Seaside Retreat Lodge", h.getName())
        );
    }

    @Test
    void testFindByLocation_singleMatch() {
        // "Mumbai" has only Taj Hotel (hotelId 50) — but that's removed
        // "Pine Forest" has only Whispering Pines Inn (hotelId 45)
        Page<Hotel> result =
                hotelRepository.findByLocation(
                        "Pine Forest",
                        PageRequest.of(0, 10)
                );

        assertThat(result.getContent()).isNotEmpty();
        result.getContent().forEach(h ->
                assertEquals("Pine Forest", h.getLocation())
        );
    }

    @Test
    void testFindByLocation_multipleMatches() {
        // "Downtown City Center" has hotelId 1, 11, 21 in real DB
        Page<Hotel> result =
                hotelRepository.findByLocation(
                        "Downtown City Center",
                        PageRequest.of(0, 10)
                );

        assertThat(result.getContent()).hasSizeGreaterThanOrEqualTo(3);
        result.getContent().forEach(h ->
                assertEquals("Downtown City Center", h.getLocation())
        );
    }

    // =======================================================
    // ❌ INCORRECT SCENARIOS
    // =======================================================

    @Test
    void testFindById_notFound() {
        Optional<Hotel> result =
                hotelRepository.findById(99999);

        assertTrue(result.isEmpty());
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
    void testFindByName_caseSensitive() {
        // MySQL is case-insensitive by default so lowercase also matches
        Page<Hotel> result =
                hotelRepository.findByName(
                        "whispering pines inn",
                        PageRequest.of(0, 10)
                );

        // On case-insensitive MySQL this finds "Whispering Pines Inn"
        assertThat(result.getContent()).isNotEmpty();
        result.getContent().forEach(h ->
                assertEquals("Whispering Pines Inn", h.getName())
        );
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

    @Test
    void testUpdateDoesNotCreateNewRecord() {
        Hotel saved = save(
                "Harbor View Hotel",
                "Harborfront District",
                "Enjoy scenic harbor views in this waterfront hotel."
        );

        long countBefore = hotelRepository.count();

        saved.setName("Updated Harbor View Hotel");
        hotelRepository.saveAndFlush(saved);

        long countAfter = hotelRepository.count();

        assertEquals(countBefore, countAfter);
    }

    // =======================================================
    // 🚫 INVALID SCENARIOS
    // =======================================================

    @Test
    void testSaveHotel_blankName_shouldFail() {
        Hotel hotel = new Hotel(
                null,
                "",
                "Delhi",
                "Valid description here"
        );

        assertThrows(
                ConstraintViolationException.class,
                () -> hotelRepository.saveAndFlush(hotel)
        );
    }

    @Test
    void testSaveHotel_nullName_shouldFail() {
        Hotel hotel = new Hotel(
                null,
                null,
                "Delhi",
                "Valid description here"
        );

        assertThrows(
                ConstraintViolationException.class,
                () -> hotelRepository.saveAndFlush(hotel)
        );
    }

    @Test
    void testSaveHotel_shortDescription_shouldFail() {
        Hotel hotel = new Hotel(
                null,
                "Good Hotel",
                "Delhi",
                "abc"
        );

        assertThrows(
                ConstraintViolationException.class,
                () -> hotelRepository.saveAndFlush(hotel)
        );
    }

    @Test
    void testUpdateHotel_blankName_shouldFail() {
        Hotel saved = save(
                "City Lights Hotel",
                "Downtown Core",
                "Captivating city lights view from every room."
        );

        saved.setName("");

        assertThrows(
                ConstraintViolationException.class,
                () -> hotelRepository.saveAndFlush(saved)
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