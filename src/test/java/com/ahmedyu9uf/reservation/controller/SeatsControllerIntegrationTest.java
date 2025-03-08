package com.ahmedyu9uf.reservation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SeatsControllerIntegrationTest {
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetSeatById() throws Exception {
        long seatId = 1L;

        mockMvc.perform(get("/api/v1/seats/" + seatId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(seatId))
                .andExpect(jsonPath("$.eventId").isNumber())
                .andExpect(jsonPath("$.label").isString())
                .andExpect(jsonPath("$.type").isString());
    }

    @Test
    void shouldReturnNotFoundSeat() throws Exception {
        mockMvc.perform(get("/api/v1/seats/-1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Seat with id -1 not found"));
    }
}
