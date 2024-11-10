package io.games.play_book.game_event.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.games.play_book.game_event.model.GameEventDTO;
import io.games.play_book.game_event.service.GameEventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/gameEvents", produces = MediaType.APPLICATION_JSON_VALUE)
public class GameEventResource {

  private final GameEventService gameEventService;

  public GameEventResource(final GameEventService gameEventService) {
    this.gameEventService = gameEventService;
  }

  @GetMapping
  public ResponseEntity<List<GameEventDTO>> getAllGameEvents() {
    return ResponseEntity.ok(gameEventService.findAll());
  }

  @GetMapping("/{eventId}")
  public ResponseEntity<GameEventDTO> getGameEvent(
      @PathVariable(name = "eventId") final Integer eventId) {
    return ResponseEntity.ok(gameEventService.get(eventId));
  }

  @PostMapping
  public ResponseEntity<Integer> createGameEvent(
      @RequestBody @Valid final GameEventDTO gameEventDTO) {
    final Integer createdEventId = gameEventService.create(gameEventDTO);
    return new ResponseEntity<>(createdEventId, HttpStatus.CREATED);
  }

  @PutMapping("/{eventId}")
  public ResponseEntity<Integer> updateGameEvent(
      @PathVariable(name = "eventId") final Integer eventId,
      @RequestBody @Valid final GameEventDTO gameEventDTO) {
    gameEventService.update(eventId, gameEventDTO);
    return ResponseEntity.ok(eventId);
  }

  @DeleteMapping("/{eventId}")
  public ResponseEntity<Void> deleteGameEvent(
      @PathVariable(name = "eventId") final Integer eventId) {
    gameEventService.delete(eventId);
    return ResponseEntity.noContent().build();
  }
}
