package io.games.play_book.game_event.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.games.play_book.game.domain.Game;
import io.games.play_book.game.repos.GameRepository;
import io.games.play_book.game_event.domain.GameEvent;
import io.games.play_book.game_event.model.GameEventDTO;
import io.games.play_book.game_event.repos.GameEventRepository;
import io.games.play_book.season_player.domain.SeasonPlayer;
import io.games.play_book.season_player.repos.SeasonPlayerRepository;
import io.games.play_book.util.NotFoundException;

@Service
public class GameEventService {

  private final GameEventRepository gameEventRepository;
  private final GameRepository gameRepository;
  private final SeasonPlayerRepository seasonPlayerRepository;

  public GameEventService(
      final GameEventRepository gameEventRepository,
      final GameRepository gameRepository,
      final SeasonPlayerRepository seasonPlayerRepository) {
    this.gameEventRepository = gameEventRepository;
    this.gameRepository = gameRepository;
    this.seasonPlayerRepository = seasonPlayerRepository;
  }

  public List<GameEventDTO> findAll() {
    final List<GameEvent> gameEvents = gameEventRepository.findAll(Sort.by("eventId"));
    return gameEvents.stream().map(gameEvent -> mapToDTO(gameEvent, new GameEventDTO())).toList();
  }

  public GameEventDTO get(final Integer eventId) {
    return gameEventRepository
        .findById(eventId)
        .map(gameEvent -> mapToDTO(gameEvent, new GameEventDTO()))
        .orElseThrow(NotFoundException::new);
  }

  public long create(final GameEventDTO gameEventDTO) {
    final GameEvent gameEvent = new GameEvent();
    mapToEntity(gameEventDTO, gameEvent);
    return gameEventRepository.save(gameEvent).getEventId();
  }

  public void update(final Integer eventId, final GameEventDTO gameEventDTO) {
    final GameEvent gameEvent =
        gameEventRepository.findById(eventId).orElseThrow(NotFoundException::new);
    mapToEntity(gameEventDTO, gameEvent);
    gameEventRepository.save(gameEvent);
  }

  public void delete(final Integer eventId) {
    gameEventRepository.deleteById(eventId);
  }

  private GameEventDTO mapToDTO(final GameEvent gameEvent, final GameEventDTO gameEventDTO) {
    gameEventDTO.setEventId(gameEvent.getEventId());
    gameEventDTO.setEventType(gameEvent.getEventType());
    gameEventDTO.setEventTime(gameEvent.getEventTime());
    gameEventDTO.setGame(gameEvent.getGame() == null ? null : gameEvent.getGame().getGameId());
    gameEventDTO.setSeasonPlayer(
        gameEvent.getSeasonPlayer() == null
            ? null
            : gameEvent.getSeasonPlayer().getSeasonPlayerId());
    return gameEventDTO;
  }

  private GameEvent mapToEntity(final GameEventDTO gameEventDTO, final GameEvent gameEvent) {
    gameEvent.setEventType(gameEventDTO.getEventType());
    gameEvent.setEventTime(gameEventDTO.getEventTime());
    final Game game =
        gameEventDTO.getGame() == null
            ? null
            : gameRepository
                .findById(gameEventDTO.getGame())
                .orElseThrow(() -> new NotFoundException("game not found"));
    gameEvent.setGame(game);
    final SeasonPlayer seasonPlayer =
        gameEventDTO.getSeasonPlayer() == null
            ? null
            : seasonPlayerRepository
                .findById(gameEventDTO.getSeasonPlayer())
                .orElseThrow(() -> new NotFoundException("seasonPlayer not found"));
    gameEvent.setSeasonPlayer(seasonPlayer);
    return gameEvent;
  }
}
