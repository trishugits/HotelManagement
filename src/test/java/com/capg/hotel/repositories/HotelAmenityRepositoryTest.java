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
    private HotelAmenityRepository hotelAmenityRepository;

    // VALID SCENARIOS - findByAmenityAmenityId

    @Test
    void shouldFindHotelsByAmenityId() {
        var page = hotelAmenityRepository
                .findByAmenityAmenityId(1, PageRequest.of(0, 10));

        assertFalse(page.isEmpty());

        assertTrue(
                page.getContent().stream()
                        .allMatch(ha -> ha.getAmenity().getAmenityId().equals(1)));
    }

    @Test
    void shouldPaginateResultsByAmenityId() {
        var page1 = hotelAmenityRepository
                .findByAmenityAmenityId(1, PageRequest.of(0, 5));

        var page2 = hotelAmenityRepository
                .findByAmenityAmenityId(1, PageRequest.of(1, 5));

        if (!page1.isEmpty() && !page2.isEmpty()) {
            assertNotEquals(
                    page1.getContent().get(0).getId(),
                    page2.getContent().get(0).getId());
        }
    }

    // VALID SCENARIOS - findByHotelHotelId

    @Test
    void shouldFindAmenitiesByHotelId() {
        var page = hotelAmenityRepository
                .findByHotelHotelId(1, PageRequest.of(0, 10));

        assertFalse(page.isEmpty());

        assertTrue(
                page.getContent().stream()
                        .allMatch(ha -> ha.getHotel().getHotelId().equals(1)));
    }

    @Test
    void shouldPaginateResultsByHotelId() {
        var page1 = hotelAmenityRepository
                .findByHotelHotelId(1, PageRequest.of(0, 5));

        var page2 = hotelAmenityRepository
                .findByHotelHotelId(1, PageRequest.of(1, 5));

        if (!page1.isEmpty() && !page2.isEmpty()) {
            assertNotEquals(
                    page1.getContent().get(0).getId(),
                    page2.getContent().get(0).getId());
        }
    }

    // INVALID SCENARIOS - NON-EXISTING IDS

    @Test
    void shouldReturnEmptyWhenAmenityIdDoesNotExist() {
        var page = hotelAmenityRepository
                .findByAmenityAmenityId(-999, PageRequest.of(0, 10));

        assertTrue(page.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenHotelIdDoesNotExist() {
        var page = hotelAmenityRepository
                .findByHotelHotelId(-999, PageRequest.of(0, 10));

        assertTrue(page.isEmpty());
    }

    // INVALID SCENARIOS - NULL INPUT

    @Test
    void shouldReturnEmptyWhenAmenityIdIsNull() {
        var page = hotelAmenityRepository
                .findByAmenityAmenityId(null, PageRequest.of(0, 10));

        assertTrue(page.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenHotelIdIsNull() {
        var page = hotelAmenityRepository
                .findByHotelHotelId(null, PageRequest.of(0, 10));

        assertTrue(page.isEmpty());
    }

    // INVALID SCENARIOS - PAGINATION

    @Test
    void shouldThrowExceptionForNegativePageIndex() {
        assertThrows(IllegalArgumentException.class, () -> PageRequest.of(-1, 10));
    }

    @Test
    void shouldThrowExceptionForZeroPageSize() {
        assertThrows(IllegalArgumentException.class, () -> PageRequest.of(0, 0));
    }
}