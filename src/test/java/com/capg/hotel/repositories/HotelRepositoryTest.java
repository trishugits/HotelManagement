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

    private Hotel save(String name, String location, String description) {
        Hotel hotel = new Hotel(null, name, location, description);
        return hotelRepository.saveAndFlush(hotel);
    }

    // =========================
    // ✅ CORRECT SCENARIOS
    // =========================

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
                "Relaxing resort with spa facilities."
        );

        Optional<Hotel> found =
                hotelRepository.findById(saved.getHotelId());

        assertTrue(found.isPresent());
        assertEquals("Oceanfront Resort & Spa", found.get().getName());
        assertEquals("Beachfront Paradise", found.get().getLocation());
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
                "Urban Skyline Suites",
                "Metropolitan Area",
                "Chic suites with skyline views."
        );

        Integer id = saved.getHotelId();

        saved.setName("Updated Skyline Suites");
        saved.setLocation("Luxury Skyline");

        hotelRepository.saveAndFlush(saved);

        Hotel updated = hotelRepository.findById(id).orElseThrow();

        assertEquals(id, updated.getHotelId());
        assertEquals("Updated Skyline Suites", updated.getName());
        assertEquals("Luxury Skyline", updated.getLocation());
    }

    @Test
    void testPagination_firstPage() {
        Page<Hotel> page =
                hotelRepository.findAll(PageRequest.of(0, 2));

        assertEquals(2, page.getContent().size());
        assertTrue(page.getTotalPages() >= 1);
    }

    @Test
    void testPagination_secondPage() {
        Page<Hotel> page =
                hotelRepository.findAll(PageRequest.of(1, 2));

        assertEquals(2, page.getContent().size());
    }

    @Test
    void testSaveHotel_createsRecords() {
        Hotel h1 = save(
                "Test Hotel One",
                "Location One",
                "Valid description one"
        );

        Hotel h2 = save(
                "Test Hotel Two",
                "Location Two",
                "Valid description two"
        );

        Optional<Hotel> f1 = hotelRepository.findById(h1.getHotelId());
        Optional<Hotel> f2 = hotelRepository.findById(h2.getHotelId());

        assertTrue(f1.isPresent());
        assertTrue(f2.isPresent());
    }

    @Test
    void testFindByName_valid() {
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
        Page<Hotel> result =
                hotelRepository.findByLocation(
                        "Downtown City Center",
                        PageRequest.of(0, 10)
                );

        assertThat(result.getContent()).hasSizeGreaterThanOrEqualTo(1);
    }

    // =========================
    // ❌ INCORRECT SCENARIOS
    // =========================

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
        Page<Hotel> result =
                hotelRepository.findByName(
                        "whispering pines inn",
                        PageRequest.of(0, 10)
                );

        assertThat(result.getContent()).isNotEmpty();
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
                "Enjoy scenic harbor views."
        );

        Integer id = saved.getHotelId();

        saved.setName("Updated Harbor View Hotel");
        hotelRepository.saveAndFlush(saved);

        Optional<Hotel> updated = hotelRepository.findById(id);

        assertTrue(updated.isPresent());
        assertEquals(id, updated.get().getHotelId());
        assertEquals("Updated Harbor View Hotel", updated.get().getName());
    }

    // =========================
    // 🚫 INVALID SCENARIOS
    // =========================

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
        Hotel hotel = new Hotel(null, "Good Hotel", "Delhi", "abc");

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
                "Nice view hotel"
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