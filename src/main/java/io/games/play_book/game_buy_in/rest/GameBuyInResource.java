package io.games.play_book.game_buy_in.rest;

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

import io.games.play_book.game_buy_in.model.GameBuyInDTO;
import io.games.play_book.game_buy_in.service.GameBuyInService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/gameBuyIns", produces = MediaType.APPLICATION_JSON_VALUE)
public class GameBuyInResource {

  private final GameBuyInService gameBuyInService;

  public GameBuyInResource(final GameBuyInService gameBuyInService) {
    this.gameBuyInService = gameBuyInService;
  }

  @GetMapping
  public ResponseEntity<List<GameBuyInDTO>> getAllGameBuyIns() {
    return ResponseEntity.ok(gameBuyInService.findAll());
  }

  @GetMapping("/{gameBuyInId}")
  public ResponseEntity<GameBuyInDTO> getGameBuyIn(
      @PathVariable(name = "gameBuyInId") final Integer gameBuyInId) {
    return ResponseEntity.ok(gameBuyInService.get(gameBuyInId));
  }

  @PostMapping
  public ResponseEntity<Integer> createGameBuyIn(
      @RequestBody @Valid final GameBuyInDTO gameBuyInDTO) {
    final Integer createdGameBuyInId = gameBuyInService.create(gameBuyInDTO);
    return new ResponseEntity<>(createdGameBuyInId, HttpStatus.CREATED);
  }

  @PutMapping("/{gameBuyInId}")
  public ResponseEntity<Integer> updateGameBuyIn(
      @PathVariable(name = "gameBuyInId") final Integer gameBuyInId,
      @RequestBody @Valid final GameBuyInDTO gameBuyInDTO) {
    gameBuyInService.update(gameBuyInId, gameBuyInDTO);
    return ResponseEntity.ok(gameBuyInId);
  }

  @DeleteMapping("/{gameBuyInId}")
  public ResponseEntity<Void> deleteGameBuyIn(
      @PathVariable(name = "gameBuyInId") final Integer gameBuyInId) {
    gameBuyInService.delete(gameBuyInId);
    return ResponseEntity.noContent().build();
  }
}
