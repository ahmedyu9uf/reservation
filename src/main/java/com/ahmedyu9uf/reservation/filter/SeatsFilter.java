package com.ahmedyu9uf.reservation.filter;

import com.ahmedyu9uf.reservation.dto.SeatType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatsFilter {
    SeatType type;
    boolean availableOnly;
}
