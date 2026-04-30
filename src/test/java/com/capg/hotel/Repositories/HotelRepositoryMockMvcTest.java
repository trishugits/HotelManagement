package com.capg.hotel.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class HotelRepositoryMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = "/api/hotels";
    private static final String BY_NAME_URL = "/api/hotels/search/findByName";
    private static final String BY_LOCATION_URL = "/api/hotels/search/findByLocation";

    // =======================================================
    // ✅ GET ALL HOTELS
    // =======================================================

    @Test
    void testGetAllHotels_returns200() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllHotels_pageStructureExists() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").exists());
    }

    @Test
    void testGetAllHotels_embeddedExists() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists());
    }

    @Test
    void testGetAllHotels_hotelsArrayExists() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.hotels").isArray());
    }

    @Test
    void testGetAllHotels_customPagination() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("size", "5")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(5))
                .andExpect(jsonPath("$._embedded.hotels",
                        hasSize(lessThanOrEqualTo(5))));
    }

    // =======================================================
    // ✅ CREATE + FETCH FLOW
    // =======================================================

    @Test
    void testCreateAndFetchHotel_flow() throws Exception {

        String requestBody = """
                {
                    "name": "Flow Test Hotel",
                    "location": "Flow City",
                    "description": "Testing create flow"
                }
                """;

        String location = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Flow Test Hotel"));
    }

    // =======================================================
    // ✅ UPDATE
    // =======================================================

    @Test
    void testUpdateHotel_flow() throws Exception {

        String createBody = """
                {
                    "name": "Hotel To Update",
                    "location": "Old City",
                    "description": "Old desc"
                }
                """;

        String location = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andReturn()
                .getResponse()
                .getHeader("Location");

        String updateBody = """
                {
                    "name": "Updated Hotel",
                    "location": "New City",
                    "description": "Updated desc"
                }
                """;

        mockMvc.perform(put(location)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isNoContent()); // ✅ FIX
    }

    // =======================================================
    // ✅ FIND BY NAME
    // =======================================================

    @Test
    void testFindByName_flow() throws Exception {

        String body = """
                {
                    "name": "Search Hotel",
                    "location": "Search City",
                    "description": "Search test"
                }
                """;

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        mockMvc.perform(get(BY_NAME_URL)
                        .param("name", "Search Hotel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.hotels[*].name",
                        everyItem(equalTo("Search Hotel"))));
    }

    // =======================================================
    // ✅ FIND BY LOCATION
    // =======================================================

    @Test
    void testFindByLocation_flow() throws Exception {

        String body = """
                {
                    "name": "Location Hotel",
                    "location": "Test City",
                    "description": "Location test"
                }
                """;

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        mockMvc.perform(get(BY_LOCATION_URL)
                        .param("location", "Test City"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.hotels[*].location",
                        everyItem(equalTo("Test City"))));
    }

    // =======================================================
    // ❌ DELETE (NOT EXPOSED)
    // =======================================================

    @Test
    void testDeleteCollection_notAllowed() throws Exception {
        mockMvc.perform(delete(BASE_URL))
                .andExpect(status().isNotFound()); // ✅ FIX
    }
}