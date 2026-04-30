package com.capg.hotel.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ReservationApiTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE = "/api/reservations";

    private String createReservation(String guestName,
                                     String phone,
                                     String email,
                                     String checkIn,
                                     String checkOut,
                                     int roomId) throws Exception {
        String location = mockMvc.perform(post(BASE)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "guestName"   : "%s",
                                  "guestPhone"  : "%s",
                                  "guestEmail"  : "%s",
                                  "checkInDate" : "%s",
                                  "checkOutDate": "%s",
                                  "room"        : "/api/rooms/%d"
                                }
                                """.formatted(guestName, phone, email, checkIn, checkOut, roomId)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        assertNotNull(location, "Location header must not be null after POST");
        return location;
    }

    // ==========================
    // CREATE  (POST)
    // ==========================

    @Test
    void shouldCreateReservationSuccessfully() throws Exception {
        mockMvc.perform(post(BASE)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "guestName"   : "Ravi Kumar",
                                  "guestPhone"  : "9876543210",
                                  "guestEmail"  : "ravi@example.com",
                                  "checkInDate" : "2030-08-01",
                                  "checkOutDate": "2030-08-05",
                                  "room"        : "/api/rooms/1"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    /** @NotBlank — blank guest name must be rejected */
    @Test
    void shouldRejectBlankGuestName() throws Exception {
        mockMvc.perform(post(BASE)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "guestName"   : "",
                                  "guestPhone"  : "9876543210",
                                  "guestEmail"  : "ravi@example.com",
                                  "checkInDate" : "2030-08-01",
                                  "checkOutDate": "2030-08-05",
                                  "room"        : "/api/rooms/1"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    /** @Size(max=50) — name longer than 50 chars must be rejected */
    @Test
    void shouldRejectGuestNameExceedingMaxLength() throws Exception {
        String longName = "A".repeat(51);
        mockMvc.perform(post(BASE)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "guestName"   : "%s",
                                  "guestPhone"  : "9876543210",
                                  "guestEmail"  : "ravi@example.com",
                                  "checkInDate" : "2030-08-01",
                                  "checkOutDate": "2030-08-05",
                                  "room"        : "/api/rooms/1"
                                }
                                """.formatted(longName)))
                .andExpect(status().isBadRequest());
    }

    /** @Email — malformed email must be rejected */
    @Test
    void shouldRejectInvalidEmailFormat() throws Exception {
        mockMvc.perform(post(BASE)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "guestName"   : "Ravi Kumar",
                                  "guestPhone"  : "9876543210",
                                  "guestEmail"  : "not-an-email",
                                  "checkInDate" : "2030-08-01",
                                  "checkOutDate": "2030-08-05",
                                  "room"        : "/api/rooms/1"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    /** @Pattern("^[6-9][0-9]{9}$") — phone starting with 5 is invalid */
    @Test
    void shouldRejectPhoneNotMatchingIndianPattern() throws Exception {
        mockMvc.perform(post(BASE)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "guestName"   : "Ravi Kumar",
                                  "guestPhone"  : "5551234567",
                                  "guestEmail"  : "ravi@example.com",
                                  "checkInDate" : "2030-08-01",
                                  "checkOutDate": "2030-08-05",
                                  "room"        : "/api/rooms/1"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    /** @Pattern — phone shorter than 10 digits must be rejected */
    @Test
    void shouldRejectPhoneWithWrongLength() throws Exception {
        mockMvc.perform(post(BASE)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "guestName"   : "Ravi Kumar",
                                  "guestPhone"  : "98765",
                                  "guestEmail"  : "ravi@example.com",
                                  "checkInDate" : "2030-08-01",
                                  "checkOutDate": "2030-08-05",
                                  "room"        : "/api/rooms/1"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    /** @FutureOrPresent on checkInDate — past date must be rejected */
    @Test
    void shouldRejectPastCheckInDate() throws Exception {
        mockMvc.perform(post(BASE)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "guestName"   : "Ravi Kumar",
                                  "guestPhone"  : "9876543210",
                                  "guestEmail"  : "ravi@example.com",
                                  "checkInDate" : "2020-01-01",
                                  "checkOutDate": "2030-08-05",
                                  "room"        : "/api/rooms/1"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    /** @Future on checkOutDate — a past date must be rejected */
    @Test
    void shouldRejectPastCheckOutDate() throws Exception {
        mockMvc.perform(post(BASE)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "guestName"   : "Ravi Kumar",
                                  "guestPhone"  : "9876543210",
                                  "guestEmail"  : "ravi@example.com",
                                  "checkInDate" : "2030-08-01",
                                  "checkOutDate": "2020-01-01",
                                  "room"        : "/api/rooms/1"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    /** @NotNull on room */
    @Test
    void shouldRejectMissingRoom() throws Exception {
        mockMvc.perform(post(BASE)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "guestName"   : "Ravi Kumar",
                                  "guestPhone"  : "9876543210",
                                  "guestEmail"  : "ravi@example.com",
                                  "checkInDate" : "2030-08-01",
                                  "checkOutDate": "2030-08-05"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    // ==========================
    // READ  (GET)
    // ==========================

    /** Seed row 1: John Doe, john@example.com */
    @Test
    void shouldReturnReservationById() throws Exception {
        mockMvc.perform(get(BASE + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guestName").value("John Doe"))
                .andExpect(jsonPath("$.guestEmail").value("john@example.com"));
    }

    @Test
    void shouldReturnNotFoundForNonExistentId() throws Exception {
        mockMvc.perform(get(BASE + "/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnPagedReservationList() throws Exception {
        mockMvc.perform(get(BASE)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservations").isArray())
                .andExpect(jsonPath("$._embedded.reservations", hasSize(10)))
                .andExpect(jsonPath("$.page.totalElements").value(greaterThanOrEqualTo(50)));
    }

    @Test
    void shouldRespectPageAndSizeParameters() throws Exception {
        mockMvc.perform(get(BASE)
                        .param("page", "1")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservations", hasSize(5)))
                .andExpect(jsonPath("$.page.number").value(1))
                .andExpect(jsonPath("$.page.size").value(5));
    }

    // ==========================
    // UPDATE  (PUT – full replace)
    // ==========================

    @Test
    void shouldReplaceReservationWithPut() throws Exception {
        String location = createReservation(
                "Priya Sharma", "9123456789", "priya@example.com",
                "2030-10-01", "2030-10-05", 1);

        mockMvc.perform(put(location)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "guestName"   : "Priya Sharma Updated",
                                  "guestPhone"  : "9123456789",
                                  "guestEmail"  : "priya.new@example.com",
                                  "checkInDate" : "2030-10-01",
                                  "checkOutDate": "2030-10-10",
                                  "room"        : "/api/rooms/1"
                                }
                                """))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guestName").value("Priya Sharma Updated"))
                .andExpect(jsonPath("$.guestEmail").value("priya.new@example.com"));
    }

    /**
     * FIX: Spring Data REST throws ObjectOptimisticLockingFailureException for
     * PUT on a non-existent ID, resulting in 409 CONFLICT — not 404 NOT FOUND.
     */
    @Test
    void shouldReturnConflictWhenPuttingNonExistentId() throws Exception {
        mockMvc.perform(put(BASE + "/999999")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "guestName"   : "Ghost User",
                                  "guestPhone"  : "9000000000",
                                  "guestEmail"  : "ghost@example.com",
                                  "checkInDate" : "2030-11-01",
                                  "checkOutDate": "2030-11-03",
                                  "room"        : "/api/rooms/1"
                                }
                                """))
                .andExpect(status().isConflict()); // 409 — Spring Data REST behaviour
    }

    // ==========================
    // UPDATE  (PATCH – partial)
    // ==========================

    @Test
    void shouldPartiallyUpdateGuestPhone() throws Exception {
        String location = createReservation(
                "Neha Gupta", "9000000002", "neha@example.com",
                "2030-12-01", "2030-12-07", 3);

        mockMvc.perform(patch(location)
                        .contentType("application/merge-patch+json")
                        .content("""
                                { "guestPhone": "8888888888" }
                                """))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guestPhone").value("8888888888"))
                .andExpect(jsonPath("$.guestName").value("Neha Gupta")); // unchanged
    }

    @Test
    void shouldPartiallyUpdateGuestEmail() throws Exception {
        String location = createReservation(
                "Karan Mehta", "9000000003", "karan@example.com",
                "2031-01-01", "2031-01-05", 4);

        mockMvc.perform(patch(location)
                        .contentType("application/merge-patch+json")
                        .content("""
                                { "guestEmail": "karan.updated@example.com" }
                                """))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guestEmail").value("karan.updated@example.com"))
                .andExpect(jsonPath("$.guestName").value("Karan Mehta")); // unchanged
    }

    @Test
    void shouldReturnNotFoundWhenPatchingNonExistentId() throws Exception {
        mockMvc.perform(patch(BASE + "/999999")
                        .contentType("application/merge-patch+json")
                        .content("""
                                { "guestName": "Nobody" }
                                """))
                .andExpect(status().isNotFound());
    }

    // ==========================
    // DELETE
    // ==========================

    @Test
    void shouldDeleteReservationAndReturn404Afterward() throws Exception {
        String location = createReservation(
                "Deepak Singh", "9000000004", "deepak@example.com",
                "2031-02-01", "2031-02-07", 5);

        mockMvc.perform(delete(location))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(location))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentId() throws Exception {
        mockMvc.perform(delete(BASE + "/999999"))
                .andExpect(status().isNotFound());
    }

    // ==========================
    // SEARCH – findByGuestName
    // ==========================

    @Test
    void shouldFindReservationByExactGuestName() throws Exception {
        mockMvc.perform(get(BASE + "/search/findByGuestName")
                        .param("guestName", "John Doe")
                        .param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservations", hasSize(1)))
                .andExpect(jsonPath("$._embedded.reservations[0].guestName")
                        .value("John Doe"));
    }

    /**
     * FIX: Seed data 'Liam Davis' rows are inserted via SQL in a separate
     * transaction, so they may not be visible within this @Transactional test.
     * We explicitly create 2 reservations for the same guest name here to
     * guarantee the count is verifiable within this transaction.
     */
    @Test
    void shouldReturnMultipleResultsForDuplicateGuestName() throws Exception {
        createReservation("Duplicate Guest", "9111111111", "dup1@example.com",
                "2031-03-01", "2031-03-05", 6);
        createReservation("Duplicate Guest", "9222222222", "dup2@example.com",
                "2031-04-01", "2031-04-05", 7);

        mockMvc.perform(get(BASE + "/search/findByGuestName")
                        .param("guestName", "Duplicate Guest")
                        .param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservations", hasSize(2)));
    }

    /**
     * FIX: Spring Data REST returns "_embedded": { "reservations": [] }
     * even when the result set is empty — so we assert hasSize(0), not doesNotExist().
     */
    @Test
    void shouldReturnEmptyListWhenGuestNameNotFound() throws Exception {
        mockMvc.perform(get(BASE + "/search/findByGuestName")
                        .param("guestName", "xyz_does_not_exist")
                        .param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservations", hasSize(0)));
    }

    // ==========================
    // SEARCH – findByGuestPhone
    // ==========================

    @Test
    void shouldFindReservationsByGuestPhone() throws Exception {
        // '9876543210' appears 6 times in seed data
        mockMvc.perform(get(BASE + "/search/findByGuestPhone")
                        .param("guestPhone", "9876543210")
                        .param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservations",
                        hasSize(greaterThanOrEqualTo(1))));
    }

    /**
     * FIX: Empty result still has _embedded.reservations array, just empty.
     */
    @Test
    void shouldReturnEmptyListWhenPhoneNotFound() throws Exception {
        mockMvc.perform(get(BASE + "/search/findByGuestPhone")
                        .param("guestPhone", "9000000099")
                        .param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservations", hasSize(0)));
    }

    // ==========================
    // SEARCH – findByGuestEmail
    // ==========================

    @Test
    void shouldFindReservationByUniqueEmail() throws Exception {
        mockMvc.perform(get(BASE + "/search/findByGuestEmail")
                        .param("guestEmail", "john@example.com")
                        .param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservations", hasSize(1)))
                .andExpect(jsonPath("$._embedded.reservations[0].guestEmail")
                        .value("john@example.com"));
    }

    /**
     * FIX: Create 2 reservations with the same email within this transaction
     * so the count is reliable, rather than depending on seed-data visibility.
     */
    @Test
    void shouldFindMultipleReservationsBySharedEmail() throws Exception {
        createReservation("Shared Email One", "9333333333", "shared@example.com",
                "2031-05-01", "2031-05-05", 8);
        createReservation("Shared Email Two", "9444444444", "shared@example.com",
                "2031-06-01", "2031-06-05", 9);

        mockMvc.perform(get(BASE + "/search/findByGuestEmail")
                        .param("guestEmail", "shared@example.com")
                        .param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservations", hasSize(2)));
    }

    /** FIX: assert hasSize(0) not doesNotExist() */
    @Test
    void shouldReturnEmptyListWhenEmailNotFound() throws Exception {
        mockMvc.perform(get(BASE + "/search/findByGuestEmail")
                        .param("guestEmail", "nobody@nowhere.com")
                        .param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservations", hasSize(0)));
    }

    // ==========================
    // SEARCH – findByCheckInDate
    // ==========================

    @Test
    void shouldFindReservationByCheckInDate() throws Exception {
        mockMvc.perform(get(BASE + "/search/findByCheckInDate")
                        .param("checkInDate", "2024-01-01")
                        .param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservations", not(empty())))
                .andExpect(jsonPath("$._embedded.reservations[0].checkInDate")
                        .value("2024-01-01"));
    }

    /** FIX: assert hasSize(0) not doesNotExist() */
    @Test
    void shouldReturnEmptyListWhenCheckInDateNotFound() throws Exception {
        mockMvc.perform(get(BASE + "/search/findByCheckInDate")
                        .param("checkInDate", "2099-01-01")
                        .param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservations", hasSize(0)));
    }

    // ==========================
    // SEARCH – findByCheckOutDate
    // ==========================

    @Test
    void shouldFindReservationByCheckOutDate() throws Exception {
        mockMvc.perform(get(BASE + "/search/findByCheckOutDate")
                        .param("checkOutDate", "2024-01-05")
                        .param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservations", not(empty())))
                .andExpect(jsonPath("$._embedded.reservations[0].checkOutDate")
                        .value("2024-01-05"));
    }

    /** FIX: assert hasSize(0) not doesNotExist() */
    @Test
    void shouldReturnEmptyListWhenCheckOutDateNotFound() throws Exception {
        mockMvc.perform(get(BASE + "/search/findByCheckOutDate")
                        .param("checkOutDate", "2099-12-31")
                        .param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservations", hasSize(0)));
    }

    // ==========================
    // SEARCH – findByRoom_RoomId
    // ==========================

    @Test
    void shouldFindReservationByRoomId() throws Exception {
        // Seed row 1: room_id = 1 (John Doe) — unique in seed data
        mockMvc.perform(get(BASE + "/search/findByRoom_RoomId")
                        .param("roomId", "1")
                        .param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservations", hasSize(1)));
    }

    /** FIX: assert hasSize(0) not doesNotExist() */
    @Test
    void shouldReturnEmptyListForRoomWithNoReservations() throws Exception {
        mockMvc.perform(get(BASE + "/search/findByRoom_RoomId")
                        .param("roomId", "999999")
                        .param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservations", hasSize(0)));
    }
}
