package com.ahmedyu9uf.reservation.filter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationsFilter {
    private Long userId;
    private Long eventId;
}
