package com.ahmedyu9uf.reservation.controller;

import com.ahmedyu9uf.reservation.dto.EventType;
import com.ahmedyu9uf.reservation.dto.SeatType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EventsControllerIntegrationTest {
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetPagedEvents() throws Exception {
        mockMvc.perform(get("/api/v1/events")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events.size()").value(3));
    }

    @Test
    void shouldGetMovieEvents() throws Exception {
        mockMvc.perform(get("/api/v1/events")
                        .param("size", "1")
                        .param("type", "MOVIE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].type").value(EventType.MOVIE.name()));
    }

    @Test
    void shouldGetEventsByName() throws Exception {
        mockMvc.perform(get("/api/v1/events")
                        .param("size", "1")
                        .param("name", "Shakespeare"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].name").value(containsString("Shakespeare")));
    }

    @Test
    void shouldGetEventsByDate() throws Exception {
        mockMvc.perform(get("/api/v1/events")
                        .param("size", "1")
                        .param("startsAfter", "2025-03-10T16:30:00Z"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetEventById() throws Exception {
        long eventId = 1L;

        mockMvc.perform(get("/api/v1/events/" + eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventId))
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.type").isString())
                .andExpect(jsonPath("$.startTime").isString());
    }

    @Test
    void shouldReturnNotFoundEvent() throws Exception {
        mockMvc.perform(get("/api/v1/events/-1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Event with id -1 not found"));
    }

    @Test
    void shouldGetEventSeats() throws Exception {
        long eventId = 1L;

        mockMvc.perform(get("/api/v1/events/" + eventId + "/seats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seats.length()", greaterThan(1)))
                .andExpect(jsonPath("$.seats[*].eventId", everyItem(is((int) eventId))));
    }

    @Test
    void shouldGetEventVIPSeats() throws Exception {
        long eventId = 1L;
        var type = SeatType.VIP;

        mockMvc.perform(get("/api/v1/events/" + eventId + "/seats")
                        .param("type", type.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seats.length()", greaterThan(1)))
                .andExpect(jsonPath("$.seats[*].eventId", everyItem(is((int) eventId))))
                .andExpect(jsonPath("$.seats[*].type", everyItem(is(type.name()))));
    }

    @Test
    void shouldGetAvailableEventSeatsOnly() throws Exception {
        long eventId = 1L, reservedSeatId = 1L;

        mockMvc.perform(get("/api/v1/events/" + eventId + "/seats").param("availableOnly", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seats.length()", greaterThan(1)))
                .andExpect(jsonPath("$.seats[*].id", not(hasItem(reservedSeatId))));
    }

    @Test
    void shouldReturnNotFoundEvent_whenGettingNotFoundEventSeats() throws Exception {
        mockMvc.perform(get("/api/v1/events/-1/seats"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Event with id -1 not found"));
    }
}
