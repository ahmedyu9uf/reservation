package com.ahmedyu9uf.reservation.controller;

import com.ahmedyu9uf.reservation.dto.ReservationDto;
import com.ahmedyu9uf.reservation.filter.ReservationsFilter;
import com.ahmedyu9uf.reservation.service.ReservationsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Reservation")
@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationsController {
    private final ReservationsService reservationsService;

    @GetMapping
    public ResponseEntity<ReservationsResponse> getReservations(
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) Long userId
    ) {
        var filter = ReservationsFilter.builder().eventId(eventId).userId(userId).build();
        return ResponseEntity.ok(new ReservationsResponse(reservationsService.getReservations(filter)));
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationDto> getReservationById(
            @PathVariable Long reservationId
    ) {
        return ResponseEntity.ok(reservationsService.getReservationById(reservationId));
    }

    @PostMapping
    public ResponseEntity<ReservationDto> createReservation(
            @Valid @RequestBody ReservationDto reservationDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationsService.createReservation(reservationDto));
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservations(
            @PathVariable Long reservationId
    ) {
        reservationsService.deleteReservation(reservationId);
        return ResponseEntity.noContent().build();
    }

    @Data
    @AllArgsConstructor
    public static class ReservationsResponse {
        private List<ReservationDto> reservations;
    }
}
