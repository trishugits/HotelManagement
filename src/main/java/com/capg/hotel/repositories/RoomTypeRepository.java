package com.capg.hotel.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.capg.hotel.entities.RoomType;	

@RepositoryRestResource(
        path = "roomtypes",
        collectionResourceRel = "roomtypes"
)
public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {
	List<RoomType> findByTypeName(String typeName);

	List<RoomType> findByMaxOccupancyGreaterThan(Integer value);

	List<RoomType> findByPricePerNightLessThan(BigDecimal price);

	List<RoomType> findByTypeNameContaining(String keyword);
}