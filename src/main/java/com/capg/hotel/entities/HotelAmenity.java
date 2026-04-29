package com.capg.hotel.entities;

import com.capg.hotel.dtos.HotelAmenityId;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "HotelAmenity")
public class HotelAmenity {

    @EmbeddedId
    @NotNull(message = "Composite ID cannot be null")
    @Valid
    private HotelAmenityId id;

    @ManyToOne
    @MapsId("hotelId")
    @JoinColumn(name = "hotel_id")
    @NotNull(message = "Amenity cannot be null")
    private Hotel hotel;

    @ManyToOne
    @MapsId("amenityId")
    @JoinColumn(name = "amenity_id")
    @NotNull(message = "Amenity cannot be null")
    private Amenity amenity;

	public HotelAmenityId getId() {
		return id;
	}

	public void setId(HotelAmenityId id) {
		this.id = id;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public Amenity getAmenity() {
		return amenity;
	}

	public void setAmenity(Amenity amenity) {
		this.amenity = amenity;
	}

	public HotelAmenity(HotelAmenityId id, Hotel hotel, Amenity amenity) {
		super();
		this.id = id;
		this.hotel = hotel;
		this.amenity = amenity;
	}

	public HotelAmenity() {
	}
    
}