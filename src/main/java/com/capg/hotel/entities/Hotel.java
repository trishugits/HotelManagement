package com.capg.hotel.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Hotel")
public class Hotel {

    @Id
    @Column(name = "hotel_id")
    private Integer hotelId;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "description")
    private String description;

	public Integer getHotelId() {
		return hotelId;
	}

	public void setHotelId(Integer hotelId) {
		this.hotelId = hotelId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Hotel() {
		
	}

	public Hotel(Integer hotelId, String name, String location, String description) {
		super();
		this.hotelId = hotelId;
		this.name = name;
		this.location = location;
		this.description = description;
	}
}