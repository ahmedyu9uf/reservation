package com.ahmedyu9uf.reservation.controller;

import com.ahmedyu9uf.reservation.dto.SeatDto;
import com.ahmedyu9uf.reservation.service.SeatsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Seats")
@RestController
@RequestMapping("/api/v1/seats")
@RequiredArgsConstructor
public class SeatsController {
    private final SeatsService seatsService;

    @GetMapping("/{seatId}")
    public ResponseEntity<SeatDto> getSeatById(
            @PathVariable long seatId
    ) {
        return ResponseEntity.ok(seatsService.getSeatById(seatId));
    }
}
