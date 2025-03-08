package com.ahmedyu9uf.reservation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatDto {
    private Long id;
    private Long eventId;
    private String label;
    private SeatType type;
}
