package com.cinema.dto.ticket.request;

import com.cinema.model.enums.ImaxGlassesOption;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class CreateTicketRequest {

    @NotNull
    private Long screeningId;

    @NotNull
    private Long seatId;

    private ImaxGlassesOption glassesOption;
}
