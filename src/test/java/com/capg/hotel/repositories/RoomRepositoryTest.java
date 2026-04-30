package com.capg.hotel.repositories;

import com.capg.hotel.entities.Room;
import com.capg.hotel.repositories.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoomRepositoryTest {

    @Autowired
    private RoomRepository repository;

    private final PageRequest page = PageRequest.of(0, 10);

    // =========================
    // 🔹 BASIC CRUD (SAFE)
    // =========================

    @Test
    void findById_existingId_returnsEntity() {
        Room any = repository.findAll().get(0);
        assertThat(repository.findById(any.getRoomId())).isPresent();
    }

    @Test
    void findById_nonExistingId_returnsEmpty() {
        assertThat(repository.findById(999999)).isEmpty();
    }

    // =========================
    // 🔹 QUERY METHODS (PAGEABLE)
    // =========================

    @Test
    void findByRoomTypeId_existing_returnsResults() {
        Room any = repository.findAll().get(0);
        Integer roomTypeId = any.getRoomType().getRoomTypeId();

        Page<Room> result =
                repository.findByRoomType_RoomTypeId(roomTypeId, page);

        assertThat(result.getContent()).isNotEmpty();
    }

    @Test
    void findByRoomTypeId_nonExisting_returnsEmpty() {
        Page<Room> result =
                repository.findByRoomType_RoomTypeId(999999, page);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void findByRoomTypeId_allResultsMatchRoomTypeId() {
        Room any = repository.findAll().get(0);
        Integer roomTypeId = any.getRoomType().getRoomTypeId();

        Page<Room> result =
                repository.findByRoomType_RoomTypeId(roomTypeId, page);

        assertThat(result.getContent())
                .allMatch(r -> r.getRoomType().getRoomTypeId().equals(roomTypeId));
    }

    @Test
    void findByIsAvailableTrue_returnsOnlyAvailableRooms() {
        Page<Room> result =
                repository.findByIsAvailableTrue(page);

        assertThat(result.getContent())
                .allMatch(Room::getIsAvailable);
    }

    @Test
    void findByIsAvailableFalse_returnsOnlyUnavailableRooms() {
        Page<Room> result =
                repository.findByIsAvailableFalse(page);

        assertThat(result.getContent())
                .noneMatch(Room::getIsAvailable);
    }

    // =========================
    // 🔹 BOUNDARY TESTS
    // =========================

    @Test
    void findByIsAvailableTrue_pageMetadata_isCorrect() {
        Page<Room> result =
                repository.findByIsAvailableTrue(page);

        assertThat(result.getNumber()).isZero();
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalElements()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void findByIsAvailableFalse_pageMetadata_isCorrect() {
        Page<Room> result =
                repository.findByIsAvailableFalse(page);

        assertThat(result.getNumber()).isZero();
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalElements()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void findByRoomTypeId_paginationSecondPage_returnsCorrectPage() {
        Room any = repository.findAll().get(0);
        Integer roomTypeId = any.getRoomType().getRoomTypeId();

        PageRequest secondPage = PageRequest.of(1, 1);
        Page<Room> result =
                repository.findByRoomType_RoomTypeId(roomTypeId, secondPage);

        assertThat(result.getNumber()).isEqualTo(1);
    }

    @Test
    void availableAndUnavailableCounts_sumToTotal() {
        long available   = repository.findByIsAvailableTrue(page).getTotalElements();
        long unavailable = repository.findByIsAvailableFalse(page).getTotalElements();
        long total       = repository.count();

        assertThat(available + unavailable).isEqualTo(total);
    }

    // =========================
    // 🚫 VALIDATION TESTS
    // =========================

    @Test
    void save_nullRoomNumber_shouldFail() {
        Room room = new Room();
        room.setRoomNumber(null);
        room.setIsAvailable(true);

        assertThatThrownBy(() -> repository.saveAndFlush(room))
                .isInstanceOf(jakarta.validation.ConstraintViolationException.class);
    }


    @Test
    void save_nullRoomType_shouldFail() {
        Room room = new Room();
        room.setRoomNumber(Integer.valueOf("101"));
        room.setRoomType(null);
        room.setIsAvailable(true);

        assertThatThrownBy(() -> repository.saveAndFlush(room))
                .isInstanceOf(Exception.class);
    }




}