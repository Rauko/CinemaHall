package com.cinema.dto.ticket;

import com.cinema.model.enums.ImaxGlassesOption;
import lombok.Data;

@Data
public class CreateTicketRequest {

    private Long screeningId;
    private Long seatId;
    private ImaxGlassesOption glassesOption;
}
