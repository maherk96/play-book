package io.games.play_book.game_result.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.games.play_book.game.domain.Game;
import io.games.play_book.game.repos.GameRepository;
import io.games.play_book.game_result.domain.GameResult;
import io.games.play_book.game_result.model.GameResultDTO;
import io.games.play_book.game_result.repos.GameResultRepository;
import io.games.play_book.season_player.domain.SeasonPlayer;
import io.games.play_book.season_player.repos.SeasonPlayerRepository;
import io.games.play_book.util.NotFoundException;

@Service
public class GameResultService {

  private final GameResultRepository gameResultRepository;
  private final GameRepository gameRepository;
  private final SeasonPlayerRepository seasonPlayerRepository;

  public GameResultService(
      final GameResultRepository gameResultRepository,
      final GameRepository gameRepository,
      final SeasonPlayerRepository seasonPlayerRepository) {
    this.gameResultRepository = gameResultRepository;
    this.gameRepository = gameRepository;
    this.seasonPlayerRepository = seasonPlayerRepository;
  }

  public List<GameResultDTO> findAll() {
    final List<GameResult> gameResults = gameResultRepository.findAll(Sort.by("gameResultId"));
    return gameResults.stream()
        .map(gameResult -> mapToDTO(gameResult, new GameResultDTO()))
        .toList();
  }

  public GameResultDTO get(final Integer gameResultId) {
    return gameResultRepository
        .findById(gameResultId)
        .map(gameResult -> mapToDTO(gameResult, new GameResultDTO()))
        .orElseThrow(NotFoundException::new);
  }

  public Integer create(final GameResultDTO gameResultDTO) {
    final GameResult gameResult = new GameResult();
    mapToEntity(gameResultDTO, gameResult);
    return gameResultRepository.save(gameResult).getGameResultId();
  }

  public void update(final Integer gameResultId, final GameResultDTO gameResultDTO) {
    final GameResult gameResult =
        gameResultRepository.findById(gameResultId).orElseThrow(NotFoundException::new);
    mapToEntity(gameResultDTO, gameResult);
    gameResultRepository.save(gameResult);
  }

  public void delete(final Integer gameResultId) {
    gameResultRepository.deleteById(gameResultId);
  }

  private GameResultDTO mapToDTO(final GameResult gameResult, final GameResultDTO gameResultDTO) {
    gameResultDTO.setGameResultId(gameResult.getGameResultId());
    gameResultDTO.setChipCount(gameResult.getChipCount());
    gameResultDTO.setGame(gameResult.getGame() == null ? null : gameResult.getGame().getGameId());
    gameResultDTO.setSeasonPlayer(
        gameResult.getSeasonPlayer() == null
            ? null
            : gameResult.getSeasonPlayer().getSeasonPlayerId());
    return gameResultDTO;
  }

  private GameResult mapToEntity(final GameResultDTO gameResultDTO, final GameResult gameResult) {
    gameResult.setChipCount(gameResultDTO.getChipCount());
    final Game game =
        gameResultDTO.getGame() == null
            ? null
            : gameRepository
                .findById(gameResultDTO.getGame())
                .orElseThrow(() -> new NotFoundException("game not found"));
    gameResult.setGame(game);
    final SeasonPlayer seasonPlayer =
        gameResultDTO.getSeasonPlayer() == null
            ? null
            : seasonPlayerRepository
                .findById(gameResultDTO.getSeasonPlayer())
                .orElseThrow(() -> new NotFoundException("seasonPlayer not found"));
    gameResult.setSeasonPlayer(seasonPlayer);
    return gameResult;
  }
}
