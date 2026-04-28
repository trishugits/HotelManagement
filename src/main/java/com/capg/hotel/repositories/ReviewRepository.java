package com.capg.hotel.repositories;

import com.capg.hotel.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

@RepositoryRestResource(
        path = "reviews",
        collectionResourceRel = "reviews"
)
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByReservation_Room_Hotel_HotelId(Integer hotelId);
}