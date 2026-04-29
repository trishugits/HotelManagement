package com.capg.hotel.repositories;

import com.capg.hotel.entities.Amenity;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AmenityRepositoryTest {

    @Autowired
    private AmenityRepository amenityRepository;

    // =======================================================
    // Helper
    // =======================================================

    private Amenity save(String name, String desc) {
        Amenity a = new Amenity(null, name, desc);
        return amenityRepository.saveAndFlush(a);
    }

    // =======================================================
    // CORRECT SCENARIOS
    // =======================================================

    @Test
    void testSaveAmenity_valid() {
        Amenity saved = save("Wi-Fi", "High-speed internet");

        assertNotNull(saved);
        assertNotNull(saved.getAmenityId());
        assertEquals("Wi-Fi", saved.getName());
    }

    @Test
    void testSaveAmenity_persistsFields() {
        Amenity saved = save("Gym", "Fitness center");

        Optional<Amenity> found =
                amenityRepository.findById(saved.getAmenityId());

        assertTrue(found.isPresent());
        assertEquals("Gym", found.get().getName());
        assertEquals("Fitness center", found.get().getDescription());
    }

    @Test
    void testFindByNameContainingIgnoreCase_singleMatch() {
        save("Swimming Pool", "Outdoor pool");

        var page = amenityRepository
                .findByNameContainingIgnoreCase("pool", PageRequest.of(0, 5));

        assertEquals(1, page.getContent().size());
        assertEquals("Swimming Pool", page.getContent().get(0).getName());
    }

    @Test
    void testUpdateAmenity_valid() {
        Amenity saved = save("Old Name", "Old Description");

        saved.setName("New Name");
        saved.setDescription("New Description");

        amenityRepository.saveAndFlush(saved);

        Amenity updated =
                amenityRepository.findById(saved.getAmenityId()).orElseThrow();

        assertEquals("New Name", updated.getName());
        assertEquals("New Description", updated.getDescription());
    }

    @Test
    void testCountAmenities_valid() {
        save("AA", "Valid Desc AAAAA");
        save("BB", "Valid Desc BBBBB");

        long count = amenityRepository.count();

        assertEquals(2, count);
    }

    // =======================================================
    // INCORRECT SCENARIOS
    // =======================================================

//    @Test
//    void testFindByName_notFound() {
//        Optional<Amenity> found =
//                amenityRepository.findByName("Unknown");
//
//        assertTrue(found.isEmpty());
//    }

//    @Test
//    void testFindByName_caseSensitive() {
//        save("Tennis Court", "Outdoor court facility");
//
//        Optional<Amenity> found =
//                amenityRepository.findByName("tennis court");
//
//        assertTrue(found.isEmpty());
//    }

    @Test
    void testUpdateDoesNotCreateNewRecord() {
        Amenity saved = save("Parking", "Secure parking");

        long countBefore = amenityRepository.count();

        saved.setName("Updated Parking");

        amenityRepository.saveAndFlush(saved);

        long countAfter = amenityRepository.count();

        assertEquals(countBefore, countAfter);
    }

    // =======================================================
    // INVALID SCENARIOS
    // =======================================================

    @Test
    void testSaveAmenity_blankName_shouldFail() {
        Amenity a = new Amenity(
                null,
                "",
                "Valid description here"
        );

        assertThrows(
                ConstraintViolationException.class,
                () -> amenityRepository.saveAndFlush(a)
        );
    }

    @Test
    void testSaveAmenity_nullName_shouldFail() {
        Amenity a = new Amenity(
                null,
                null,
                "Valid description here"
        );

        assertThrows(
                ConstraintViolationException.class,
                () -> amenityRepository.saveAndFlush(a)
        );
    }

    @Test
    void testSaveAmenity_shortDescription_shouldFail() {
        Amenity a = new Amenity(
                null,
                "Valid Name",
                "abc"
        );

        assertThrows(
                ConstraintViolationException.class,
                () -> amenityRepository.saveAndFlush(a)
        );
    }

    @Test
    void testUpdateAmenity_blankName_shouldFail() {
        Amenity saved = save("Valid Name", "Valid Description");

        saved.setName("");

        assertThrows(
                ConstraintViolationException.class,
                () -> amenityRepository.saveAndFlush(saved)
        );
    }

    @Test
    void testUpdateAmenity_nullName_shouldFail() {
        Amenity saved = save("Valid Name", "Valid Description");

        saved.setName(null);

        assertThrows(
                ConstraintViolationException.class,
                () -> amenityRepository.saveAndFlush(saved)
        );
    }

    @Test
    void testSaveAmenity_nullEntity_shouldFail() {
        assertThrows(
                Exception.class,
                () -> amenityRepository.saveAndFlush(null)
        );
    }
    
 // =======================================================
 // PAGINATION SCENARIOS
 // =======================================================

    @Test
	void testPagination_firstPage() {
	    save("AA", "Valid Desc AAAAA");
	    save("BB", "Valid Desc BBBBB");
	    save("CC", "Valid Desc CCCCC");
	
	    var page = amenityRepository.findAll(PageRequest.of(0, 2));
	
	    assertEquals(2, page.getContent().size());
	    assertEquals(3, page.getTotalElements());
	    assertEquals(2, page.getTotalPages());
	 }

	 @Test
	 void testPagination_secondPage() {
	     save("AA", "Valid Desc AAAAA");
	     save("BB", "Valid Desc BBBBB");
	     save("CC", "Valid Desc CCCCC");
	
	     var page = amenityRepository.findAll(PageRequest.of(1, 2));
	
	     assertEquals(1, page.getContent().size());
	 }
	
	 @Test
	 void testPagination_emptyPage() {
	     save("AA", "Valid Desc AAAAA");
	
	     var page = amenityRepository.findAll(PageRequest.of(5, 2));
	
	     assertTrue(page.getContent().isEmpty());
	 }

}