package com.ahmedyu9uf.reservation.filter;

import com.ahmedyu9uf.reservation.dto.EventType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class EventsFilter {
    private String name;
    private Instant startsAfter;
    private EventType type;
}
