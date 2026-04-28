package com.capg.hotel.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Amenity")
public class Amenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "amenity_id")
    private Integer amenityId;
    
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotBlank(message = "Description cannot be empty")
    @Size(min = 5, max = 200, message = "Description must be between 5 and 200 characters")
    @Column(name = "description", nullable = false)
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