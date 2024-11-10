package io.games.play_book.game_result.rest;

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

import io.games.play_book.game_result.model.GameResultDTO;
import io.games.play_book.game_result.service.GameResultService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/gameResults", produces = MediaType.APPLICATION_JSON_VALUE)
public class GameResultResource {

  private final GameResultService gameResultService;

  public GameResultResource(final GameResultService gameResultService) {
    this.gameResultService = gameResultService;
  }

  @GetMapping
  public ResponseEntity<List<GameResultDTO>> getAllGameResults() {
    return ResponseEntity.ok(gameResultService.findAll());
  }

  @GetMapping("/{gameResultId}")
  public ResponseEntity<GameResultDTO> getGameResult(
      @PathVariable(name = "gameResultId") final Integer gameResultId) {
    return ResponseEntity.ok(gameResultService.get(gameResultId));
  }

  @PostMapping
  public ResponseEntity<Integer> createGameResult(
      @RequestBody @Valid final GameResultDTO gameResultDTO) {
    final Integer createdGameResultId = gameResultService.create(gameResultDTO);
    return new ResponseEntity<>(createdGameResultId, HttpStatus.CREATED);
  }

  @PutMapping("/{gameResultId}")
  public ResponseEntity<Integer> updateGameResult(
      @PathVariable(name = "gameResultId") final Integer gameResultId,
      @RequestBody @Valid final GameResultDTO gameResultDTO) {
    gameResultService.update(gameResultId, gameResultDTO);
    return ResponseEntity.ok(gameResultId);
  }

  @DeleteMapping("/{gameResultId}")
  public ResponseEntity<Void> deleteGameResult(
      @PathVariable(name = "gameResultId") final Integer gameResultId) {
    gameResultService.delete(gameResultId);
    return ResponseEntity.noContent().build();
  }
}
