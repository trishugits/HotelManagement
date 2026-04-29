package com.capg.hotel.projections;

import java.math.BigDecimal;

import org.springframework.data.rest.core.config.Projection;

import com.capg.hotel.entities.RoomType;



@Projection(name = "RoomTypeSummary", types = { RoomType.class })
public interface RoomTypeProjections {
	
   Integer getRoomTypeId();
   String typeName();
   String getDescription();
   Integer getMaxOccupancy();
   BigDecimal getPricePerNight();
}
