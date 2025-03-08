package com.ahmedyu9uf.reservation.service.impl;

import com.ahmedyu9uf.reservation.dto.SeatDto;
import com.ahmedyu9uf.reservation.exceptions.ResourceNotFoundException;
import com.ahmedyu9uf.reservation.filter.SeatsFilter;
import com.ahmedyu9uf.reservation.repository.EventsRepo;
import com.ahmedyu9uf.reservation.repository.SeatsRepo;
import com.ahmedyu9uf.reservation.service.SeatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatsService {
    private final SeatsRepo seatsRepo;
    private final EventsRepo eventsRepo;

    @Override
    public List<SeatDto> getSeats(long eventId, SeatsFilter filter) {
        eventsRepo.getEventById(eventId).orElseThrow(() -> new ResourceNotFoundException("Event with id " + eventId + " not found"));
        return seatsRepo.getSeats(eventId, filter);
    }

    @Override
    public SeatDto getSeatById(long id) {
        return seatsRepo.getSeatById(id).orElseThrow(() -> new ResourceNotFoundException("Seat with id " + id + " not found"));
    }
}
