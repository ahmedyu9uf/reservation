package com.ahmedyu9uf.reservation.service.impl;

import com.ahmedyu9uf.reservation.dto.EventDto;
import com.ahmedyu9uf.reservation.dto.SeatDto;
import com.ahmedyu9uf.reservation.exceptions.ResourceNotFoundException;
import com.ahmedyu9uf.reservation.repository.EventsRepo;
import com.ahmedyu9uf.reservation.repository.SeatsRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SeatServiceImplTest {

    @Mock
    private SeatsRepo seatsRepo;

    @Mock
    private EventsRepo eventsRepo;

    @InjectMocks
    private SeatServiceImpl seatService;

    @Test
    void shouldReturnSeats() {
        long eventId = 1L;
        var expected = List.of(SeatDto.builder().id(1L).eventId(eventId).build());

        when(eventsRepo.getEventById(eventId)).thenReturn(Optional.of(EventDto.builder().id(eventId).build()));
        when(seatsRepo.getSeats(anyLong(), any())).thenReturn(expected);

        List<SeatDto> actual = seatService.getSeats(eventId, null);

        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowException_whenEventNotFound() {
        var eventId = 1L;
        when(eventsRepo.getEventById(eventId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> seatService.getSeats(eventId, null));
        verify(eventsRepo).getEventById(eventId);
    }

    @Test
    void shouldReturnSeatById() {
        long seatId = 10L;
        SeatDto expected = SeatDto.builder().id(seatId).build();
        when(seatsRepo.getSeatById(seatId)).thenReturn(Optional.of(expected));

        SeatDto actual = seatService.getSeatById(seatId);

        assertEquals(expected, actual);
        verify(seatsRepo).getSeatById(seatId);
    }

    @Test
    void shouldThrowException_whenSeatNotFound() {
        long seatId = 10L;
        when(seatsRepo.getSeatById(seatId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> seatService.getSeatById(seatId));
        verify(seatsRepo).getSeatById(seatId);
    }
}
