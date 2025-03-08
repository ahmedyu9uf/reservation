package com.ahmedyu9uf.reservation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ReservationDto {
    private Long id;

    @NotNull(message = "User ID must not be null")
    private Long userId;

    @NotNull(message = "Seat ID must not be null")
    private Long seatId;

    private Instant reservedAt;
}
