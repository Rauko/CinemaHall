package com.cinema.service;

import com.cinema.dto.actor.request.CreateActorRequest;
import com.cinema.exception.ActorNotFoundException;
import com.cinema.model.Actor;
import com.cinema.repository.ActorRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class ActorService {

    private final ActorRepository actorRepository;
    private static final Logger log = LoggerFactory.getLogger(ActorService.class);

    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public Actor createActor(CreateActorRequest request){

        log.info("Creating actor: name={}, birthDate={}",
                request.getName(),
                request.getBirthDate()
        );

        Actor actor = new Actor();
        actor.setName(request.getName());
        actor.setBirthDate(request.getBirthDate());
        actor.setBiography(request.getBiography());

        log.info("Actor created: id={}, name={}",
                actor.getId(),
                actor.getName()
        );

        return actorRepository.save(actor);
    }

    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    public Optional<Actor> getActorById(Long id) {
        return actorRepository.findById(id);
    }

    public Actor saveActor(Actor actor) {
        return actorRepository.save(actor);
    }

    public void deleteActor(Long id) {

        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new ActorNotFoundException(id));

        log.info("Deleting actor: id={}, name={}", actor.getId(), actor.getName());

        actorRepository.delete(actor);

        log.info("Actor deleted: id={}, name={}", actor.getId(), actor.getName());
    }
}
