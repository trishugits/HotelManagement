package com.capg.hotel.entities;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.*;


@Entity
@Table(name = "RoomType")
public class RoomType {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "room_type_id")
	    private Integer roomTypeId;
	    
	 @NotBlank(message = "Type name is required")
	    @Size(min = 2, max = 50, message = "Type name must be between 2 and 50 characters")
	    @Column(name = "type_name", nullable = false, unique = true)
	    private String typeName;

	    @Size(max = 255, message = "Description cannot exceed 255 characters")
	    @Column(name = "description")
	    private String description;
        
	    @NotNull(message = "Max occupancy is required")
	    @Min(value = 1, message = "Max occupancy must be at least 1")
	    @Max(value = 20, message = "Max occupancy cannot exceed 20")
	    @Column(name = "max_occupancy", nullable = false)
	    private Integer maxOccupancy;

	    @NotNull(message = "Price per night is required")
	    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
	    @Digits(integer = 8, fraction = 2, message = "Price format is invalid")
	    @Column(name = "price_per_night", nullable = false)
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
