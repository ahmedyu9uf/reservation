package com.ahmedyu9uf.reservation.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class EventDto {
    private Long id;
    private String name;
    private Instant startTime;
    private EventType type;
}
