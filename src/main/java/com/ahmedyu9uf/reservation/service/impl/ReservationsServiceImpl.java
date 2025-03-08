package com.ahmedyu9uf.reservation.service.impl;

import com.ahmedyu9uf.reservation.dto.ReservationDto;
import com.ahmedyu9uf.reservation.exceptions.DatabaseException;
import com.ahmedyu9uf.reservation.exceptions.ResourceNotFoundException;
import com.ahmedyu9uf.reservation.exceptions.SeatAlreadyReservedException;
import com.ahmedyu9uf.reservation.filter.ReservationsFilter;
import com.ahmedyu9uf.reservation.repository.ReservationsRepo;
import com.ahmedyu9uf.reservation.service.ReservationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationsServiceImpl implements ReservationsService {
    private final ReservationsRepo reservationsRepo;

    @Override
    public List<ReservationDto> getReservations(ReservationsFilter filter) {
        return reservationsRepo.getReservations(filter);
    }

    @Override
    public ReservationDto getReservationById(long id) {
        return reservationsRepo.getReservationById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation with id " + id + " not found"));
    }

    @Override
    public ReservationDto createReservation(ReservationDto reservationDto) {
        try {
            return reservationsRepo.save(reservationDto);
        } catch (DataIntegrityViolationException e) {
            handleDatabaseException(e, reservationDto.getUserId(), reservationDto.getSeatId());
            return null;
        }
    }

    @Override
    public void deleteReservation(long reservationId) {
        reservationsRepo.deleteById(reservationId);
    }

    private void handleDatabaseException(Exception e, long userId, long seatId) {
        if (e.getCause() instanceof SQLException sqlException) {
            String sqlState = sqlException.getSQLState();
            switch (sqlState) {
                case "23505":
                    throw new SeatAlreadyReservedException("Seat with id " + seatId + " is already reserved");
                case "23503":
                    throw new ResourceNotFoundException("User id " + userId + ", or seat id " + seatId + ", does not exist");
                default:
                    throw new DatabaseException("Unexpected database error", e);
            }
        }
        throw new DatabaseException("Unexpected database error", e);
    }
}
