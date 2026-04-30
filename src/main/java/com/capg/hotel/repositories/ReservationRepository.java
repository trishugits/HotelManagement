package com.capg.hotel.repositories;

import com.capg.hotel.entities.Reservation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;

@RepositoryRestResource(
        path = "reservations",
        collectionResourceRel = "reservations"
)
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    Page<Reservation> findByGuestName(String guestName, Pageable pageable);

    Page<Reservation> findByGuestPhone(String guestPhone, Pageable pageable);

    Page<Reservation> findByGuestEmail(String guestEmail, Pageable pageable);

    Page<Reservation> findByCheckInDate(LocalDate checkInDate, Pageable pageable);

    Page<Reservation> findByCheckOutDate(LocalDate checkOutDate, Pageable pageable);

    Page<Reservation> findByRoom_RoomId(Integer roomId, Pageable pageable);
}