package com.capg.hotel.Repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class RoomRepositoryMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BY_ROOM_TYPE_URL =
            "/api/room/search/findByRoomType_RoomTypeId";

    private static final String BY_AVAILABLE_TRUE_URL =
            "/api/room/search/findByIsAvailableTrue";

    private static final String BY_AVAILABLE_FALSE_URL =
            "/api/room/search/findByIsAvailableFalse";

    // =======================================================
    // ✅ findByRoomType_RoomTypeId — CORRECT SCENARIOS
    // =======================================================

    @Test
    void testFindByRoomTypeId_validId_returns200() throws Exception {
        mockMvc.perform(get(BY_ROOM_TYPE_URL)
                        .param("roomTypeId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(
                        MediaType.APPLICATION_JSON));
    }

    @Test
    void testFindByRoomTypeId_returnsPageStructure() throws Exception {
        mockMvc.perform(get(BY_ROOM_TYPE_URL)
                        .param("roomTypeId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$.page.size").exists())
                .andExpect(jsonPath("$.page.totalElements").exists())
                .andExpect(jsonPath("$.page.totalPages").exists())
                .andExpect(jsonPath("$.page.number").exists());
    }

    @Test
    void testFindByRoomTypeId_returnsContentArray() throws Exception {
        mockMvc.perform(get(BY_ROOM_TYPE_URL)
                        .param("roomTypeId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void testFindByRoomTypeId_returnsLinksArray() throws Exception {
        mockMvc.perform(get(BY_ROOM_TYPE_URL)
                        .param("roomTypeId", "1")
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
    void testFindByRoomTypeId_paginationDefaultPageSize() throws Exception {
        mockMvc.perform(get(BY_ROOM_TYPE_URL)
                        .param("roomTypeId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(20))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    void testFindByRoomTypeId_selfLinkContainsSearchPath() throws Exception {
        mockMvc.perform(get(BY_ROOM_TYPE_URL)
                        .param("roomTypeId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.links[0].href",
                        containsString(
                                "/api/room/search/findByRoomType_RoomTypeId")));
    }

    @Test
    void testFindByRoomTypeId_withCustomPageSize() throws Exception {
        mockMvc.perform(get(BY_ROOM_TYPE_URL)
                        .param("roomTypeId", "1")
                        .param("size", "2")
                        .param("page", "0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    void testFindByRoomTypeId_withCustomPageSecondPage() throws Exception {
        mockMvc.perform(get(BY_ROOM_TYPE_URL)
                        .param("roomTypeId", "1")
                        .param("size", "1")
                        .param("page", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.number").value(1));
    }

    @Test
    void testFindByRoomTypeId_totalElementsIsNonNegative() throws Exception {
        mockMvc.perform(get(BY_ROOM_TYPE_URL)
                        .param("roomTypeId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements",
                        greaterThanOrEqualTo(0)));
    }

    @Test
    void testFindByRoomTypeId_pageNumberIsZeroByDefault() throws Exception {
        mockMvc.perform(get(BY_ROOM_TYPE_URL)
                        .param("roomTypeId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.number").value(0));
    }

    // =======================================================
    // ❌ findByRoomType_RoomTypeId — INCORRECT SCENARIOS
    // =======================================================

    @Test
    void testFindByRoomTypeId_nonExistentId_returnsZeroElements()
            throws Exception {
        mockMvc.perform(get(BY_ROOM_TYPE_URL)
                        .param("roomTypeId", "99999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.page.totalElements").value(0))
                .andExpect(jsonPath(
                        "$.page.totalPages").value(0));
    }

    @Test
    void testFindByRoomTypeId_negativeId_returnsZeroElements()
            throws Exception {
        mockMvc.perform(get(BY_ROOM_TYPE_URL)
                        .param("roomTypeId", "-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.page.totalElements").value(0));
    }

    @Test
    void testFindByRoomTypeId_zeroId_returnsZeroElements()
            throws Exception {
        mockMvc.perform(get(BY_ROOM_TYPE_URL)
                        .param("roomTypeId", "0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.page.totalElements").value(0));
    }

    @Test
    void testFindByRoomTypeId_missingParam_returns200WithEmpty()
            throws Exception {
        mockMvc.perform(get(BY_ROOM_TYPE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.page.totalElements").value(0));
    }

    @Test
    void testFindByRoomTypeId_stringParam_returns5xx() throws Exception {
        mockMvc.perform(get(BY_ROOM_TYPE_URL)
                        .param("roomTypeId", "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    // =======================================================
    // ✅ findByIsAvailableTrue — CORRECT SCENARIOS
    // =======================================================

    @Test
    void testFindByIsAvailableTrue_returns200() throws Exception {
        mockMvc.perform(get(BY_AVAILABLE_TRUE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(
                        MediaType.APPLICATION_JSON));
    }

    @Test
    void testFindByIsAvailableTrue_returnsPageStructure() throws Exception {
        mockMvc.perform(get(BY_AVAILABLE_TRUE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$.page.size").exists())
                .andExpect(jsonPath("$.page.totalElements").exists())
                .andExpect(jsonPath("$.page.totalPages").exists())
                .andExpect(jsonPath("$.page.number").exists());
    }

    @Test
    void testFindByIsAvailableTrue_returnsContentArray() throws Exception {
        mockMvc.perform(get(BY_AVAILABLE_TRUE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void testFindByIsAvailableTrue_paginationDefaultPageSize()
            throws Exception {
        mockMvc.perform(get(BY_AVAILABLE_TRUE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(20))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    void testFindByIsAvailableTrue_selfLinkContainsSearchPath()
            throws Exception {
        mockMvc.perform(get(BY_AVAILABLE_TRUE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.links[0].href",
                        containsString(
                                "/api/room/search/findByIsAvailableTrue")));
    }

    @Test
    void testFindByIsAvailableTrue_withCustomPageSize() throws Exception {
        mockMvc.perform(get(BY_AVAILABLE_TRUE_URL)
                        .param("size", "2")
                        .param("page", "0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    void testFindByIsAvailableTrue_withSecondPage() throws Exception {
        mockMvc.perform(get(BY_AVAILABLE_TRUE_URL)
                        .param("size", "1")
                        .param("page", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.number").value(1));
    }

    @Test
    void testFindByIsAvailableTrue_totalElementsIsNonNegative()
            throws Exception {
        mockMvc.perform(get(BY_AVAILABLE_TRUE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements",
                        greaterThanOrEqualTo(0)));
    }

    @Test
    void testFindByIsAvailableTrue_totalPagesIsNonNegative()
            throws Exception {
        mockMvc.perform(get(BY_AVAILABLE_TRUE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalPages",
                        greaterThanOrEqualTo(0)));
    }

    // =======================================================
    // ✅ findByIsAvailableFalse — CORRECT SCENARIOS
    // =======================================================

    @Test
    void testFindByIsAvailableFalse_returns200() throws Exception {
        mockMvc.perform(get(BY_AVAILABLE_FALSE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(
                        MediaType.APPLICATION_JSON));
    }

    @Test
    void testFindByIsAvailableFalse_returnsPageStructure() throws Exception {
        mockMvc.perform(get(BY_AVAILABLE_FALSE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$.page.size").exists())
                .andExpect(jsonPath("$.page.totalElements").exists())
                .andExpect(jsonPath("$.page.totalPages").exists())
                .andExpect(jsonPath("$.page.number").exists());
    }

    @Test
    void testFindByIsAvailableFalse_returnsContentArray() throws Exception {
        mockMvc.perform(get(BY_AVAILABLE_FALSE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void testFindByIsAvailableFalse_paginationDefaultPageSize()
            throws Exception {
        mockMvc.perform(get(BY_AVAILABLE_FALSE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(20))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    void testFindByIsAvailableFalse_selfLinkContainsSearchPath()
            throws Exception {
        mockMvc.perform(get(BY_AVAILABLE_FALSE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.links[0].href",
                        containsString(
                                "/api/room/search/findByIsAvailableFalse")));
    }

    @Test
    void testFindByIsAvailableFalse_withCustomPageSize() throws Exception {
        mockMvc.perform(get(BY_AVAILABLE_FALSE_URL)
                        .param("size", "2")
                        .param("page", "0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    void testFindByIsAvailableFalse_withSecondPage() throws Exception {
        mockMvc.perform(get(BY_AVAILABLE_FALSE_URL)
                        .param("size", "1")
                        .param("page", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.number").value(1));
    }

    @Test
    void testFindByIsAvailableFalse_totalElementsIsNonNegative()
            throws Exception {
        mockMvc.perform(get(BY_AVAILABLE_FALSE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements",
                        greaterThanOrEqualTo(0)));
    }

    @Test
    void testFindByIsAvailableFalse_totalPagesIsNonNegative()
            throws Exception {
        mockMvc.perform(get(BY_AVAILABLE_FALSE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalPages",
                        greaterThanOrEqualTo(0)));
    }
}