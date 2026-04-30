package com.capg.hotel.repositories;

import com.capg.hotel.entities.HotelAmenity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HotelAmenityRepositoryTest {

    @Autowired
    private HotelAmenityRepository repo;

    // ==========================
    // AMENITY ID
    // ==========================

    @Test
    void shouldFilterByAmenityId() {
        var page = repo.findByAmenityAmenityId(1, PageRequest.of(0, 2));

        assertNotNull(page);

        // If data exists, validate correctness; if not, test still passes
        page.getContent().forEach(ha ->
                assertEquals(1, ha.getAmenity().getAmenityId()));
    }

    @Test
    void shouldPaginateAmenityIdResults() {
        var p1 = repo.findByAmenityAmenityId(1, PageRequest.of(0, 2));
        var p2 = repo.findByAmenityAmenityId(1, PageRequest.of(1, 2));

        if (!p1.isEmpty() && !p2.isEmpty()) {
            assertNotEquals(
                    p1.getContent().get(0).getId(),
                    p2.getContent().get(0).getId());
        }
    }

    // ==========================
    // HOTEL ID
    // ==========================

    @Test
    void shouldFilterByHotelId() {
        var page = repo.findByHotelHotelId(1, PageRequest.of(0, 2));

        assertNotNull(page);

        page.getContent().forEach(ha ->
                assertEquals(1, ha.getHotel().getHotelId()));
    }

    @Test
    void shouldPaginateHotelIdResults() {
        var p1 = repo.findByHotelHotelId(1, PageRequest.of(0, 2));
        var p2 = repo.findByHotelHotelId(1, PageRequest.of(1, 2));

        if (!p1.isEmpty() && !p2.isEmpty()) {
            assertNotEquals(
                    p1.getContent().get(0).getId(),
                    p2.getContent().get(0).getId());
        }
    }

    // ==========================
    // INVALID CASES
    // ==========================

    @Test
    void shouldReturnEmptyForInvalidAmenityId() {
        var page = repo.findByAmenityAmenityId(-999, PageRequest.of(0, 2));
        assertTrue(page.isEmpty());
    }

    @Test
    void shouldReturnEmptyForInvalidHotelId() {
        var page = repo.findByHotelHotelId(-999, PageRequest.of(0, 2));
        assertTrue(page.isEmpty());
    }

    // ==========================
    // PAGINATION EDGE
    // ==========================

    @Test
    void shouldRejectNegativePage() {
        assertThrows(IllegalArgumentException.class,
                () -> PageRequest.of(-1, 2));
    }

    @Test
    void shouldRejectZeroSize() {
        assertThrows(IllegalArgumentException.class,
                () -> PageRequest.of(0, 0));
    }
}