package com.capg.hotel.repositories;

import com.capg.hotel.entities.Reservation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    // =======================================================
    // FIND BY ID
    // =======================================================

    @Test
    void findById_existing_returnsReservation() {
        Optional<Reservation> result = reservationRepository.findById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getGuestName()).isEqualTo("John Doe");
    }

    @Test
    void findById_nonExisting_returnsEmpty() {
        Optional<Reservation> result = reservationRepository.findById(9999);

        assertThat(result).isEmpty();
    }

    // =======================================================
    // FIND BY GUEST NAME
    // =======================================================

    @Test
    void findByGuestName_singleResult() {
        Page<Reservation> page = reservationRepository.findByGuestName(
                "Jane Smith",
                PageRequest.of(0, 10)
        );

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).getGuestEmail()).isEqualTo("jane@example.com");
    }

    @Test
    void findByGuestName_multipleResults() {
        Page<Reservation> page = reservationRepository.findByGuestName(
                "Liam Davis",
                PageRequest.of(0, 10)
        );

        assertThat(page.getTotalElements()).isGreaterThan(1);
    }

    @Test
    void findByGuestName_notFound() {
        Page<Reservation> page = reservationRepository.findByGuestName(
                "Ghost",
                PageRequest.of(0, 10)
        );

        assertThat(page.isEmpty()).isTrue();
    }

    // =======================================================
    // FIND BY PHONE
    // =======================================================

    @Test
    void findByGuestPhone_multipleResults() {
        Page<Reservation> page = reservationRepository.findByGuestPhone(
                "1234567890",
                PageRequest.of(0, 10)
        );

        assertThat(page.getTotalElements()).isGreaterThan(1);
    }

    @Test
    void findByGuestPhone_notFound() {
        Page<Reservation> page = reservationRepository.findByGuestPhone(
                "0000000000",
                PageRequest.of(0, 10)
        );

        assertThat(page.isEmpty()).isTrue();
    }

    // =======================================================
    // FIND BY EMAIL
    // =======================================================

    @Test
    void findByGuestEmail_singleResult() {
        Page<Reservation> page = reservationRepository.findByGuestEmail(
                "john@example.com",
                PageRequest.of(0, 10)
        );

        assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findByGuestEmail_notFound() {
        Page<Reservation> page = reservationRepository.findByGuestEmail(
                "notfound@example.com",
                PageRequest.of(0, 10)
        );

        assertThat(page.isEmpty()).isTrue();
    }

    // =======================================================
    // FIND BY CHECK-IN DATE
    // =======================================================

    @Test
    void findByCheckInDate_existing() {
        Page<Reservation> page = reservationRepository.findByCheckInDate(
                LocalDate.of(2024, 1, 1),
                PageRequest.of(0, 10)
        );

        assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findByCheckInDate_notFound() {
        Page<Reservation> page = reservationRepository.findByCheckInDate(
                LocalDate.of(2030, 1, 1),
                PageRequest.of(0, 10)
        );

        assertThat(page.isEmpty()).isTrue();
    }

    // =======================================================
    // FIND BY CHECK-OUT DATE
    // =======================================================

    @Test
    void findByCheckOutDate_existing() {
        Page<Reservation> page = reservationRepository.findByCheckOutDate(
                LocalDate.of(2024, 1, 5),
                PageRequest.of(0, 10)
        );

        assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findByCheckOutDate_notFound() {
        Page<Reservation> page = reservationRepository.findByCheckOutDate(
                LocalDate.of(2030, 1, 1),
                PageRequest.of(0, 10)
        );

        assertThat(page.isEmpty()).isTrue();
    }

    // =======================================================
    // FIND BY ROOM ID
    // =======================================================

    @Test
    void findByRoomId_existing() {
        Page<Reservation> page = reservationRepository.findByRoom_RoomId(
                1,
                PageRequest.of(0, 10)
        );

        assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findByRoomId_notFound() {
        Page<Reservation> page = reservationRepository.findByRoom_RoomId(
                9999,
                PageRequest.of(0, 10)
        );

        assertThat(page.isEmpty()).isTrue();
    }

    // =======================================================
    // PAGINATION SPECIFIC TEST
    // =======================================================

    @Test
    void pagination_worksCorrectly() {
        Page<Reservation> page1 = reservationRepository.findAll(PageRequest.of(0, 5));
        Page<Reservation> page2 = reservationRepository.findAll(PageRequest.of(1, 5));

        assertThat(page1.getContent()).hasSize(5);
        assertThat(page2.getContent()).hasSize(5);
        assertThat(page1.getContent()).isNotEqualTo(page2.getContent());
    }
}