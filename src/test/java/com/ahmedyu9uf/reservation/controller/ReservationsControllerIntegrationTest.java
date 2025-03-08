package com.ahmedyu9uf.reservation.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReservationsControllerIntegrationTest {
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetReservationById() throws Exception {
        long reservationId = 1L;

        mockMvc.perform(get("/api/v1/reservations/" + reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reservationId))
                .andExpect(jsonPath("$.userId").isNumber())
                .andExpect(jsonPath("$.seatId").isNumber())
                .andExpect(jsonPath("$.reservedAt").isString());
    }

    @Test
    void shouldReturnNotFoundForNonExistingReservation() throws Exception {
        mockMvc.perform(get("/api/v1/reservations/-1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Reservation with id -1 not found"));
    }

    @Test
    void shouldCreateReservation() throws Exception {
        String requestBody = """
                    {
                        "userId": 1,
                        "seatId": 10
                    }
                """;

        mockMvc.perform(post("/api/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.seatId").value(10))
                .andExpect(jsonPath("$.reservedAt").isString());
    }

    @Test
    void shouldDeleteReservation() throws Exception {
        String requestBody = """
                    {
                        "userId": 1,
                        "seatId": 11
                    }
                """;

        MvcResult result = mockMvc.perform(post("/api/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber()) // Ensure ID exists
                .andReturn();

        // Extract reservationId from the response JSON
        int reservationId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(delete("/api/v1/reservations/" + reservationId))
                .andExpect(status().isNoContent());

        // Verify that the reservation no longer exists
        mockMvc.perform(get("/api/v1/reservations/" + reservationId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnSeatAlreadyReserved() throws Exception {
        String requestBody = """
                    {
                        "userId": 1,
                        "seatId": 1
                    }
                """;

        mockMvc.perform(post("/api/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Seat with id 1 is already reserved"));
    }

    @Test
    void shouldReturnNotFoundForNotExistingUser() throws Exception {
        String requestBody = """
                    {
                        "userId": -1,
                        "seatId": 99
                    }
                """;

        mockMvc.perform(post("/api/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User id -1, or seat id 99, does not exist"));
    }

    @Test
    void shouldReturnNotFoundForNotExistingSeat() throws Exception {
        String requestBody = """
                    {
                        "userId": 1,
                        "seatId": -1
                    }
                """;

        mockMvc.perform(post("/api/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User id 1, or seat id -1, does not exist"));
    }

    @Test
    void shouldReturnBadRequestWithMessageDetailForNullFields() throws Exception {
        String requestBody = """
                    {
                        "seatId": 1
                    }
                """;

        mockMvc.perform(post("/api/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("userId: User ID must not be null"));
    }
}
