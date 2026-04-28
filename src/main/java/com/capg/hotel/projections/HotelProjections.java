package com.capg.hotel.projections;

import org.springframework.data.rest.core.config.Projection;
import com.capg.hotel.entities.Hotel;

@Projection(name = "hotelSummary", types = { Hotel.class })
public interface HotelProjections {

    Integer getHotelId();
    String getName();
    String getLocation();
    String getDescription();
}