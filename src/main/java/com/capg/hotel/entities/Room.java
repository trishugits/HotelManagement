package com.capg.hotel.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Room")
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_id")
	private Integer roomId;

	@Column(name = "room_number")
	@NotNull(message = "Room number cannot be null")
	@Min(value = 1, message = "Room number must be greater than 0")
	private Integer roomNumber;

	@Column(name = "is_available")
	@NotNull(message = "Availability status must be specified")
	private Boolean isAvailable;

	// Foreign key mapping (IMPORTANT)
	@ManyToOne
	@JoinColumn(name = "room_type_id")
	@NotNull(message = "Room type must be assigned")
	private RoomType roomType;

	@ManyToOne
	@JoinColumn(name = "hotel_id")
	@NotNull(message = "Hotel must be assigned")
	private Hotel hotel;

	// Constructors
	public Room() {
	}

	// Getters and Setters
	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public Room(Integer roomId, Integer roomNumber, Boolean isAvailable, RoomType roomType, Hotel hotel) {
		this.roomId = roomId;
		this.roomNumber = roomNumber;
		this.isAvailable = isAvailable;
		this.roomType = roomType;
		this.hotel = hotel;
	}
}