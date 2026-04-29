package com.capg.hotel.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private Integer hotelId;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Location cannot be empty")
    @Size(min = 2, max = 100, message = "Location must be between 2 and 100 characters")
    @Column(name = "location", nullable = false)
    private String location;

    @NotBlank(message = "Description cannot be empty")
    @Size(min = 5, max = 200, message = "Description must be between 5 and 200 characters")
    @Column(name = "description", nullable = false)
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