package io.games.play_book.game.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.games.play_book.game.domain.Game;
import io.games.play_book.game.model.GameDTO;
import io.games.play_book.game.repos.GameRepository;
import io.games.play_book.game_buy_in.domain.GameBuyIn;
import io.games.play_book.game_buy_in.repos.GameBuyInRepository;
import io.games.play_book.game_event.domain.GameEvent;
import io.games.play_book.game_event.repos.GameEventRepository;
import io.games.play_book.game_result.domain.GameResult;
import io.games.play_book.game_result.repos.GameResultRepository;
import io.games.play_book.player_participation.domain.PlayerParticipation;
import io.games.play_book.player_participation.repos.PlayerParticipationRepository;
import io.games.play_book.season.domain.Season;
import io.games.play_book.season.repos.SeasonRepository;
import io.games.play_book.util.NotFoundException;
import io.games.play_book.util.ReferencedWarning;

@Service
public class GameService {

  private final GameRepository gameRepository;
  private final SeasonRepository seasonRepository;
  private final GameBuyInRepository gameBuyInRepository;
  private final GameEventRepository gameEventRepository;
  private final GameResultRepository gameResultRepository;
  private final PlayerParticipationRepository playerParticipationRepository;

  @Autowired
  public GameService(
      final GameRepository gameRepository,
      final SeasonRepository seasonRepository,
      final GameBuyInRepository gameBuyInRepository,
      final GameEventRepository gameEventRepository,
      final GameResultRepository gameResultRepository,
      final PlayerParticipationRepository playerParticipationRepository) {
    this.gameRepository = gameRepository;
    this.seasonRepository = seasonRepository;
    this.gameBuyInRepository = gameBuyInRepository;
    this.gameEventRepository = gameEventRepository;
    this.gameResultRepository = gameResultRepository;
    this.playerParticipationRepository = playerParticipationRepository;
  }

  public List<GameDTO> findAll() {
    final List<Game> games = gameRepository.findAll(Sort.by("gameId"));
    return games.stream().map(game -> mapToDTO(game, new GameDTO())).toList();
  }

  public GameDTO get(final long gameId) {
    return gameRepository
        .findById(gameId)
        .map(game -> mapToDTO(game, new GameDTO()))
        .orElseThrow(NotFoundException::new);
  }

  public long create(final GameDTO gameDTO) {
    final Game game = new Game();
    mapToEntity(gameDTO, game);
    return gameRepository.save(game).getGameId();
  }

  public void update(final long gameId, final GameDTO gameDTO) {
    final Game game = gameRepository.findById(gameId).orElseThrow(NotFoundException::new);
    mapToEntity(gameDTO, game);
    gameRepository.save(game);
  }

  public void delete(final long gameId) {
    gameRepository.deleteById(gameId);
  }

  private GameDTO mapToDTO(final Game game, final GameDTO gameDTO) {
    gameDTO.setGameId(game.getGameId());
    gameDTO.setCreatedAt(game.getCreatedAt());
    gameDTO.setStartTime(game.getStartTime());
    gameDTO.setEndTime(game.getEndTime());
    gameDTO.setGameNumber(game.getGameNumber());
    gameDTO.setSeason(game.getSeason() == null ? null : game.getSeason().getSeasonId());
    return gameDTO;
  }

  private Game mapToEntity(final GameDTO gameDTO, final Game game) {
    game.setCreatedAt(gameDTO.getCreatedAt());
    game.setStartTime(gameDTO.getStartTime());
    game.setEndTime(gameDTO.getEndTime());
    game.setGameNumber(gameDTO.getGameNumber());
    final Season season =
        gameDTO.getSeason() == null
            ? null
            : seasonRepository
                .findById(gameDTO.getSeason())
                .orElseThrow(() -> new NotFoundException("season not found"));
    game.setSeason(season);
    return game;
  }

  public ReferencedWarning getReferencedWarning(final long gameId) {
    final ReferencedWarning referencedWarning = new ReferencedWarning();
    final Game game = gameRepository.findById(gameId).orElseThrow(NotFoundException::new);
    final GameBuyIn gameGameBuyIn = gameBuyInRepository.findFirstByGame(game);
    if (gameGameBuyIn != null) {
      referencedWarning.setKey("game.gameBuyIn.game.referenced");
      referencedWarning.addParam(gameGameBuyIn.getGameBuyInId());
      return referencedWarning;
    }
    final GameEvent gameGameEvent = gameEventRepository.findFirstByGame(game);
    if (gameGameEvent != null) {
      referencedWarning.setKey("game.gameEvent.game.referenced");
      referencedWarning.addParam(gameGameEvent.getEventId());
      return referencedWarning;
    }
    final GameResult gameGameResult = gameResultRepository.findFirstByGame(game);
    if (gameGameResult != null) {
      referencedWarning.setKey("game.gameResult.game.referenced");
      referencedWarning.addParam(gameGameResult.getGameResultId());
      return referencedWarning;
    }
    final PlayerParticipation gamePlayerParticipation =
        playerParticipationRepository.findFirstByGame(game);
    if (gamePlayerParticipation != null) {
      referencedWarning.setKey("game.playerParticipation.game.referenced");
      referencedWarning.addParam(gamePlayerParticipation.getParticipationId());
      return referencedWarning;
    }
    return null;
  }
}
