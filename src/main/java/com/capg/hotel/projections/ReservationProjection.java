package com.capg.hotel.projections;

import org.springframework.data.rest.core.config.Projection;

import com.capg.hotel.entities.Reservation;
import com.capg.hotel.entities.Room;

@Projection(name = "reservationSummary", types = Reservation.class)
public interface ReservationProjection {
	Integer getReservationId();
	String getGuestName();
	String  getGuestPhone();
	java.time.LocalDate getCheckInDate();
	java.time.LocalDate getCheckOutDate();
	Room getRoom();
}
