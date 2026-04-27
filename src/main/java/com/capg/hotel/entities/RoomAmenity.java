package com.capg.hotel.entities;


import com.capg.hotel.dtos.RoomAmenityId;

import jakarta.persistence.*;

@Entity
@Table(name = "RoomAmenity")
public class RoomAmenity {

    @EmbeddedId
    private RoomAmenityId id;

    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @MapsId("amenityId")
    @JoinColumn(name = "amenity_id")
    private Amenity amenity;

	public RoomAmenityId getId() {
		return id;
	}

	public void setId(RoomAmenityId id) {
		this.id = id;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Amenity getAmenity() {
		return amenity;
	}

	public void setAmenity(Amenity amenity) {
		this.amenity = amenity;
	}

	public RoomAmenity() {
	}

	public RoomAmenity(RoomAmenityId id, Room room, Amenity amenity) {
		super();
		this.id = id;
		this.room = room;
		this.amenity = amenity;
	}
}