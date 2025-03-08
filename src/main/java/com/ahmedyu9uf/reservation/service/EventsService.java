package com.ahmedyu9uf.reservation.service;

import com.ahmedyu9uf.reservation.dto.EventDto;
import com.ahmedyu9uf.reservation.filter.EventsFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventsService {
    List<EventDto> getEvents(EventsFilter filter, Pageable pageable);

    EventDto getEventById(long eventId);
}
