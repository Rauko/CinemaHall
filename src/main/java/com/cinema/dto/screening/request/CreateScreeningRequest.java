package com.cinema.dto.screening.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CreateScreeningRequest {

    @NotNull
    private Long movieId;

    @NotBlank
    private String hallName;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private Integer duration;

    @NotNull
    private Double price;

}
