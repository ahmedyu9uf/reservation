package com.ahmedyu9uf.reservation.service.impl;

import com.ahmedyu9uf.reservation.dto.EventDto;
import com.ahmedyu9uf.reservation.exceptions.ResourceNotFoundException;
import com.ahmedyu9uf.reservation.filter.EventsFilter;
import com.ahmedyu9uf.reservation.repository.EventsRepo;
import com.ahmedyu9uf.reservation.service.EventsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventsServiceImpl implements EventsService {
    private final EventsRepo eventsRepo;

    @Override
    public List<EventDto> getEvents(EventsFilter filter, Pageable pageable) {
        return eventsRepo.getEvents(filter, pageable);
    }

    @Override
    public EventDto getEventById(long eventId) {
        return eventsRepo.getEventById(eventId).orElseThrow(() -> new ResourceNotFoundException("Event with id " + eventId + " not found"));
    }
}
