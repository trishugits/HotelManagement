package com.capg.hotel.repositories;

import com.capg.hotel.entities.RoomType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class RoomTypeRepositoryTest {

    @Autowired
    private RoomTypeRepository repository;

    private RoomType createRoomType(String typeName, String description, int occupancy, double price) {
        RoomType rt = new RoomType();
        rt.setTypeName(typeName);
        rt.setDescription(description);
        rt.setMaxOccupancy(occupancy);
        rt.setPricePerNight(BigDecimal.valueOf(price));
        return repository.save(rt);
    }

    // =========================
    // 🔹 BASIC CRUD (4)
    // =========================

    @Test
    void save_validRoomType_persistsSuccessfully() {
        RoomType saved = createRoomType("Double", "Room", 2, 80);
        assertThat(saved.getRoomTypeId()).isNotNull();
    }

    @Test
    void findById_existingId_returnsEntity() {
        RoomType saved = createRoomType("Double", "Room", 2, 80);
        assertThat(repository.findById(saved.getRoomTypeId())).isPresent();
    }

    @Test
    void findById_nonExistingId_returnsEmpty() {
        assertThat(repository.findById(999)).isEmpty();
    }

    @Test
    void save_duplicateTypeName_throwsException() {
        createRoomType("UniqueType", "Room", 2, 80);

        RoomType duplicate = new RoomType(null, "UniqueType", "Another", 3, BigDecimal.valueOf(3000));

        assertThatThrownBy(() -> repository.saveAndFlush(duplicate))
                .isInstanceOf(Exception.class);
    }

    // =========================
    // 🔹 QUERY METHODS (6)
    // =========================

//    @Test
//    void findByTypeName_exactMatch_returnsResult() {
//        createRoomType("Deluxe", "Room", 2, 80);
//        assertThat(repository.findByTypeName("Deluxe")).hasSize(1);
//    }
//
//    @Test
//    void findByTypeName_noMatch_returnsEmpty() {
//        createRoomType("Double", "Room", 2, 80);
//        assertThat(repository.findByTypeName("Suite")).isEmpty();
//    }
//
//    @Test
//    void findByMaxOccupancyGreaterThan_returnsCorrectResults() {
//        createRoomType("TypeA", "Room", 2, 80);
//        createRoomType("TypeB", "Room", 5, 100);
//
//        assertThat(repository.findByMaxOccupancyGreaterThan(2)).hasSize(1);
//    }
//
//    @Test
//    void findByPricePerNightLessThan_returnsCorrectResults() {
//        createRoomType("TypeA", "Room", 2, 80);
//        createRoomType("TypeB", "Room", 2, 3000);
//
//        assertThat(repository.findByPricePerNightLessThan(BigDecimal.valueOf(2000)))
//                .hasSize(1);
//    }
//
//    @Test
//    void findByTypeNameContaining_partialMatch_returnsResults() {
//        createRoomType("Deluxe Room", "Room", 2, 80);
//        createRoomType("Suite Room", "Room", 1, 100);
//
//        assertThat(repository.findByTypeNameContaining("Room")).hasSize(2);
//    }
//
//    @Test
//    void findByTypeNameContaining_noMatch_returnsEmpty() {
//        createRoomType("Deluxe", "Room", 2, 80);
//        assertThat(repository.findByTypeNameContaining("XYZ")).isEmpty();
//    }
//
//    // =========================
//    // 🔹 BOUNDARY TESTS (3)
//    // =========================
//
//    @Test
//    void findByMaxOccupancyGreaterThan_boundary_excludesEqual() {
//        createRoomType("TypeA", "Room", 2, 80);
//        assertThat(repository.findByMaxOccupancyGreaterThan(2)).isEmpty();
//    }
//
//    @Test
//    void findByPricePerNightLessThan_boundary_excludesEqual() {
//        createRoomType("TypeA", "Room", 2, 2000);
//        assertThat(repository.findByPricePerNightLessThan(BigDecimal.valueOf(2000))).isEmpty();
//    }
//
//    @Test
//    void findByPricePerNightLessThan_noMatch_returnsEmpty() {
//        createRoomType("TypeA", "Room", 2, 5000);
//        assertThat(repository.findByPricePerNightLessThan(BigDecimal.valueOf(1000))).isEmpty();
//    }

    // =========================
    // 🚫 VALIDATION TESTS (7)
    // =========================

    @Test
    void save_blankTypeName_shouldFail() {
        RoomType rt = new RoomType(null, "", "Desc", 2, BigDecimal.valueOf(100));

        assertThatThrownBy(() -> repository.saveAndFlush(rt))
                .isInstanceOf(jakarta.validation.ConstraintViolationException.class);
    }

    @Test
    void save_shortTypeName_shouldFail() {
        RoomType rt = new RoomType(null, "A", "Desc", 2, BigDecimal.valueOf(100));

        assertThatThrownBy(() -> repository.saveAndFlush(rt))
                .isInstanceOf(jakarta.validation.ConstraintViolationException.class);
    }

    @Test
    void save_nullMaxOccupancy_shouldFail() {
        RoomType rt = new RoomType(null, "Deluxe", "Desc", null, BigDecimal.valueOf(100));

        assertThatThrownBy(() -> repository.saveAndFlush(rt))
                .isInstanceOf(jakarta.validation.ConstraintViolationException.class);
    }

    @Test
    void save_invalidMaxOccupancy_shouldFail() {
        RoomType rt = new RoomType(null, "Deluxe", "Desc", 0, BigDecimal.valueOf(100));

        assertThatThrownBy(() -> repository.saveAndFlush(rt))
                .isInstanceOf(jakarta.validation.ConstraintViolationException.class);
    }

    @Test
    void save_nullPrice_shouldFail() {
        RoomType rt = new RoomType(null, "Deluxe", "Desc", 2, null);

        assertThatThrownBy(() -> repository.saveAndFlush(rt))
                .isInstanceOf(jakarta.validation.ConstraintViolationException.class);
    }

    @Test
    void save_negativePrice_shouldFail() {
        RoomType rt = new RoomType(null, "Deluxe", "Desc", 2, BigDecimal.valueOf(-10));

        assertThatThrownBy(() -> repository.saveAndFlush(rt))
                .isInstanceOf(jakarta.validation.ConstraintViolationException.class);
    }

    @Test
    void update_invalidTypeName_shouldFail() {
        RoomType saved = createRoomType("Deluxe", "Desc", 2, 100);
        saved.setTypeName("");

        assertThatThrownBy(() -> repository.saveAndFlush(saved))
                .isInstanceOf(jakarta.validation.ConstraintViolationException.class);
    }
}