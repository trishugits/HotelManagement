package com.capg.hotel.projections;

import org.springframework.data.rest.core.config.Projection;
import com.capg.hotel.entities.RoomAmenity;
import com.capg.hotel.entities.Amenity;
import com.capg.hotel.entities.Room;

@Projection(name = "roomOnly", types = RoomAmenity.class)
public interface AmenityRoomsProjection {
    
    Room getRoom();
    Amenity getAmenity();
    
}