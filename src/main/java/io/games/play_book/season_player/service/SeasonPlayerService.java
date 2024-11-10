package io.games.play_book.season_player.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.games.play_book.game_buy_in.domain.GameBuyIn;
import io.games.play_book.game_buy_in.repos.GameBuyInRepository;
import io.games.play_book.game_event.domain.GameEvent;
import io.games.play_book.game_event.repos.GameEventRepository;
import io.games.play_book.game_result.domain.GameResult;
import io.games.play_book.game_result.repos.GameResultRepository;
import io.games.play_book.player.domain.Player;
import io.games.play_book.player.repos.PlayerRepository;
import io.games.play_book.player_participation.domain.PlayerParticipation;
import io.games.play_book.player_participation.repos.PlayerParticipationRepository;
import io.games.play_book.season.domain.Season;
import io.games.play_book.season.repos.SeasonRepository;
import io.games.play_book.season_player.domain.SeasonPlayer;
import io.games.play_book.season_player.model.SeasonPlayerDTO;
import io.games.play_book.season_player.repos.SeasonPlayerRepository;
import io.games.play_book.util.NotFoundException;
import io.games.play_book.util.ReferencedWarning;

@Service
public class SeasonPlayerService {

  private final SeasonPlayerRepository seasonPlayerRepository;
  private final PlayerRepository playerRepository;
  private final SeasonRepository seasonRepository;
  private final GameBuyInRepository gameBuyInRepository;
  private final GameEventRepository gameEventRepository;
  private final GameResultRepository gameResultRepository;
  private final PlayerParticipationRepository playerParticipationRepository;

  public SeasonPlayerService(
      final SeasonPlayerRepository seasonPlayerRepository,
      final PlayerRepository playerRepository,
      final SeasonRepository seasonRepository,
      final GameBuyInRepository gameBuyInRepository,
      final GameEventRepository gameEventRepository,
      final GameResultRepository gameResultRepository,
      final PlayerParticipationRepository playerParticipationRepository) {
    this.seasonPlayerRepository = seasonPlayerRepository;
    this.playerRepository = playerRepository;
    this.seasonRepository = seasonRepository;
    this.gameBuyInRepository = gameBuyInRepository;
    this.gameEventRepository = gameEventRepository;
    this.gameResultRepository = gameResultRepository;
    this.playerParticipationRepository = playerParticipationRepository;
  }

  public List<SeasonPlayerDTO> findAll() {
    final List<SeasonPlayer> seasonPlayers =
        seasonPlayerRepository.findAll(Sort.by("seasonPlayerId"));
    return seasonPlayers.stream()
        .map(seasonPlayer -> mapToDTO(seasonPlayer, new SeasonPlayerDTO()))
        .toList();
  }

  public SeasonPlayerDTO get(final long seasonPlayerId) {
    return seasonPlayerRepository
        .findById(seasonPlayerId)
        .map(seasonPlayer -> mapToDTO(seasonPlayer, new SeasonPlayerDTO()))
        .orElseThrow(NotFoundException::new);
  }

  public long create(final SeasonPlayerDTO seasonPlayerDTO) {
    final SeasonPlayer seasonPlayer = new SeasonPlayer();
    mapToEntity(seasonPlayerDTO, seasonPlayer);
    return seasonPlayerRepository.save(seasonPlayer).getSeasonPlayerId();
  }

  public void update(final long seasonPlayerId, final SeasonPlayerDTO seasonPlayerDTO) {
    final SeasonPlayer seasonPlayer =
        seasonPlayerRepository.findById(seasonPlayerId).orElseThrow(NotFoundException::new);
    mapToEntity(seasonPlayerDTO, seasonPlayer);
    seasonPlayerRepository.save(seasonPlayer);
  }

  public void delete(final long seasonPlayerId) {
    seasonPlayerRepository.deleteById(seasonPlayerId);
  }

  private SeasonPlayerDTO mapToDTO(
      final SeasonPlayer seasonPlayer, final SeasonPlayerDTO seasonPlayerDTO) {
    seasonPlayerDTO.setSeasonPlayerId(seasonPlayer.getSeasonPlayerId());
    seasonPlayerDTO.setInitialChipCount(seasonPlayer.getInitialChipCount());
    seasonPlayerDTO.setRemainingChipCount(seasonPlayer.getRemainingChipCount());
    seasonPlayerDTO.setPlayer(
        seasonPlayer.getPlayer() == null ? null : seasonPlayer.getPlayer().getPlayerId());
    seasonPlayerDTO.setSeason(
        seasonPlayer.getSeason() == null ? null : seasonPlayer.getSeason().getSeasonId());
    return seasonPlayerDTO;
  }

  private SeasonPlayer mapToEntity(
      final SeasonPlayerDTO seasonPlayerDTO, final SeasonPlayer seasonPlayer) {
    seasonPlayer.setInitialChipCount(seasonPlayerDTO.getInitialChipCount());
    seasonPlayer.setRemainingChipCount(seasonPlayerDTO.getRemainingChipCount());
    final Player player =
        seasonPlayerDTO.getPlayer() == null
            ? null
            : playerRepository
                .findById(seasonPlayerDTO.getPlayer())
                .orElseThrow(() -> new NotFoundException("player not found"));
    seasonPlayer.setPlayer(player);
    final Season season =
        seasonPlayerDTO.getSeason() == null
            ? null
            : seasonRepository
                .findById(seasonPlayerDTO.getSeason())
                .orElseThrow(() -> new NotFoundException("season not found"));
    seasonPlayer.setSeason(season);
    return seasonPlayer;
  }

  public ReferencedWarning getReferencedWarning(final long seasonPlayerId) {
    final ReferencedWarning referencedWarning = new ReferencedWarning();
    final SeasonPlayer seasonPlayer =
        seasonPlayerRepository.findById(seasonPlayerId).orElseThrow(NotFoundException::new);
    final GameBuyIn seasonPlayerGameBuyIn =
        gameBuyInRepository.findFirstBySeasonPlayer(seasonPlayer);
    if (seasonPlayerGameBuyIn != null) {
      referencedWarning.setKey("seasonPlayer.gameBuyIn.seasonPlayer.referenced");
      referencedWarning.addParam(seasonPlayerGameBuyIn.getGameBuyInId());
      return referencedWarning;
    }
    final GameEvent seasonPlayerGameEvent =
        gameEventRepository.findFirstBySeasonPlayer(seasonPlayer);
    if (seasonPlayerGameEvent != null) {
      referencedWarning.setKey("seasonPlayer.gameEvent.seasonPlayer.referenced");
      referencedWarning.addParam(seasonPlayerGameEvent.getEventId());
      return referencedWarning;
    }
    final GameResult seasonPlayerGameResult =
        gameResultRepository.findFirstBySeasonPlayer(seasonPlayer);
    if (seasonPlayerGameResult != null) {
      referencedWarning.setKey("seasonPlayer.gameResult.seasonPlayer.referenced");
      referencedWarning.addParam(seasonPlayerGameResult.getGameResultId());
      return referencedWarning;
    }
    final PlayerParticipation seasonPlayerPlayerParticipation =
        playerParticipationRepository.findFirstBySeasonPlayer(seasonPlayer);
    if (seasonPlayerPlayerParticipation != null) {
      referencedWarning.setKey("seasonPlayer.playerParticipation.seasonPlayer.referenced");
      referencedWarning.addParam(seasonPlayerPlayerParticipation.getParticipationId());
      return referencedWarning;
    }
    return null;
  }
}
