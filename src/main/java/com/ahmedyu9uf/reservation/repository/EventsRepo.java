package com.ahmedyu9uf.reservation.repository;

import com.ahmedyu9uf.reservation.dto.EventDto;
import com.ahmedyu9uf.reservation.filter.EventsFilter;
import com.ahmedyu9uf.reservation.jooq.public_.enums.EventType;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ahmedyu9uf.reservation.jooq.public_.Tables.EVENTS;

@Repository
@RequiredArgsConstructor
public class EventsRepo {
    private final DSLContext dsl;

    public List<EventDto> getEvents(EventsFilter eventsFilter, Pageable pageable) {
        // Build dynamic conditions
        List<Condition> conditions = new ArrayList<>();

        Optional.ofNullable(eventsFilter.getName())
                .ifPresent(name -> conditions.add(EVENTS.NAME.likeIgnoreCase("%" + name + "%")));

        Optional.ofNullable(eventsFilter.getStartsAfter())
                .ifPresent(startsAfter -> conditions.add(EVENTS.START_TIME.greaterThan(startsAfter.atOffset(ZoneOffset.UTC).toLocalDateTime())));

        Optional.ofNullable(eventsFilter.getType())
                .ifPresent(eventType -> conditions.add(EVENTS.TYPE.eq(EventType.valueOf(eventType.name()))));

        return dsl.select(EVENTS.ID, EVENTS.NAME, EVENTS.START_TIME, EVENTS.TYPE)
                .from(EVENTS)
                .where(conditions)
                .orderBy(EVENTS.START_TIME.asc())
                .limit(pageable.getPageSize())
                .offset((int) pageable.getOffset())
                .fetchInto(EventDto.class);
    }

    public Optional<EventDto> getEventById(long id) {
        return dsl.select(EVENTS.ID, EVENTS.NAME, EVENTS.START_TIME, EVENTS.TYPE)
                .from(EVENTS)
                .where(EVENTS.ID.eq(id))
                .fetchOptionalInto(EventDto.class);
    }
}
