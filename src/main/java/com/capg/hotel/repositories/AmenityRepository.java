package com.capg.hotel.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.capg.hotel.entities.Amenity;

@RepositoryRestResource(
        path = "amenities",
        collectionResourceRel = "amenities"
)
public interface AmenityRepository extends JpaRepository<Amenity,Integer> {
	@RestResource(exported=false)
	void deleteById(int id);
	@RestResource(exported=false)
	void delete(Amenity amenity);
	Optional<Amenity> findByName(int id);
}