package com.cinema.exception;

import com.cinema.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class ActorNotFoundException extends BaseAppException{
    public ActorNotFoundException(Long actorId){
        super("Actor not found: " + actorId,
                HttpStatus.NOT_FOUND,
                ErrorCode.ACTOR_NOT_FOUND);
    }
}
