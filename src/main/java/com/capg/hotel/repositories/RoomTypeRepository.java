package com.capg.hotel.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.capg.hotel.entities.Hotel;
import com.capg.hotel.entities.RoomType;
import com.capg.hotel.projections.RoomProjections;
import com.capg.hotel.projections.RoomTypeProjections;	

@RepositoryRestResource(
        path = "roomtypes",
        collectionResourceRel = "roomtypes",
        excerptProjection = RoomTypeProjections.class
)
public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {
	
	 @RestResource(exported = false)
	    void deleteById(int id);

	@RestResource(exported = false)
	void delete(RoomType roomtype);
	
	List<RoomType> findByTypeName(String typeName);

	List<RoomType> findByMaxOccupancyGreaterThan(Integer value);

	List<RoomType> findByPricePerNightLessThan(BigDecimal price);

	List<RoomType> findByTypeNameContaining(String keyword);
}