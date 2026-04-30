package com.capg.hotel.repositories;

import com.capg.hotel.entities.RoomType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoomTypeRepositoryTest {

    @Autowired
    private RoomTypeRepository repository;

    private final PageRequest page = PageRequest.of(0, 10);

    // =========================
    // 🔹 BASIC CRUD (SAFE)
    // =========================

    @Test
    void findById_existingId_returnsEntity() {
        RoomType any = repository.findAll().get(0);
        assertThat(repository.findById(any.getRoomTypeId())).isPresent();
    }

    @Test
    void findById_nonExistingId_returnsEmpty() {
        assertThat(repository.findById(999999)).isEmpty();
    }

    // =========================
    // 🔹 QUERY METHODS (PAGEABLE)
    // =========================

    @Test
    void findByTypeName_existing_returnsResults() {
        Page<RoomType> result =
                repository.findByTypeName("Single", page);

        assertThat(result.getContent()).isNotEmpty();
    }

    @Test
    void findByTypeName_noMatch_returnsEmpty() {
        Page<RoomType> result =
                repository.findByTypeName("XYZ_UNKNOWN", page);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void findByMaxOccupancyGreaterThan_returnsCorrectResults() {
        Page<RoomType> result =
                repository.findByMaxOccupancyGreaterThan(5, page);

        assertThat(result.getContent())
                .allMatch(rt -> rt.getMaxOccupancy() > 5);
    }

    @Test
    void findByPricePerNightLessThan_returnsCorrectResults() {
        Page<RoomType> result =
                repository.findByPricePerNightLessThan(BigDecimal.valueOf(100), page);

        assertThat(result.getContent())
                .allMatch(rt ->
                        rt.getPricePerNight().compareTo(BigDecimal.valueOf(100)) < 0);
    }

    @Test
    void findByTypeNameContaining_partialMatch_returnsResults() {
        Page<RoomType> result =
                repository.findByTypeNameContaining("Suite", page);

        assertThat(result.getContent()).isNotEmpty();
    }

    @Test
    void findByTypeNameContaining_noMatch_returnsEmpty() {
        Page<RoomType> result =
                repository.findByTypeNameContaining("ZZZ_UNKNOWN", page);

        assertThat(result.getContent()).isEmpty();
    }

    // =========================
    // 🔹 BOUNDARY TESTS
    // =========================

    @Test
    void findByMaxOccupancyGreaterThan_boundaryCheck() {
        Page<RoomType> result =
                repository.findByMaxOccupancyGreaterThan(2, page);

        result.forEach(rt ->
                assertThat(rt.getMaxOccupancy()).isGreaterThan(2));
    }

    @Test
    void findByPricePerNightLessThan_boundaryCheck() {
        Page<RoomType> result =
                repository.findByPricePerNightLessThan(BigDecimal.valueOf(200), page);

        result.forEach(rt ->
                assertThat(rt.getPricePerNight())
                        .isLessThan(BigDecimal.valueOf(200)));
    }

    // =========================
    // 🚫 VALIDATION TESTS
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
        RoomType existing = repository.findAll().get(0);
        existing.setTypeName("");

        assertThatThrownBy(() -> repository.saveAndFlush(existing))
                .isInstanceOf(jakarta.validation.ConstraintViolationException.class);
    }
}