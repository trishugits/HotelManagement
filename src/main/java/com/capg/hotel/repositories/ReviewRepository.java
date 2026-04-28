package com.capg.hotel.repositories;

import com.capg.hotel.entities.Review;
import com.capg.hotel.projections.ReviewProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import java.util.List;

@RepositoryRestResource(
        path = "reviews",
        collectionResourceRel = "reviews",
        excerptProjection = ReviewProjection.class
)
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @RestResource(path = "byHotel", rel = "byHotel")
    List<Review> findByReservation_Room_Hotel_HotelId(@Param("hotelId") Integer hotelId);
}