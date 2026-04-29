package com.capg.hotel.repositories;

import com.capg.hotel.entities.Hotel;
import com.capg.hotel.repositories.HotelRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
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
        Hotel saved = save(
                "Downtown Oasis Hotel",
                "City Center",
                "Modern hotel with a central location, perfect for business travelers."
        );

        Optional<Hotel> result =
                hotelRepository.findById(saved.getHotelId());

        assertTrue(result.isPresent());
        assertEquals("Downtown Oasis Hotel", result.get().getName());
    }

    @Test
    void testFindByName_valid() {
        save(
                "Seaside Retreat Lodge",
                "Coastal Area",
                "Scenic lodge offering a peaceful escape by the sea."
        );

        Optional<Hotel> result =
                hotelRepository.findByName("Seaside Retreat Lodge");

        assertTrue(result.isPresent());
        assertEquals("Coastal Area", result.get().getLocation());
    }

    @Test
    void testFindByLocation_singleMatch() {
        save(
                "Mountain View Hotel",
                "Mountainous Region",
                "Elegant hotel surrounded by breathtaking mountain views."
        );

        List<Hotel> result =
                hotelRepository.findByLocation("Mountainous Region");

        assertThat(result).hasSize(1);
        assertEquals("Mountainous Region", result.get(0).getLocation());
    }

    @Test
    void testFindByLocation_multipleMatches() {
        save("Grand Plaza Hotel",
                "Downtown City Center",
                "Luxury hotel with stunning views of the city.");

        save("Business Traveler Inn",
                "Downtown City Center",
                "Tailored for the needs of business travelers.");

        List<Hotel> result =
                hotelRepository.findByLocation("Downtown City Center");

        assertThat(result).hasSize(2);
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
        save("Hotel A", "City A", "Description AAAAA");
        save("Hotel B", "City B", "Description BBBBB");
        save("Hotel C", "City C", "Description CCCCC");

        Page<Hotel> page =
                hotelRepository.findAll(PageRequest.of(0, 2));

        assertEquals(2, page.getContent().size());
        assertEquals(3, page.getTotalElements());
        assertEquals(2, page.getTotalPages());
    }

    @Test
    void testPagination_secondPage() {
        save("Hotel A", "City A", "Description AAAAA");
        save("Hotel B", "City B", "Description BBBBB");
        save("Hotel C", "City C", "Description CCCCC");

        Page<Hotel> page =
                hotelRepository.findAll(PageRequest.of(1, 2));

        assertEquals(1, page.getContent().size());
    }

    @Test
    void testCountHotels_valid() {
        save("Snowy Peaks Chalet",
                "Mountain Resort",
                "Cozy chalet with a fireplace, perfect for winter getaways.");

        save("Riverside Boutique Hotel",
                "Riverside District",
                "Boutique hotel offering a blend of comfort and style by the river.");

        long count = hotelRepository.count();

        assertEquals(2, count);
    }

    @Test
    void testDeleteHotel_valid() {
        Hotel saved = save(
                "Palm Grove Resort",
                "Tropical Haven",
                "Relax under the palm trees in this tropical haven."
        );

        Integer id = saved.getHotelId();

        hotelRepository.delete(saved);

        Optional<Hotel> result =
                hotelRepository.findById(id);

        assertTrue(result.isEmpty());
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
        Optional<Hotel> result =
                hotelRepository.findByName("Unknown Hotel");

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByName_caseSensitive() {
        save(
                "Whispering Pines Inn",
                "Pine Forest",
                "Cozy inn surrounded by the whispering sounds of pine trees."
        );

        Optional<Hotel> result =
                hotelRepository.findByName("whispering pines inn");

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByLocation_notFound() {
        List<Hotel> result =
                hotelRepository.findByLocation("Unknown City");

        assertThat(result).isEmpty();
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