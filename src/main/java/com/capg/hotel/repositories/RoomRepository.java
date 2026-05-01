package com.capg.hotel.repositories;

import java.util.Optional;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import com.capg.hotel.entities.Room;
import com.capg.hotel.projections.RoomProjections;

@RepositoryRestResource(
        path = "room",
        collectionResourceRel = "room",
        excerptProjection = RoomProjections.class
)
public interface RoomRepository extends JpaRepository<Room, Integer> {
	
	@RestResource(exported = false)
	void deleteById(int id);

	@RestResource(exported = false)
    void delete(Room room);
	
	@EntityGraph(attributePaths = {"hotel", "roomType"})
	Page<Room> findAll(Pageable pageable);

	@EntityGraph(attributePaths = {"hotel", "roomType"})
	Optional<Room> findById(Integer id);
	    
	@EntityGraph(attributePaths = {"hotel", "roomType"})
	Page<Room> findByRoomType_RoomTypeId(Integer roomTypeId,Pageable pageable);

	@EntityGraph(attributePaths = {"hotel", "roomType"})
	Page<Room> findByIsAvailableTrue(Pageable pageable);

	@EntityGraph(attributePaths = {"hotel", "roomType"})
    Page<Room> findByIsAvailableFalse(Pageable pageable);
}
