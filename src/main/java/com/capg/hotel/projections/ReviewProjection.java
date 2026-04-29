package com.capg.hotel.projections;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import com.capg.hotel.entities.Review;

@Projection(name = "reviewSummary", types = { Review.class })
public interface ReviewProjection {

    Integer getRating();
    String getComment();
    LocalDate getReviewDate();

    @Value("#{target.reservation.guestName}")
    String getGuestName();

    @Value("#{target.reservation.room.hotel.name}")
    String getHotelName();
}