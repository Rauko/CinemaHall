package com.cinema.dto.screening.mapper;

import com.cinema.dto.screening.ScreeningDto;
import com.cinema.model.Screening;

public class ScreeningMapper {
    public static ScreeningDto toDto(Screening screening) {
        return new ScreeningDto(
                screening.getId(),
                screening.getStartTime(),
                screening.getDuration(),

                screening.getMovie().getId(),
                screening.getMovie().getTitle(),

                screening.getHall().getId(),
                screening.getHall().getName()
        );
    }
}
