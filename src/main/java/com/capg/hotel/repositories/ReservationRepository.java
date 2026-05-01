package com.capg.hotel.repositories;

import com.capg.hotel.entities.Reservation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.Optional;

@RepositoryRestResource(
        path = "reservations",
        collectionResourceRel = "reservations"
)
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
	@EntityGraph(attributePaths = {"room", "room.hotel", "room.roomType"})
	Page<Reservation> findAll(Pageable pageable);
	@EntityGraph(attributePaths = {"room", "room.hotel", "room.roomType"})
	Optional<Reservation> findById(Integer id);
	@EntityGraph(attributePaths = {"room", "room.hotel", "room.roomType"})
	Page<Reservation> findByGuestName(String guestName, Pageable pageable);

	@EntityGraph(attributePaths = {"room", "room.hotel", "room.roomType"})
	Page<Reservation> findByGuestPhone(String guestPhone, Pageable pageable);

	@EntityGraph(attributePaths = {"room", "room.hotel", "room.roomType"})
	Page<Reservation> findByGuestEmail(String guestEmail, Pageable pageable);

	@EntityGraph(attributePaths = {"room", "room.hotel", "room.roomType"})
	Page<Reservation> findByCheckInDate(LocalDate checkInDate, Pageable pageable);

	@EntityGraph(attributePaths = {"room", "room.hotel", "room.roomType"})
	Page<Reservation> findByCheckOutDate(LocalDate checkOutDate, Pageable pageable);

	@EntityGraph(attributePaths = {"room", "room.hotel", "room.roomType"})
    Page<Reservation> findByRoom_RoomId(Integer roomId, Pageable pageable);
}