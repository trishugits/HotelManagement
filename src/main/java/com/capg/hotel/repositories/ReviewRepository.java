package com.capg.hotel.repositories;

import com.capg.hotel.entities.Review;
import com.capg.hotel.projections.ReviewProjection;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;


@RepositoryRestResource(
        path = "reviews",
        collectionResourceRel = "reviews",
        excerptProjection = ReviewProjection.class
)
public interface ReviewRepository extends JpaRepository<Review, Integer> {
	@EntityGraph(attributePaths = {"reservation", "reservation.room", "reservation.room.hotel", "reservation.room.roomType"})
	Page<Review> findAll(Pageable pageable);

	@EntityGraph(attributePaths = {"reservation", "reservation.room", "reservation.room.hotel", "reservation.room.roomType"})
	Optional<Review> findById(Integer id);

	@EntityGraph(attributePaths = {"reservation", "reservation.room", "reservation.room.hotel", "reservation.room.roomType"})
	@RestResource(path = "byHotel", rel = "byHotel")
    Page<Review> findByReservation_Room_Hotel_HotelId(@Param("hotelId") Integer hotelId,Pageable pageable);
}