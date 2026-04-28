package com.capg.hotel.projections;

import org.springframework.data.rest.core.config.Projection;

import com.capg.hotel.entities.Amenity;
import com.capg.hotel.entities.Hotel;
import com.capg.hotel.entities.HotelAmenity;

@Projection(name = "hotelOnly", types = HotelAmenity.class)
public interface AmenityHotelsProjection {
    
    Hotel getHotel();
    Amenity getAmenity();
    
}