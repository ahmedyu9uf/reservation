package com.ahmedyu9uf.reservation.repository;

import com.ahmedyu9uf.reservation.dto.SeatDto;
import com.ahmedyu9uf.reservation.filter.SeatsFilter;
import com.ahmedyu9uf.reservation.jooq.public_.enums.SeatType;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ahmedyu9uf.reservation.jooq.public_.Tables.RESERVATIONS;
import static com.ahmedyu9uf.reservation.jooq.public_.Tables.SEATS;

@Repository
@RequiredArgsConstructor
public class SeatsRepo {
    private final DSLContext dsl;

    public List<SeatDto> getSeats(long eventId, SeatsFilter filter) {
        var baseQuery = dsl.select(SEATS.ID, SEATS.EVENT_ID, SEATS.LABEL, SEATS.TYPE)
                .from(SEATS);

        List<Condition> conditions = new ArrayList<>();
        conditions.add(SEATS.EVENT_ID.eq(eventId));
        Optional.ofNullable(filter.getType())
                .ifPresent(type -> conditions.add(SEATS.TYPE.eq(SeatType.valueOf(type.name()))));


        // Add only if filtering by availability
        if (filter.isAvailableOnly()) {
            baseQuery = baseQuery.leftJoin(RESERVATIONS).on(SEATS.ID.eq(RESERVATIONS.SEAT_ID));
            conditions.add(
                    RESERVATIONS.ID.isNull() // Seat has no reservation
            );
        }

        return baseQuery.where(conditions)
                .orderBy(SEATS.LABEL.asc())
                .fetchInto(SeatDto.class);
    }

    public Optional<SeatDto> getSeatById(long seatId) {
        return dsl.select(SEATS.ID, SEATS.EVENT_ID, SEATS.LABEL, SEATS.TYPE)
                .from(SEATS)
                .where(SEATS.ID.eq(seatId))
                .fetchOptionalInto(SeatDto.class);
    }
}
