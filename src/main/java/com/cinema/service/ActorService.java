package com.cinema.service;

import com.cinema.dto.actor.request.CreateActorRequest;
import com.cinema.model.Actor;
import com.cinema.repository.ActorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActorService {
    private final ActorRepository actorRepository;

    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public Actor createActor(CreateActorRequest request){
        Actor actor = new Actor();
        actor.setName(request.getName());
        actor.setBirthDate(request.getBirthDate());
        actor.setBiography(request.getBiography());

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
        actorRepository.deleteById(id);
    }
}
