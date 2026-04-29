package com.capg.hotel.repositories;

import com.capg.hotel.entities.Review;
import com.capg.hotel.projections.ReviewProjection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @RestResource(path = "byHotel", rel = "byHotel")
    Page<Review> findByReservation_Room_Hotel_HotelId(@Param("hotelId") Integer hotelId,Pageable pageable);
}