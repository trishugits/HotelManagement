package com.capg.hotel.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RoomTypeAPITest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = "/api/roomtypes";
    private static final String SEARCH_URL = BASE_URL + "/search";

    // =======================================================
    // ✅ CRUD TEST CASES
    // =======================================================

    @Test
    void testGetAllRoomTypes() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void testGetRoomTypeById_valid() throws Exception {
        mockMvc.perform(get(BASE_URL + "/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetRoomTypeById_notFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/9999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateRoomType_valid() throws Exception {
        String json = """
                {
                    "typeName": "Deluxe",
                    "description": "Luxury room",
                    "maxOccupancy": 3,
                    "pricePerNight": 2500
                }
                """;

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testCreateRoomType_invalid_missingFields() throws Exception {
        String json = "{}";

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateRoomType_put() throws Exception {
        String json = """
                {
                    "typeName": "Updated Room",
                    "description": "Updated desc",
                    "maxOccupancy": 4,
                    "pricePerNight": 3000
                }
                """;

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateRoomType_patch() throws Exception {
        String json = """
                {
                    "pricePerNight": 1800
                }
                """;

        mockMvc.perform(patch(BASE_URL + "/1")
                        .contentType("application/merge-patch+json")
                        .content(json))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteRoomType_shouldFail() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isMethodNotAllowed()); // DELETE disabled
    }

    // =======================================================
    // ✅ SEARCH TEST CASES
    // =======================================================

    @Test
    void testFindByTypeName_valid_returns200() throws Exception {
        mockMvc.perform(get(SEARCH_URL + "/findByTypeName")
                        .param("typeName", "Single"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByTypeName_returnsPageStructure() throws Exception {
        mockMvc.perform(get(SEARCH_URL + "/findByTypeName")
                        .param("typeName", "Single"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").exists());
    }

    @Test
    void testFindByTypeName_defaultPagination() throws Exception {
        mockMvc.perform(get(SEARCH_URL + "/findByTypeName")
                        .param("typeName", "Single"))
                .andExpect(jsonPath("$.page.size").value(20));
    }

    @Test
    void testFindByMaxOccupancyGreaterThan() throws Exception {
        mockMvc.perform(get(SEARCH_URL + "/findByMaxOccupancyGreaterThan")
                        .param("value", "2"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByPricePerNightLessThan() throws Exception {
        mockMvc.perform(get(SEARCH_URL + "/findByPricePerNightLessThan")
                        .param("price", "5000"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByTypeNameContaining() throws Exception {
        mockMvc.perform(get(SEARCH_URL + "/findByTypeNameContaining")
                        .param("keyword", "Del"))
                .andExpect(status().isOk());
    }

    // =======================================================
    // ❌ NEGATIVE CASES
    // =======================================================

    @Test
    void testFindByTypeName_noMatch() throws Exception {
        mockMvc.perform(get(SEARCH_URL + "/findByTypeName")
                        .param("typeName", "XYZ"))
                .andExpect(jsonPath("$.page.totalElements").value(0));
    }

    @Test
    void testInvalidParam_shouldFail() throws Exception {
        mockMvc.perform(get(SEARCH_URL + "/findByMaxOccupancyGreaterThan")
                        .param("value", "abc"))
                .andExpect(status().is5xxServerError());
    }
}