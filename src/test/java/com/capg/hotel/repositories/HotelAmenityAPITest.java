package com.capg.hotel.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HotelAmenityAPITest {

    @Autowired
    private MockMvc mockMvc;

    // =========================
    // ✅ VALID CASES
    // =========================

    @Test
    void shouldFetchAllHotelAmenities() throws Exception {
        mockMvc.perform(get("/hotelamenities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.hotelamenities").exists());
    }

    @Test
    void shouldFindByAmenityId() throws Exception {
        mockMvc.perform(get("/hotelamenities/search/findByAmenityAmenityId")
                        .param("amenityId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.hotelamenities").exists());
    }

    @Test
    void shouldFindByHotelId() throws Exception {
        mockMvc.perform(get("/hotelamenities/search/findByHotelHotelId")
                        .param("hotelId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.hotelamenities").exists());
    }

    // =========================
    // ❌ INVALID CASES
    // =========================

    @Test
    void shouldReturnEmptyForInvalidAmenityId() throws Exception {
        mockMvc.perform(get("/hotelamenities/search/findByAmenityAmenityId")
                        .param("amenityId", "-999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").doesNotExist());
    }

    @Test
    void shouldReturnEmptyForInvalidHotelId() throws Exception {
        mockMvc.perform(get("/hotelamenities/search/findByHotelHotelId")
                        .param("hotelId", "-999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").doesNotExist());
    }

    // =========================
    // 🚫 EDGE CASES
    // =========================

    @Test
    void shouldFailWhenAmenityIdMissing() throws Exception {
        mockMvc.perform(get("/hotelamenities/search/findByAmenityAmenityId"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailWhenHotelIdMissing() throws Exception {
        mockMvc.perform(get("/hotelamenities/search/findByHotelHotelId"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundForInvalidEndpoint() throws Exception {
        mockMvc.perform(get("/hotelamenities/invalid"))
                .andExpect(status().isNotFound());
    }
}