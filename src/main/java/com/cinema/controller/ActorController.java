package com.cinema.controller;

import com.cinema.dto.actor.ActorDto;
import com.cinema.dto.actor.mapper.ActorMapper;
import com.cinema.dto.actor.request.CreateActorRequest;
import com.cinema.model.Actor;
import com.cinema.service.ActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/actors")
public class ActorController {

    private final ActorService actorService;

    @GetMapping("/all")
    public List<ActorDto> findAll() {
        return actorService.getAllActors()
                .stream()
                .map(ActorMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorDto> getActorById(@PathVariable Long id) {
        return actorService.getActorById(id)
                .map(actor -> ResponseEntity.ok(ActorMapper.toDto(actor)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<ActorDto> createActor(@RequestBody CreateActorRequest request) {
        Actor actor = actorService.createActor(request);
        return ResponseEntity.ok(ActorMapper.toDto(actor));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteActor(@PathVariable Long id) {
        actorService.deleteActor(id);
        return ResponseEntity.ok().build();
    }
}