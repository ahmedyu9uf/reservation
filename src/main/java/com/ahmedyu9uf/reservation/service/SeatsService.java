package com.ahmedyu9uf.reservation.service;

import com.ahmedyu9uf.reservation.dto.SeatDto;
import com.ahmedyu9uf.reservation.filter.SeatsFilter;

import java.util.List;

public interface SeatsService {
    List<SeatDto> getSeats(long eventId, SeatsFilter filter);

    SeatDto getSeatById(long id);
}
