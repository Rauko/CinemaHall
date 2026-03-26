package com.cinema.dto.actor.mapper;

import com.cinema.dto.actor.ActorDto;
import com.cinema.model.Actor;

public class ActorMapper {
    public static ActorDto toDto(Actor actor){
        return new ActorDto(
                actor.getId(),
                actor.getName(),
                actor.getBirthDate(),
                actor.getBiography(),
                actor.getMovies()
        );
    }
}
