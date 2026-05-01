package com.capg.hotel.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ReviewRepositoryMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL =
            "/api/reviews/search/byHotel";

    // =======================================================
    // ✅ CORRECT SCENARIOS
    // =======================================================

    @Test
    void testFindByHotelId_validHotelId_returns200() throws Exception {
        // Any valid hotelId should return 200
        mockMvc.perform(get(BASE_URL)
                        .param("hotelId", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(
                        MediaType.APPLICATION_JSON));
    }

    @Test
    void testFindByHotelId_returnsPageStructure() throws Exception {
        // Verify page structure exists in response
        mockMvc.perform(get(BASE_URL)
                        .param("hotelId", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$.page.size").exists())
                .andExpect(jsonPath("$.page.totalElements").exists())
                .andExpect(jsonPath("$.page.totalPages").exists())
                .andExpect(jsonPath("$.page.number").exists());
    }

    @Test
    void testFindByHotelId_returnsContentArray() throws Exception {
        // Response uses "content" array not "_embedded"
        mockMvc.perform(get(BASE_URL)
                        .param("hotelId", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void testFindByHotelId_returnsLinksArray() throws Exception {
        // Response uses "links" array with self link
        mockMvc.perform(get(BASE_URL)
                        .param("hotelId", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.links").exists())
                .andExpect(jsonPath("$.links").isArray())
                .andExpect(jsonPath(
                        "$.links[0].rel").value("self"))
                .andExpect(jsonPath(
                        "$.links[0].href").exists());
    }

    @Test
    void testFindByHotelId_paginationDefaultPageSize() throws Exception {
        // Default page size is 20, first page is 0
        mockMvc.perform(get(BASE_URL)
                        .param("hotelId", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(20))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    void testFindByHotelId_selfLinkContainsHotelId() throws Exception {
        // Self link href must contain the search path
        mockMvc.perform(get(BASE_URL)
                        .param("hotelId", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.links[0].href",
                        containsString("/api/reviews/search/byHotel")));
    }

    @Test
    void testFindByHotelId_withCustomPageSize() throws Exception {
        // Custom page size should reflect in page.size
        mockMvc.perform(get(BASE_URL)
                        .param("hotelId", "4")
                        .param("size", "2")
                        .param("page", "0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    void testFindByHotelId_withCustomPageSecondPage() throws Exception {
        // Second page request should return page.number = 1
        mockMvc.perform(get(BASE_URL)
                        .param("hotelId", "4")
                        .param("size", "2")
                        .param("page", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.number").value(1));
    }

    @Test
    void testFindByHotelId_totalElementsIsNonNegative() throws Exception {
        // totalElements must always be >= 0
        mockMvc.perform(get(BASE_URL)
                        .param("hotelId", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements",
                        greaterThanOrEqualTo(0)));
    }

    @Test
    void testFindByHotelId_totalPagesIsNonNegative() throws Exception {
        // totalPages must always be >= 0
        mockMvc.perform(get(BASE_URL)
                        .param("hotelId", "4")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalPages",
                        greaterThanOrEqualTo(0)));
    }

    @Test
    void testFindByHotelId_pageNumberIsZeroByDefault() throws Exception {
        // Without page param, page.number should be 0
        mockMvc.perform(get(BASE_URL)
                        .param("hotelId", "4")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.number").value(0));
    }

    // =======================================================
    // ❌ INCORRECT SCENARIOS
    // =======================================================

    @Test
    void testFindByHotelId_nonExistentHotelId_returnsZeroElements()
            throws Exception {
        // hotelId 99999 does not exist → totalElements = 0
        mockMvc.perform(get(BASE_URL)
                        .param("hotelId", "99999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.page.totalElements").value(0))
                .andExpect(jsonPath(
                        "$.page.totalPages").value(0));
    }

    @Test
    void testFindByHotelId_negativeHotelId_returnsZeroElements()
            throws Exception {
        // Negative hotelId → no match → totalElements = 0
        mockMvc.perform(get(BASE_URL)
                        .param("hotelId", "-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.page.totalElements").value(0));
    }

    @Test
    void testFindByHotelId_zeroHotelId_returnsZeroElements()
            throws Exception {
        // hotelId 0 → no match → totalElements = 0
        mockMvc.perform(get(BASE_URL)
                        .param("hotelId", "0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.page.totalElements").value(0));
    }

    @Test
    void testFindByHotelId_missingParam_returns200WithEmpty()
            throws Exception {
        // Spring Data REST returns 200 with empty result
        // when required param is missing
        mockMvc.perform(get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.page.totalElements").value(0));
    }

    @Test
    void testFindByHotelId_stringParam_returns5xx() throws Exception {
        // Non-numeric hotelId → Spring returns 500
        mockMvc.perform(get(BASE_URL)
                        .param("hotelId", "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }
}