package com.capg.hotel.dtos;

import java.io.Serializable;

import jakarta.persistence.*;

@Embeddable
public class RoomAmenityId implements Serializable {

    @Column(name = "room_id")
    private Integer roomId;

    @Column(name = "amenity_id")
    private Integer amenityId;

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getAmenityId() {
		return amenityId;
	}

	public void setAmenityId(Integer amenityId) {
		this.amenityId = amenityId;
	}
    
}