package com.capg.hotel.projections;

import org.springframework.data.rest.core.config.Projection;

import com.capg.hotel.entities.Hotel;
import com.capg.hotel.entities.Room;
import com.capg.hotel.entities.RoomType;

@Projection(name = "RoomSummary", types = { Room.class })
public interface RoomProjections {

    Integer getRoomId();
    Integer getRoomNumber();
    Boolean getIsAvailable();
    RoomType getRoomType();
    Hotel getHotel();

}