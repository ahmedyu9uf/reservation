package com.ahmedyu9uf.reservation.controller;

import com.ahmedyu9uf.reservation.dto.EventDto;
import com.ahmedyu9uf.reservation.dto.EventType;
import com.ahmedyu9uf.reservation.dto.SeatDto;
import com.ahmedyu9uf.reservation.dto.SeatType;
import com.ahmedyu9uf.reservation.filter.EventsFilter;
import com.ahmedyu9uf.reservation.filter.SeatsFilter;
import com.ahmedyu9uf.reservation.service.EventsService;
import com.ahmedyu9uf.reservation.service.SeatsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@Tag(name = "Events")
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventsController {
    public final static int DEFAULT_PAGE_SIZE = 50;

    private final EventsService eventsService;
    private final SeatsService seatsService;

    @GetMapping
    public ResponseEntity<EventsResponse> getEvents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) EventType type,
            @RequestParam(required = false) Instant startsAfter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "" + DEFAULT_PAGE_SIZE) int size
    ) {
        var eventFilter = EventsFilter.builder().name(name).type(type).startsAfter(startsAfter).build();
        var pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new EventsResponse(eventsService.getEvents(eventFilter, pageable)));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventById(@PathVariable long eventId) {
        return ResponseEntity.ok(eventsService.getEventById(eventId));
    }

    @GetMapping("/{eventId}/seats")
    public ResponseEntity<SeatsResponse> getEventSeats(
            @PathVariable long eventId,
            @RequestParam(required = false) SeatType type,
            @RequestParam(defaultValue = "false") boolean availableOnly
    ) {
        var filter = SeatsFilter.builder().type(type).availableOnly(availableOnly).build();
        return ResponseEntity.ok(new SeatsResponse(seatsService.getSeats(eventId, filter)));
    }

    @Data
    @AllArgsConstructor
    public static class EventsResponse {
        private List<EventDto> events;
    }

    @Data
    @AllArgsConstructor
    public static class SeatsResponse {
        private List<SeatDto> seats;
    }
}
