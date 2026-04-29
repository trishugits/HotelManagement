package com.capg.hotel.repositories;

import com.capg.hotel.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.List;

@RepositoryRestResource(
        path = "reservations",
        collectionResourceRel = "reservations"
)
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByGuestName(String guestName);

    List<Reservation> findByGuestPhone(String guestPhone);

    List<Reservation> findByGuestEmail(String guestEmail);

    List<Reservation> findByCheckInDate(LocalDate checkInDate);

    List<Reservation> findByCheckOutDate(LocalDate checkOutDate);

    List<Reservation> findByRoom_RoomId(Integer roomId);
}