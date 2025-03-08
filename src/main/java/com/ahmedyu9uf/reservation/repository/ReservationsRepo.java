package com.ahmedyu9uf.reservation.repository;

import com.ahmedyu9uf.reservation.dto.ReservationDto;
import com.ahmedyu9uf.reservation.filter.ReservationsFilter;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ahmedyu9uf.reservation.jooq.public_.Tables.RESERVATIONS;
import static com.ahmedyu9uf.reservation.jooq.public_.Tables.SEATS;

@Repository
@RequiredArgsConstructor
public class ReservationsRepo {
    private final DSLContext dsl;

    public List<ReservationDto> getReservations(ReservationsFilter filter) {
        // Build dynamic conditions
        List<Condition> conditions = new ArrayList<>();
        Optional.ofNullable(filter.getUserId()).ifPresent(userId -> conditions.add(RESERVATIONS.USER_ID.eq(userId)));
        Optional.ofNullable(filter.getEventId()).ifPresent(eventId -> conditions.add(SEATS.EVENT_ID.eq(filter.getEventId())));

        return dsl.select(
                        RESERVATIONS.ID,
                        RESERVATIONS.USER_ID,
                        RESERVATIONS.SEAT_ID,
                        RESERVATIONS.RESERVED_AT
                )
                .from(RESERVATIONS)
                .join(SEATS).on(RESERVATIONS.SEAT_ID.eq(SEATS.ID))
                .where(conditions)
                .fetchInto(ReservationDto.class);
    }

    public Optional<ReservationDto> getReservationById(long id) {
        return dsl.select(RESERVATIONS.ID, RESERVATIONS.USER_ID, RESERVATIONS.SEAT_ID, RESERVATIONS.RESERVED_AT)
                .from(RESERVATIONS)
                .where(RESERVATIONS.ID.eq(id))
                .fetchOptionalInto(ReservationDto.class);
    }

    public ReservationDto save(ReservationDto reservation) {
        return dsl.insertInto(RESERVATIONS)
                .set(RESERVATIONS.USER_ID, reservation.getUserId())
                .set(RESERVATIONS.SEAT_ID, reservation.getSeatId())
                .set(RESERVATIONS.RESERVED_AT, Instant.now().atOffset(ZoneOffset.UTC).toLocalDateTime())
                .returning()
                .fetchOneInto(ReservationDto.class);
    }

    public void deleteById(long id) {
        dsl.deleteFrom(RESERVATIONS)
                .where(RESERVATIONS.ID.eq(id))
                .execute();
    }
}
