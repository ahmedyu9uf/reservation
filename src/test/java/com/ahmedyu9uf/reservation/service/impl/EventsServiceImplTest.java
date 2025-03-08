package com.ahmedyu9uf.reservation.service.impl;

import com.ahmedyu9uf.reservation.dto.EventDto;
import com.ahmedyu9uf.reservation.exceptions.ResourceNotFoundException;
import com.ahmedyu9uf.reservation.repository.EventsRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventsServiceImplTest {

    @Mock
    EventsRepo eventsRepo;

    @InjectMocks
    EventsServiceImpl eventsService;

    @Test
    void shouldReturnEvents() {
        var expected = List.of(EventDto.builder().id(1L).build());
        when(eventsRepo.getEvents(any(), any())).thenReturn(expected);

        var actual = eventsService.getEvents(null, null);

        assertEquals(expected, actual);
        verify(eventsRepo).getEvents(any(), any());
    }

    @Test
    void shouldReturnEventById() {
        var eventId = 1L;
        var expected = EventDto.builder().id(eventId).build();
        when(eventsRepo.getEventById(eventId)).thenReturn(Optional.of(expected));

        var actual = eventsService.getEventById(eventId);

        assertEquals(expected, actual);
        verify(eventsRepo).getEventById(eventId);
    }

    @Test
    void shouldThrowException() {
        var eventId = 1L;
        when(eventsRepo.getEventById(eventId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> eventsService.getEventById(eventId));
        verify(eventsRepo).getEventById(eventId);
    }

}