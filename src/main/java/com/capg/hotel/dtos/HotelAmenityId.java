package com.capg.hotel.dtos;

import java.io.Serializable;

import jakarta.persistence.*;

@Embeddable
public class HotelAmenityId implements Serializable {

    @Column(name = "hotel_id")
    private Integer hotelId;

    @Column(name = "amenity_id")
    private Integer amenityId;

	public Integer getHotelId() {
		return hotelId;
	}

	public void setHotelId(Integer hotelId) {
		this.hotelId = hotelId;
	}

	public Integer getAmenityId() {
		return amenityId;
	}

	public void setAmenityId(Integer amenityId) {
		this.amenityId = amenityId;
	}
    
}