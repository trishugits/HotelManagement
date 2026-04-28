package com.capg.hotel.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capg.hotel.entities.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {
	
    List<Room> findByRoomType_RoomTypeId(Integer roomTypeId);

    List<Room> findByIsAvailableTrue();

    List<Room> findByIsAvailableFalse();
}
