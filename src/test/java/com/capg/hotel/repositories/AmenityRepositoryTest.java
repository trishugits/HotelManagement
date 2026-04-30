package com.capg.hotel.repositories;

import com.capg.hotel.entities.Amenity;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AmenityRepositoryTest {

    @Autowired
    private AmenityRepository amenityRepository;
    
    //read tests
    @Test
    void shouldFindAmenityById() {
        Amenity a = new Amenity(null, "Temp", "Valid description");
        a = amenityRepository.saveAndFlush(a);

        Optional<Amenity> found = amenityRepository.findById(a.getAmenityId());

        assertTrue(found.isPresent());
    }

    @Test
    void shouldReturnEmptyWhenIdNotFound() {
        Optional<Amenity> found = amenityRepository.findById(-999);

        assertTrue(found.isEmpty());
    }

    @Test
    void shouldFindByNameContainingIgnoreCase() {
        var page = amenityRepository
                .findByNameContainingIgnoreCase("pool", PageRequest.of(0, 10));

        assertFalse(page.isEmpty());
        assertTrue(
            page.getContent().stream()
                .allMatch(a -> a.getName().toLowerCase().contains("pool"))
        );
    }

    @Test
    void shouldBeCaseInsensitiveSearch() {
        var page = amenityRepository
                .findByNameContainingIgnoreCase("POOL", PageRequest.of(0, 10));

        assertFalse(page.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenNoMatchFound() {
        var page = amenityRepository
                .findByNameContainingIgnoreCase("xyz_not_exist", PageRequest.of(0, 10));

        assertTrue(page.isEmpty());
    }

    //pagination tests
    @Test
    void shouldReturnLimitedPageSize() {
        var page = amenityRepository.findAll(PageRequest.of(0, 5));

        assertEquals(5, page.getContent().size());
    }

    @Test
    void shouldReturnDifferentResultsForDifferentPages() {
        var page1 = amenityRepository.findAll(PageRequest.of(0, 5));
        var page2 = amenityRepository.findAll(PageRequest.of(1, 5));

        assertNotEquals(
            page1.getContent().get(0).getAmenityId(),
            page2.getContent().get(0).getAmenityId()
        );
    }

    //update and add tests
    @Test
    void shouldSaveAmenity() {
        Amenity a = new Amenity(null, "Test Amenity", "Valid description here");

        Amenity saved = amenityRepository.saveAndFlush(a);

        assertNotNull(saved.getAmenityId());

        Optional<Amenity> found = amenityRepository.findById(saved.getAmenityId());
        assertTrue(found.isPresent());
    }

    @Test
    void shouldUpdateAmenity() {
        Amenity a = new Amenity(null, "Temp Name", "Valid description");
        a = amenityRepository.saveAndFlush(a);

        Integer id = a.getAmenityId();

        a.setName("Updated Name");
        a.setDescription("Updated description");

        amenityRepository.saveAndFlush(a);

        Amenity updated = amenityRepository.findById(id).orElseThrow();

        assertEquals(id, updated.getAmenityId());
        assertEquals("Updated Name", updated.getName());
    }

    @Test
    void shouldNotCreateNewRecordOnUpdate() {
        Amenity a = new Amenity(null, "Temp", "Valid description");
        a = amenityRepository.saveAndFlush(a);

        Integer idBefore = a.getAmenityId();

        a.setName("Updated Temp");
        Amenity updated = amenityRepository.saveAndFlush(a);

        Integer idAfter = updated.getAmenityId();

        assertEquals(idBefore, idAfter);
    }

    //invalid cases
    @Test
    void shouldFailWhenNameInvalid() {
        Amenity a = new Amenity(null, "", "Valid description");

        assertThrows(ConstraintViolationException.class,
                () -> amenityRepository.saveAndFlush(a));
    }

    @Test
    void shouldFailWhenDescriptionTooShort() {
        Amenity a = new Amenity(null, "Valid Name", "abc");

        assertThrows(ConstraintViolationException.class,
                () -> amenityRepository.saveAndFlush(a));
    }

    @Test
    void shouldFailWhenEntityIsNull() {
        assertThrows(Exception.class,
                () -> amenityRepository.saveAndFlush(null));
    }

}