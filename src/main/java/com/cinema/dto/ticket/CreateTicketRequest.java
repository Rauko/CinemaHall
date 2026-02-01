package com.cinema.dto.ticket;

import com.cinema.model.ImaxGlassesOption;
import lombok.Data;

@Data
public class CreateTicketRequest {

    private Long screeningId;
    private Long seatId;
    private ImaxGlassesOption glassesOption;
}
