package com.ahmedyu9uf.reservation.service;

import com.ahmedyu9uf.reservation.dto.ReservationDto;
import com.ahmedyu9uf.reservation.filter.ReservationsFilter;

import java.util.List;

public interface ReservationsService {
    List<ReservationDto> getReservations(ReservationsFilter filter);

    ReservationDto getReservationById(long id);

    ReservationDto createReservation(ReservationDto reservationDto);

    void deleteReservation(long reservationId);
}
