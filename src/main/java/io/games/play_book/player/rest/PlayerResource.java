package io.games.play_book.player.rest;

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

import io.games.play_book.player.model.PlayerDTO;
import io.games.play_book.player.service.PlayerService;
import io.games.play_book.util.ReferencedException;
import io.games.play_book.util.ReferencedWarning;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/players", produces = MediaType.APPLICATION_JSON_VALUE)
public class PlayerResource {

  private final PlayerService playerService;

  public PlayerResource(final PlayerService playerService) {
    this.playerService = playerService;
  }

  @GetMapping
  public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
    return ResponseEntity.ok(playerService.findAll());
  }

  @GetMapping("/{playerId}")
  public ResponseEntity<PlayerDTO> getPlayer(
      @PathVariable(name = "playerId") final Integer playerId) {
    return ResponseEntity.ok(playerService.get(playerId));
  }

  @PostMapping
  public ResponseEntity<Integer> createPlayer(@RequestBody @Valid final PlayerDTO playerDTO) {
    final Integer createdPlayerId = playerService.create(playerDTO);
    return new ResponseEntity<>(createdPlayerId, HttpStatus.CREATED);
  }

  @PutMapping("/{playerId}")
  public ResponseEntity<Integer> updatePlayer(
      @PathVariable(name = "playerId") final Integer playerId,
      @RequestBody @Valid final PlayerDTO playerDTO) {
    playerService.update(playerId, playerDTO);
    return ResponseEntity.ok(playerId);
  }

  @DeleteMapping("/{playerId}")
  public ResponseEntity<Void> deletePlayer(
      @PathVariable(name = "playerId") final Integer playerId) {
    final ReferencedWarning referencedWarning = playerService.getReferencedWarning(playerId);
    if (referencedWarning != null) {
      throw new ReferencedException(referencedWarning);
    }
    playerService.delete(playerId);
    return ResponseEntity.noContent().build();
  }
}
