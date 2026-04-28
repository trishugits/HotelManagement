package com.capg.hotel.entities;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
@Table(name = "RoomType")
public class RoomType {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "room_type_id")
	    private Integer roomTypeId;
	    

	    @Column(name = "type_name")
	    private String typeName;

	    @Column(name = "description")
	    private String description;

	    @Column(name = "max_occupancy")
	    private Integer maxOccupancy;

	    @Column(name = "price_per_night")
	    private BigDecimal pricePerNight;


		public Integer getRoomTypeId() {
			return roomTypeId;
		}

		public void setRoomTypeId(Integer roomTypeId) {
			this.roomTypeId = roomTypeId;
		}

		public String getTypeName() {
			return typeName;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Integer getMaxOccupancy() {
			return maxOccupancy;
		}

		public void setMaxOccupancy(Integer maxOccupancy) {
			this.maxOccupancy = maxOccupancy;
		}

		public BigDecimal getPricePerNight() {
			return pricePerNight;
		}

		public void setPricePerNight(BigDecimal pricePerNight) {
			this.pricePerNight = pricePerNight;
		}

		public RoomType() {
		
		}

		public RoomType(Integer roomTypeId, String typeName, String description, Integer maxOccupancy,
				BigDecimal pricePerNight) {
			super();
			this.roomTypeId = roomTypeId;
			this.typeName = typeName;
			this.description = description;
			this.maxOccupancy = maxOccupancy;
			this.pricePerNight = pricePerNight;
		}

	
	    

}
