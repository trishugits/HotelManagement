package com.capg.hotel.repositories;

import org.springframework.data.domain.*;
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
	    
    Page<Room> findByRoomType_RoomTypeId(Integer roomTypeId,Pageable pageable);

    Page<Room> findByIsAvailableTrue(Pageable pageable);

    Page<Room> findByIsAvailableFalse(Pageable pageable);
}
