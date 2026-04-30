package com.capg.hotel.repositories;

import com.capg.hotel.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TestEntityManager entityManager;

    // =======================================================
    // Helper Methods
    // =======================================================

    private Hotel saveHotel(String name, String location, String description) {
        Hotel hotel = new Hotel(null, name, location, description);
        return entityManager.persistAndFlush(hotel);
    }

    private Room saveRoom(Integer roomNumber, Boolean isAvailable, Hotel hotel) {
        RoomType roomType = entityManager.persistAndFlush(
                new RoomType(
                        null,
                        "Deluxe",
                        "Standard deluxe room",
                        2,
                        new BigDecimal("150.00")
                )
        );
        Room room = new Room(null, roomNumber, isAvailable, roomType, hotel);
        return entityManager.persistAndFlush(room);
    }
    
    private Reservation saveReservation(String guestName, String guestEmail,
                                        String guestPhone, Room room) {
        Reservation reservation = new Reservation(
                null, guestName, guestEmail, guestPhone,
                LocalDate.now(), LocalDate.now().plusDays(2), room
        );
        return entityManager.persistAndFlush(reservation);
    }

    private Review saveReview(Integer rating, String comment,
                               LocalDate reviewDate, Reservation reservation) {
        Review review = new Review(null, rating, comment, reviewDate, reservation);
        return entityManager.persistAndFlush(review);
    }

    // =======================================================
    // ✅ CORRECT SCENARIOS
    // =======================================================

    @Test
    void testFindByHotelId_singleReview() {
        Hotel hotel     = saveHotel(
                "Seaside Retreat Lodge",
                "Coastal Area",
                "Scenic lodge offering a peaceful escape by the sea."
        );
        Room room       = saveRoom(101, true, hotel);
        Reservation res = saveReservation(
                "Ethan Brown", "ethan@gmail.com", "9876543210", room
        );
        saveReview(5, "Fantastic hotel!", LocalDate.of(2023, 4, 12), res);

        Page<Review> result = reviewRepository
                .findByReservation_Room_Hotel_HotelId(
                        hotel.getHotelId(),
                        PageRequest.of(0, 10)
                );

        assertThat(result.getContent()).hasSize(1);
        assertEquals("Fantastic hotel!", result.getContent().get(0).getComment());
        assertEquals(5, result.getContent().get(0).getRating());
    }

    @Test
    void testFindByHotelId_multipleReviews() {
        // Sky High Tower Suites has 4 reviews (3, 33, 43, 46) in dataset
        Hotel hotel     = saveHotel(
                "Sky High Tower Suites",
                "Skyscraper District",
                "Exclusive suites located in the tallest tower in the city."
        );
        Room room       = saveRoom(201, true, hotel);

        Reservation res1 = saveReservation(
                "Mike Johnson", "mike@gmail.com", "9123456780", room
        );
        saveReview(3, "Room was okay, could be better.",
                LocalDate.of(2023, 6, 20), res1);

        Reservation res2 = saveReservation(
                "Ethan Davis", "ethan@gmail.com", "9234567801", room
        );
        saveReview(4, "Great value for the price.",
                LocalDate.of(2022, 6, 30), res2);

        Reservation res3 = saveReservation(
                "Logan Taylor", "logan@gmail.com", "9345678012", room
        );
        saveReview(5, "Exceptional service!",
                LocalDate.of(2023, 1, 14), res3);

        Reservation res4 = saveReservation(
                "Sophie Anderson", "sophie@gmail.com", "9456780123", room
        );
        saveReview(5, "Perfect for a weekend getaway.",
                LocalDate.of(2021, 2, 18), res4);

        Page<Review> result = reviewRepository
                .findByReservation_Room_Hotel_HotelId(
                        hotel.getHotelId(),
                        PageRequest.of(0, 10)
                );

        assertThat(result.getContent()).hasSize(4);
        assertEquals(4, result.getTotalElements());
    }

    @Test
    void testFindByHotelId_returnsCorrectRatings() {
        // Downtown Oasis Hotel has ratings 2, 5, 4 (reviews 13, 24, 25)
        Hotel hotel     = saveHotel(
                "Downtown Oasis Hotel",
                "City Center",
                "Modern hotel with a central location, perfect for business travelers."
        );
        Room room       = saveRoom(301, true, hotel);

        Reservation res1 = saveReservation(
                "Aiden Harris", "aiden@gmail.com", "9567801234", room
        );
        saveReview(2, "Not recommended, poor service.",
                LocalDate.of(2022, 9, 22), res1);

        Reservation res2 = saveReservation(
                "Emma Miller", "emma@gmail.com", "9678012345", room
        );
        saveReview(5, "Perfect getaway!",
                LocalDate.of(2022, 4, 18), res2);

        Reservation res3 = saveReservation(
                "Noah Harris", "noah@gmail.com", "9780123456", room
        );
        saveReview(4, "Impressive service and facilities.",
                LocalDate.of(2021, 5, 3), res3);

        Page<Review> result = reviewRepository
                .findByReservation_Room_Hotel_HotelId(
                        hotel.getHotelId(),
                        PageRequest.of(0, 10)
                );

        assertThat(result.getContent())
                .extracting(Review::getRating)
                .containsExactlyInAnyOrder(2, 5, 4);
    }

    @Test
    void testFindByHotelId_paginationFirstPage() {
        // Historic Heritage Inn has 3 reviews — page size 2 → first page has 2
        Hotel hotel     = saveHotel(
                "Historic Heritage Inn",
                "Historical District",
                "Charming inn showcasing rich cultural heritage."
        );
        Room room       = saveRoom(401, true, hotel);

        Reservation res1 = saveReservation(
                "Aria Martinez", "aria@gmail.com", "9801234567", room
        );
        saveReview(3, "Room was okay, could be better.",
                LocalDate.of(2021, 7, 19), res1);

        Reservation res2 = saveReservation(
                "Olivia Johnson", "olivia@gmail.com", "9812345678", room
        );
        saveReview(3, "Average service, needs improvement.",
                LocalDate.of(2022, 1, 30), res2);

        Reservation res3 = saveReservation(
                "Olivia Martinez", "oliviamartinez@gmail.com", "9823456789", room
        );
        saveReview(3, "Mixed feelings about the stay.",
                LocalDate.of(2020, 10, 14), res3);

        Page<Review> page1 = reviewRepository
                .findByReservation_Room_Hotel_HotelId(
                        hotel.getHotelId(),
                        PageRequest.of(0, 2)
                );

        assertEquals(2, page1.getContent().size());
        assertEquals(3, page1.getTotalElements());
        assertEquals(2, page1.getTotalPages());
        assertEquals(0, page1.getNumber());
    }

    @Test
    void testFindByHotelId_paginationSecondPage() {
        Hotel hotel     = saveHotel(
                "Historic Heritage Inn",
                "Historical District",
                "Charming inn showcasing rich cultural heritage."
        );
        Room room       = saveRoom(402, true, hotel);

        Reservation res1 = saveReservation(
                "Aria Martinez", "aria@gmail.com", "9801234567", room
        );
        saveReview(3, "Room was okay, could be better.",
                LocalDate.of(2021, 7, 19), res1);

        Reservation res2 = saveReservation(
                "Olivia Johnson", "olivia@gmail.com", "9812345678", room
        );
        saveReview(3, "Average service, needs improvement.",
                LocalDate.of(2022, 1, 30), res2);

        Reservation res3 = saveReservation(
                "Olivia Martinez", "oliviamartinez@gmail.com", "9823456789", room
        );
        saveReview(3, "Mixed feelings about the stay.",
                LocalDate.of(2020, 10, 14), res3);

        Page<Review> page2 = reviewRepository
                .findByReservation_Room_Hotel_HotelId(
                        hotel.getHotelId(),
                        PageRequest.of(1, 2)
                );

        assertEquals(1, page2.getContent().size());
        assertEquals(1, page2.getNumber());
    }

    @Test
    void testFindByHotelId_doesNotReturnOtherHotelsReviews() {
        Hotel hotelA    = saveHotel(
                "Oceanfront Resort & Spa",
                "Beachfront Paradise",
                "Relaxing resort with spa facilities, steps away from the ocean."
        );
        Room roomA      = saveRoom(101, true, hotelA);
        Reservation resA = saveReservation(
                "John Doe", "john@gmail.com", "9876543210", roomA
        );
        saveReview(4, "Great experience!",
                LocalDate.of(2023, 5, 15), resA);

        // Grand Plaza Hotel has no reviews
        Hotel hotelB    = saveHotel(
                "Grand Plaza Hotel",
                "Downtown City Center",
                "Luxury hotel with stunning views of the city."
        );

        Page<Review> result = reviewRepository
                .findByReservation_Room_Hotel_HotelId(
                        hotelB.getHotelId(),
                        PageRequest.of(0, 10)
                );

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void testFindByHotelId_reviewFieldsCorrectlyMapped() {
        Hotel hotel     = saveHotel(
                "Whispering Pines Inn",
                "Pine Forest",
                "Cozy inn surrounded by the whispering sounds of pine trees."
        );
        Room room       = saveRoom(501, true, hotel);
        Reservation res = saveReservation(
                "Mia Garcia", "mia@gmail.com", "9876543211", room
        );
        saveReview(4, "Great experience!",
                LocalDate.of(2023, 7, 8), res);

        Page<Review> result = reviewRepository
                .findByReservation_Room_Hotel_HotelId(
                        hotel.getHotelId(),
                        PageRequest.of(0, 10)
                );

        assertThat(result.getContent()).hasSize(1);
        Review review = result.getContent().get(0);

        assertAll(
                () -> assertNotNull(review.getReviewId()),
                () -> assertEquals(4,                         review.getRating()),
                () -> assertEquals("Great experience!",       review.getComment()),
                () -> assertEquals(LocalDate.of(2023, 7, 8), review.getReviewDate()),
                () -> assertNotNull(review.getReservation()),
                () -> assertEquals(res.getReservationId(),
                                   review.getReservation().getReservationId())
        );
    }

    // =======================================================
    // ❌ INCORRECT SCENARIOS
    // =======================================================

    @Test
    void testFindByHotelId_hotelWithNoReviews_returnsEmpty() {
        // Taj Hotel has no reviews in the dataset
        Hotel hotel = saveHotel(
                "Taj Hotel",
                "Mumbai",
                "Wonderful Location in Mumbai city."
        );

        Page<Review> result = reviewRepository
                .findByReservation_Room_Hotel_HotelId(
                        hotel.getHotelId(),
                        PageRequest.of(0, 10)
                );

        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void testFindByHotelId_nonExistentHotelId_returnsEmpty() {
        Page<Review> result = reviewRepository
                .findByReservation_Room_Hotel_HotelId(
                        99999,
                        PageRequest.of(0, 10)
                );

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByHotelId_negativeId_returnsEmpty() {
        Page<Review> result = reviewRepository
                .findByReservation_Room_Hotel_HotelId(
                        -1,
                        PageRequest.of(0, 10)
                );

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByHotelId_zeroId_returnsEmpty() {
        Page<Review> result = reviewRepository
                .findByReservation_Room_Hotel_HotelId(
                        0,
                        PageRequest.of(0, 10)
                );

        assertTrue(result.isEmpty());
    }

    // =======================================================
    // 🚫 EDGE SCENARIOS
    // =======================================================

    @Test
    void testFindByHotelId_multipleHotels_onlyTargetReturned() {
        Hotel hotelAlpine  = saveHotel(
                "Alpine Retreat Lodge",
                "Alpine Meadows",
                "Inviting lodge surrounded by alpine meadows and hiking trails."
        );
        Room roomAlpine    = saveRoom(601, true, hotelAlpine);
        Reservation resAlp = saveReservation(
                "Lucas Garcia", "lucas@gmail.com", "9654321098", roomAlpine
        );
        saveReview(4, "Pleasant stay with great views.",
                LocalDate.of(2021, 3, 15), resAlp);

        Hotel hotelCoastal  = saveHotel(
                "Coastal Breeze Resort",
                "Coastal Getaway",
                "Feel the coastal breeze in this relaxing and picturesque resort."
        );
        Room roomCoastal    = saveRoom(701, true, hotelCoastal);
        Reservation resCst  = saveReservation(
                "Logan Rodriguez", "loganr@gmail.com", "9543210987", roomCoastal
        );
        saveReview(5, "Absolutely outstanding!",
                LocalDate.of(2021, 11, 22), resCst);

        Page<Review> alpineResult = reviewRepository
                .findByReservation_Room_Hotel_HotelId(
                        hotelAlpine.getHotelId(),
                        PageRequest.of(0, 10)
                );

        assertThat(alpineResult.getContent()).hasSize(1);
        assertEquals(
                resAlp.getReservationId(),
                alpineResult.getContent().get(0).getReservation().getReservationId()
        );
    }

    @Test
    void testFindByHotelId_reviewCountMatchesTotalElements() {
        // Sapphire Shores Hotel has 3 reviews (26, 40, 50) in dataset
        Hotel hotel     = saveHotel(
                "Sapphire Shores Hotel",
                "Shorefront Paradise",
                "Experience luxury on the shores of a sapphire-blue ocean."
        );
        Room room       = saveRoom(801, true, hotel);

        Reservation res1 = saveReservation(
                "Mia Jones", "miaj@gmail.com", "9432109876", room
        );
        saveReview(2, "Not recommended, poor service.",
                LocalDate.of(2023, 8, 1), res1);

        Reservation res2 = saveReservation(
                "Aria Martinez", "ariam@gmail.com", "9321098765", room
        );
        saveReview(5, "Highly recommended!",
                LocalDate.of(2023, 8, 11), res2);

        Reservation res3 = saveReservation(
                "Grace Taylor", "grace@gmail.com", "9210987654", room
        );
        saveReview(3, "Needs improvement in cleanliness.",
                LocalDate.of(2023, 7, 8), res3);

        Page<Review> result = reviewRepository
                .findByReservation_Room_Hotel_HotelId(
                        hotel.getHotelId(),
                        PageRequest.of(0, 10)
                );

        assertEquals(result.getContent().size(), result.getTotalElements());
        assertEquals(3, result.getTotalElements());
    }
}