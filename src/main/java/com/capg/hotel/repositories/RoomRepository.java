package com.capg.hotel.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.capg.hotel.entities.Hotel;
import com.capg.hotel.entities.Room;
import com.capg.hotel.projections.HotelProjections;
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
	    
    List<Room> findByRoomType_RoomTypeId(Integer roomTypeId);

    List<Room> findByIsAvailableTrue();

    List<Room> findByIsAvailableFalse();
}
