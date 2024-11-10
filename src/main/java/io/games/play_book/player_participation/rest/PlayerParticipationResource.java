package io.games.play_book.player_participation.rest;

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

import io.games.play_book.player_participation.model.PlayerParticipationDTO;
import io.games.play_book.player_participation.service.PlayerParticipationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/playerParticipations", produces = MediaType.APPLICATION_JSON_VALUE)
public class PlayerParticipationResource {

  private final PlayerParticipationService playerParticipationService;

  public PlayerParticipationResource(final PlayerParticipationService playerParticipationService) {
    this.playerParticipationService = playerParticipationService;
  }

  @GetMapping
  public ResponseEntity<List<PlayerParticipationDTO>> getAllPlayerParticipations() {
    return ResponseEntity.ok(playerParticipationService.findAll());
  }

  @GetMapping("/{participationId}")
  public ResponseEntity<PlayerParticipationDTO> getPlayerParticipation(
      @PathVariable(name = "participationId") final Integer participationId) {
    return ResponseEntity.ok(playerParticipationService.get(participationId));
  }

  @PostMapping
  public ResponseEntity<Integer> createPlayerParticipation(
      @RequestBody @Valid final PlayerParticipationDTO playerParticipationDTO) {
    final Integer createdParticipationId =
        playerParticipationService.create(playerParticipationDTO);
    return new ResponseEntity<>(createdParticipationId, HttpStatus.CREATED);
  }

  @PutMapping("/{participationId}")
  public ResponseEntity<Integer> updatePlayerParticipation(
      @PathVariable(name = "participationId") final Integer participationId,
      @RequestBody @Valid final PlayerParticipationDTO playerParticipationDTO) {
    playerParticipationService.update(participationId, playerParticipationDTO);
    return ResponseEntity.ok(participationId);
  }

  @DeleteMapping("/{participationId}")
  public ResponseEntity<Void> deletePlayerParticipation(
      @PathVariable(name = "participationId") final Integer participationId) {
    playerParticipationService.delete(participationId);
    return ResponseEntity.noContent().build();
  }
}
