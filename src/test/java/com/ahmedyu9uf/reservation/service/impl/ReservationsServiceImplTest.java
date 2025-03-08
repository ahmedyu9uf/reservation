package com.ahmedyu9uf.reservation.service.impl;

import com.ahmedyu9uf.reservation.dto.ReservationDto;
import com.ahmedyu9uf.reservation.exceptions.DatabaseException;
import com.ahmedyu9uf.reservation.exceptions.ResourceNotFoundException;
import com.ahmedyu9uf.reservation.exceptions.SeatAlreadyReservedException;
import com.ahmedyu9uf.reservation.repository.ReservationsRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationsServiceImplTest {

    @Mock
    private ReservationsRepo reservationsRepo;

    @InjectMocks
    private ReservationsServiceImpl reservationsService;

    @Test
    void shouldReturnReservations() {
        List<ReservationDto> expected = List.of(ReservationDto.builder().id(1L).build());

        when(reservationsRepo.getReservations(any())).thenReturn(expected);

        List<ReservationDto> actual = reservationsService.getReservations(any());

        assertEquals(expected, actual);
        verify(reservationsRepo).getReservations(any());
    }

    @Test
    void shouldReturnReservationById() {
        long reservationId = 1L;
        ReservationDto expected = ReservationDto.builder().id(reservationId).build();

        when(reservationsRepo.getReservationById(reservationId)).thenReturn(Optional.of(expected));

        ReservationDto actual = reservationsService.getReservationById(reservationId);

        assertEquals(expected, actual);
        verify(reservationsRepo).getReservationById(reservationId);
    }

    @Test
    void shouldThrowException_whenReservationNotFound() {
        long reservationId = 1L;
        when(reservationsRepo.getReservationById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationsService.getReservationById(reservationId));
        verify(reservationsRepo).getReservationById(reservationId);
    }

    @Test
    void shouldCreateReservationSuccessfully() {
        ReservationDto reservation = ReservationDto.builder().id(1L).userId(100L).seatId(200L).build();
        when(reservationsRepo.save(reservation)).thenReturn(reservation);

        ReservationDto savedReservation = reservationsService.createReservation(reservation);

        assertEquals(reservation, savedReservation);
        verify(reservationsRepo).save(reservation);
    }

    @Test
    void shouldThrowSeatAlreadyReservedException_whenSeatIsAlreadyReserved() {
        ReservationDto reservation = ReservationDto.builder().userId(100L).seatId(200L).build();
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Duplicate entry", new SQLException("constraint", "23505"));

        when(reservationsRepo.save(reservation)).thenThrow(exception);

        assertThrows(SeatAlreadyReservedException.class, () -> reservationsService.createReservation(reservation));
        verify(reservationsRepo).save(reservation);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenUserOrSeatDoesNotExist() {
        ReservationDto reservation = ReservationDto.builder().userId(100L).seatId(200L).build();
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Foreign key violation", new SQLException("constraint", "23503"));

        when(reservationsRepo.save(reservation)).thenThrow(exception);

        assertThrows(ResourceNotFoundException.class, () -> reservationsService.createReservation(reservation));
        verify(reservationsRepo).save(reservation);
    }

    @Test
    void shouldThrowDatabaseException_whenUnexpectedDatabaseErrorOccurs() {
        ReservationDto reservation = ReservationDto.builder().userId(100L).seatId(200L).build();
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Other SQL error", new SQLException("constraint", "99999"));

        when(reservationsRepo.save(reservation)).thenThrow(exception);

        assertThrows(DatabaseException.class, () -> reservationsService.createReservation(reservation));
        verify(reservationsRepo).save(reservation);
    }

    @Test
    void shouldDeleteReservationSuccessfully() {
        long reservationId = 1L;

        reservationsService.deleteReservation(reservationId);

        verify(reservationsRepo).deleteById(reservationId);
    }
}
