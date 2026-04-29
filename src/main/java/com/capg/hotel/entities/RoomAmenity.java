package com.capg.hotel.entities;


import com.capg.hotel.dtos.RoomAmenityId;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "RoomAmenity")
public class RoomAmenity {

    @EmbeddedId
    private RoomAmenityId id;
    @NotNull(message = "Composite ID cannot be null")
    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name = "room_id")
    @NotNull(message = "Room cannot be null")
    private Room room;

    @ManyToOne
    @MapsId("amenityId")
    @JoinColumn(name = "amenity_id")
    @NotNull(message = "Amenity cannot be null")
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