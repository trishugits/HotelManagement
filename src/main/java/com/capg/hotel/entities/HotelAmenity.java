package com.capg.hotel.entities;

import com.capg.hotel.dtos.HotelAmenityId;

import jakarta.persistence.*;

@Entity
@Table(name = "hotelamenity")
public class HotelAmenity {

    @EmbeddedId
    private HotelAmenityId id;

    @ManyToOne
    @MapsId("hotelId")
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @ManyToOne
    @MapsId("amenityId")
    @JoinColumn(name = "amenity_id")
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