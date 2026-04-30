//package com.capg.hotel.repositories;
//
//import com.capg.hotel.entities.Hotel;
//import com.capg.hotel.entities.Reservation;
//import com.capg.hotel.entities.Room;
//import com.capg.hotel.entities.RoomType;
//import jakarta.validation.ConstraintViolationException;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
//import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@DataJpaTest
//class ReservationRepositoryTest {
//
//    @Autowired
//    private ReservationRepository reservationRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    private Room createAndPersistRoom(Integer roomNumber) {
//        Hotel hotel = new Hotel();
//        hotel.setName("Taj Hotel " + roomNumber);
//        hotel.setLocation("Mumbai");
//        hotel.setDescription("Luxury hotel in Mumbai");
//        entityManager.persist(hotel);
//
//        RoomType roomType = new RoomType();
//        roomType.setTypeName("Deluxe-" + roomNumber);
//        roomType.setDescription("Deluxe room");
//        roomType.setMaxOccupancy(2);
//        roomType.setPricePerNight(new BigDecimal("2500.0"));
//        entityManager.persist(roomType);
//
//        Room room = new Room();
//        room.setRoomNumber(roomNumber);
//        room.setIsAvailable(true);
//        room.setRoomType(roomType);
//        room.setHotel(hotel);
//        entityManager.persist(room);
//
//        return room;
//    }
//
//    private Reservation createReservation(String name, String email, String phone, Room room) {
//        return new Reservation(
//                null,
//                name,
//                email,
//                phone,
//                LocalDate.now(),
//                LocalDate.now().plusDays(2),
//                room
//        );
//    }
//
//    // =======================================================
//    // CORRECT SCENARIOS
//    // =======================================================
//
//    @Test
//    void saveReservation_valid_success() {
//        Room room = createAndPersistRoom(101);
//
//        Reservation saved = reservationRepository.saveAndFlush(
//                createReservation("John Doe", "john@example.com", "9876543210", room)
//        );
//
//        assertThat(saved).isNotNull();
//        assertThat(saved.getReservationId()).isNotNull();
//        assertThat(saved.getGuestName()).isEqualTo("John Doe");
//    }
//
//    @Test
//    void saveReservation_persistsFields() {
//        Room room = createAndPersistRoom(102);
//
//        Reservation saved = reservationRepository.saveAndFlush(
//                createReservation("Jane Smith", "jane@example.com", "9876543211", room)
//        );
//
//        entityManager.clear();
//
//        Reservation found = reservationRepository.findById(saved.getReservationId()).orElseThrow();
//
//        assertThat(found.getGuestName()).isEqualTo("Jane Smith");
//        assertThat(found.getGuestEmail()).isEqualTo("jane@example.com");
//        assertThat(found.getGuestPhone()).isEqualTo("9876543211");
//        assertThat(found.getRoom().getRoomNumber()).isEqualTo(102);
//    }
//
//    @Test
//    void updateReservation_valid_success() {
//        Room room = createAndPersistRoom(103);
//
//        Reservation saved = reservationRepository.saveAndFlush(
//                createReservation("Old Name", "old@example.com", "9876543212", room)
//        );
//
//        saved.setGuestName("New Name");
//        saved.setGuestEmail("new@example.com");
//
//        reservationRepository.saveAndFlush(saved);
//        entityManager.clear();
//
//        Reservation updated = reservationRepository.findById(saved.getReservationId()).orElseThrow();
//
//        assertThat(updated.getGuestName()).isEqualTo("New Name");
//        assertThat(updated.getGuestEmail()).isEqualTo("new@example.com");
//    }
//
//    @Test
//    void updateReservation_doesNotCreateNewRecord() {
//        Room room = createAndPersistRoom(104);
//
//        Reservation saved = reservationRepository.saveAndFlush(
//                createReservation("User One", "user1@example.com", "9876543213", room)
//        );
//
//        long countBefore = reservationRepository.count();
//
//        saved.setGuestName("Updated User");
//        reservationRepository.saveAndFlush(saved);
//
//        assertThat(reservationRepository.count()).isEqualTo(countBefore);
//    }
//
//    @Test
//    void findById_existing_returnsReservation() {
//        Room room = createAndPersistRoom(105);
//
//        Reservation saved = reservationRepository.saveAndFlush(
//                createReservation("Amit Kumar", "amit@example.com", "9876543214", room)
//        );
//
//        Optional<Reservation> result = reservationRepository.findById(saved.getReservationId());
//
//        assertThat(result).isPresent();
//        assertThat(result.get().getGuestName()).isEqualTo("Amit Kumar");
//    }
//
//    @Test
//    void findByGuestName_existing_returnsList() {
//        Room room = createAndPersistRoom(106);
//
//        reservationRepository.saveAndFlush(
//                createReservation("Alice", "alice@example.com", "9876543215", room)
//        );
//
//        List<Reservation> result = reservationRepository.findByGuestName("Alice");
//
//        assertThat(result).hasSize(1);
//        assertThat(result.get(0).getGuestName()).isEqualTo("Alice");
//    }
//
//    @Test
//    void findByGuestName_multipleSameNames_returnsMultiple() {
//        Room room1 = createAndPersistRoom(107);
//        Room room2 = createAndPersistRoom(108);
//
//        reservationRepository.saveAndFlush(
//                createReservation("Same Name", "one@example.com", "9876543216", room1)
//        );
//
//        reservationRepository.saveAndFlush(
//                createReservation("Same Name", "two@example.com", "9876543217", room2)
//        );
//
//        List<Reservation> result = reservationRepository.findByGuestName("Same Name");
//
//        assertThat(result).hasSize(2);
//    }
//
//    @Test
//    void findByGuestPhone_existing_returnsList() {
//        Room room = createAndPersistRoom(109);
//
//        reservationRepository.saveAndFlush(
//                createReservation("Rahul", "rahul@example.com", "9999999999", room)
//        );
//
//        List<Reservation> result = reservationRepository.findByGuestPhone("9999999999");
//
//        assertThat(result).hasSize(1);
//        assertThat(result.get(0).getGuestPhone()).isEqualTo("9999999999");
//    }
//
//    @Test
//    void findByGuestEmail_existing_returnsList() {
//        Room room = createAndPersistRoom(110);
//
//        reservationRepository.saveAndFlush(
//                createReservation("Neha", "neha@example.com", "9876543218", room)
//        );
//
//        List<Reservation> result = reservationRepository.findByGuestEmail("neha@example.com");
//
//        assertThat(result).hasSize(1);
//        assertThat(result.get(0).getGuestEmail()).isEqualTo("neha@example.com");
//    }
//
//    @Test
//    void findByCheckInDate_existing_returnsList() {
//        Room room = createAndPersistRoom(111);
//        LocalDate checkIn = LocalDate.now();
//
//        reservationRepository.saveAndFlush(
//                createReservation("Karan", "karan@example.com", "9876543219", room)
//        );
//
//        List<Reservation> result = reservationRepository.findByCheckInDate(checkIn);
//
//        assertThat(result).hasSize(1);
//    }
//
//    @Test
//    void findByCheckOutDate_existing_returnsList() {
//        Room room = createAndPersistRoom(112);
//        LocalDate checkOut = LocalDate.now().plusDays(2);
//
//        reservationRepository.saveAndFlush(
//                createReservation("Pooja", "pooja@example.com", "9876543220", room)
//        );
//
//        List<Reservation> result = reservationRepository.findByCheckOutDate(checkOut);
//
//        assertThat(result).hasSize(1);
//    }
//
//    @Test
//    void findByRoomId_existing_returnsList() {
//        Room room = createAndPersistRoom(113);
//
//        reservationRepository.saveAndFlush(
//                createReservation("Vikas", "vikas@example.com", "9876543221", room)
//        );
//
//        List<Reservation> result = reservationRepository.findByRoom_RoomId(room.getRoomId());
//
//        assertThat(result).hasSize(1);
//        assertThat(result.get(0).getRoom().getRoomId()).isEqualTo(room.getRoomId());
//    }
//
//    // =======================================================
//    // INCORRECT SCENARIOS - VALID INPUT BUT NO MATCH
//    // =======================================================
//
//    @Test
//    void findById_nonExisting_returnsEmpty() {
//        Optional<Reservation> result = reservationRepository.findById(99999);
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    void findByGuestName_notFound_returnsEmpty() {
//        List<Reservation> result = reservationRepository.findByGuestName("Ghost Person");
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    void findByGuestName_caseSensitive_returnsEmpty() {
//        Room room = createAndPersistRoom(114);
//
//        reservationRepository.saveAndFlush(
//                createReservation("Rohit Sharma", "rohit@example.com", "9876543222", room)
//        );
//
//        List<Reservation> result = reservationRepository.findByGuestName("rohit sharma");
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    void findByGuestPhone_notFound_returnsEmpty() {
//        List<Reservation> result = reservationRepository.findByGuestPhone("9999999998");
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    void findByGuestEmail_notFound_returnsEmpty() {
//        List<Reservation> result = reservationRepository.findByGuestEmail("nobody@example.com");
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    void findByCheckInDate_notFound_returnsEmpty() {
//        List<Reservation> result = reservationRepository.findByCheckInDate(
//                LocalDate.now().plusYears(5)
//        );
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    void findByCheckOutDate_notFound_returnsEmpty() {
//        List<Reservation> result = reservationRepository.findByCheckOutDate(
//                LocalDate.now().plusYears(5)
//        );
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    void findByRoomId_notFound_returnsEmpty() {
//        List<Reservation> result = reservationRepository.findByRoom_RoomId(99999);
//
//        assertThat(result).isEmpty();
//    }
//
//    // =======================================================
//    // INVALID SCENARIOS - VALIDATION ERRORS
//    // =======================================================
//
//    @Test
//    void saveReservation_blankGuestName_shouldFail() {
//        Room room = createAndPersistRoom(115);
//
//        Reservation reservation = createReservation(
//                "",
//                "test@example.com",
//                "9876543223",
//                room
//        );
//
//        assertThrows(ConstraintViolationException.class,
//                () -> reservationRepository.saveAndFlush(reservation));
//    }
//
//    @Test
//    void saveReservation_shortGuestName_shouldFail() {
//        Room room = createAndPersistRoom(116);
//
//        Reservation reservation = createReservation(
//                "A",
//                "test@example.com",
//                "9876543224",
//                room
//        );
//
//        assertThrows(ConstraintViolationException.class,
//                () -> reservationRepository.saveAndFlush(reservation));
//    }
//
//    @Test
//    void saveReservation_invalidEmail_shouldFail() {
//        Room room = createAndPersistRoom(117);
//
//        Reservation reservation = createReservation(
//                "Invalid Email",
//                "wrong-email",
//                "9876543225",
//                room
//        );
//
//        assertThrows(ConstraintViolationException.class,
//                () -> reservationRepository.saveAndFlush(reservation));
//    }
//
//    @Test
//    void saveReservation_invalidPhone_shouldFail() {
//        Room room = createAndPersistRoom(118);
//
//        Reservation reservation = createReservation(
//                "Invalid Phone",
//                "phone@example.com",
//                "12345",
//                room
//        );
//
//        assertThrows(ConstraintViolationException.class,
//                () -> reservationRepository.saveAndFlush(reservation));
//    }
//
//    @Test
//    void saveReservation_phoneStartingWithFive_shouldFail() {
//        Room room = createAndPersistRoom(119);
//
//        Reservation reservation = createReservation(
//                "Invalid Phone",
//                "phone2@example.com",
//                "5876543210",
//                room
//        );
//
//        assertThrows(ConstraintViolationException.class,
//                () -> reservationRepository.saveAndFlush(reservation));
//    }
//
//    @Test
//    void saveReservation_nullCheckInDate_shouldFail() {
//        Room room = createAndPersistRoom(120);
//
//        Reservation reservation = createReservation(
//                "Null Checkin",
//                "checkin@example.com",
//                "9876543226",
//                room
//        );
//
//        reservation.setCheckInDate(null);
//
//        assertThrows(ConstraintViolationException.class,
//                () -> reservationRepository.saveAndFlush(reservation));
//    }
//
//    @Test
//    void saveReservation_pastCheckInDate_shouldFail() {
//        Room room = createAndPersistRoom(121);
//
//        Reservation reservation = createReservation(
//                "Past Checkin",
//                "past@example.com",
//                "9876543227",
//                room
//        );
//
//        reservation.setCheckInDate(LocalDate.now().minusDays(1));
//
//        assertThrows(ConstraintViolationException.class,
//                () -> reservationRepository.saveAndFlush(reservation));
//    }
//
//    @Test
//    void saveReservation_nullCheckOutDate_shouldFail() {
//        Room room = createAndPersistRoom(122);
//
//        Reservation reservation = createReservation(
//                "Null Checkout",
//                "checkout@example.com",
//                "9876543228",
//                room
//        );
//
//        reservation.setCheckOutDate(null);
//
//        assertThrows(ConstraintViolationException.class,
//                () -> reservationRepository.saveAndFlush(reservation));
//    }
//
//    @Test
//    void saveReservation_todayCheckOutDate_shouldFail() {
//        Room room = createAndPersistRoom(123);
//
//        Reservation reservation = createReservation(
//                "Today Checkout",
//                "today@example.com",
//                "9876543229",
//                room
//        );
//
//        reservation.setCheckOutDate(LocalDate.now());
//
//        assertThrows(ConstraintViolationException.class,
//                () -> reservationRepository.saveAndFlush(reservation));
//    }
//
//    @Test
//    void saveReservation_withoutRoom_shouldFail() {
//        Reservation reservation = createReservation(
//                "No Room",
//                "noroom@example.com",
//                "9876543230",
//                null
//        );
//
//        assertThrows(ConstraintViolationException.class,
//                () -> reservationRepository.saveAndFlush(reservation));
//    }
//}