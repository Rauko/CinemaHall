package com.cinema.dto.actor.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CreateActorRequest {
    @NotBlank
    private String name;

    private LocalDate birthDate;

    private String biography;
}
