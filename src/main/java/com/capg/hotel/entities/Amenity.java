package com.capg.hotel.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "amenity")
public class Amenity {

    @Id
    @Column(name = "amenity_id")
    private Integer amenityId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

	public Integer getAmenityId() {
		return amenityId;
	}

	public void setAmenityId(Integer amenityId) {
		this.amenityId = amenityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Amenity(Integer amenityId, String name, String description) {
		super();
		this.amenityId = amenityId;
		this.name = name;
		this.description = description;
	}

	public Amenity() {
	}
}