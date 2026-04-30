package com.capg.hotel.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class HotelAmenityAPITest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE = "/api/hotelamenities";

    // ==========================
    // PAGINATION (LIGHTWEIGHT)
    // ==========================

    @Test
    void shouldReturnMinimalPagedResponse() throws Exception {
        mockMvc.perform(get(BASE)
                        .param("page", "0")
                        .param("size", "1"))   // 🔥 minimal load
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.size").value(1))
                .andExpect(jsonPath("$._embedded.hotelamenities").isArray());
    }

    @Test
    void shouldReturnDifferentPages() throws Exception {
        String page1 = mockMvc.perform(get(BASE)
                        .param("page", "0")
                        .param("size", "1"))
                .andReturn().getResponse().getContentAsString();

        String page2 = mockMvc.perform(get(BASE)
                        .param("page", "1")
                        .param("size", "1"))
                .andReturn().getResponse().getContentAsString();

        assertNotEquals(page1, page2);
    }

    // ==========================
    // SEARCH BY AMENITY ID
    // ==========================

    @Test
    void shouldFindByAmenityIdLightweight() throws Exception {
        mockMvc.perform(get(BASE + "/search/findByAmenityAmenityId")
                        .param("amenityId", "1")
                        .param("size", "1"))   // 🔥 minimal
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.hotelamenities").isArray());
    }

    @Test
    void shouldReturnEmptyForInvalidAmenityId() throws Exception {
        mockMvc.perform(get(BASE + "/search/findByAmenityAmenityId")
                        .param("amenityId", "-999")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.hotelamenities", hasSize(0)));
    }

    // ==========================
    // SEARCH BY HOTEL ID
    // ==========================

    @Test
    void shouldFindByHotelIdLightweight() throws Exception {
        mockMvc.perform(get(BASE + "/search/findByHotelHotelId")
                        .param("hotelId", "1")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.hotelamenities").isArray());
    }

    @Test
    void shouldReturnEmptyForInvalidHotelId() throws Exception {
        mockMvc.perform(get(BASE + "/search/findByHotelHotelId")
                        .param("hotelId", "-999")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.hotelamenities", hasSize(0)));
    }

    // ==========================
    // INVALID CASES
    // ==========================

    @Test
    void shouldHandleMissingAmenityIdGracefully() throws Exception {
        mockMvc.perform(get(BASE + "/search/findByAmenityAmenityId")
                        .param("size", "1")) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.hotelamenities", hasSize(0)));
    }

    @Test
    void shouldHandleMissingHotelIdGracefully() throws Exception {
        mockMvc.perform(get(BASE + "/search/findByHotelHotelId")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.hotelamenities", hasSize(0)));
    }

    // ==========================
    // PROJECTION TEST (LIGHT)
    // ==========================

    @Test
    void shouldReturnProjectionFields() throws Exception {
        mockMvc.perform(get(BASE)
                        .param("projection", "hotelOnly")
                        .param("size", "1"))   // 🔥 minimal
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.hotelamenities[0].hotel").exists())
                .andExpect(jsonPath("$._embedded.hotelamenities[0].amenity").exists());
    }
}