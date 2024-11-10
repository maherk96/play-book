package io.games.play_book.player_participation.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.games.play_book.game.domain.Game;
import io.games.play_book.game.repos.GameRepository;
import io.games.play_book.player_participation.domain.PlayerParticipation;
import io.games.play_book.player_participation.model.PlayerParticipationDTO;
import io.games.play_book.player_participation.repos.PlayerParticipationRepository;
import io.games.play_book.season_player.domain.SeasonPlayer;
import io.games.play_book.season_player.repos.SeasonPlayerRepository;
import io.games.play_book.util.NotFoundException;

@Service
public class PlayerParticipationService {

  private final PlayerParticipationRepository playerParticipationRepository;
  private final GameRepository gameRepository;
  private final SeasonPlayerRepository seasonPlayerRepository;

  public PlayerParticipationService(
      final PlayerParticipationRepository playerParticipationRepository,
      final GameRepository gameRepository,
      final SeasonPlayerRepository seasonPlayerRepository) {
    this.playerParticipationRepository = playerParticipationRepository;
    this.gameRepository = gameRepository;
    this.seasonPlayerRepository = seasonPlayerRepository;
  }

  public List<PlayerParticipationDTO> findAll() {
    final List<PlayerParticipation> playerParticipations =
        playerParticipationRepository.findAll(Sort.by("participationId"));
    return playerParticipations.stream()
        .map(playerParticipation -> mapToDTO(playerParticipation, new PlayerParticipationDTO()))
        .toList();
  }

  public PlayerParticipationDTO get(final Integer participationId) {
    return playerParticipationRepository
        .findById(participationId)
        .map(playerParticipation -> mapToDTO(playerParticipation, new PlayerParticipationDTO()))
        .orElseThrow(NotFoundException::new);
  }

  public long create(final PlayerParticipationDTO playerParticipationDTO) {
    final PlayerParticipation playerParticipation = new PlayerParticipation();
    mapToEntity(playerParticipationDTO, playerParticipation);
    return playerParticipationRepository.save(playerParticipation).getParticipationId();
  }

  public void update(
      final Integer participationId, final PlayerParticipationDTO playerParticipationDTO) {
    final PlayerParticipation playerParticipation =
        playerParticipationRepository.findById(participationId).orElseThrow(NotFoundException::new);
    mapToEntity(playerParticipationDTO, playerParticipation);
    playerParticipationRepository.save(playerParticipation);
  }

  public void delete(final Integer participationId) {
    playerParticipationRepository.deleteById(participationId);
  }

  private PlayerParticipationDTO mapToDTO(
      final PlayerParticipation playerParticipation,
      final PlayerParticipationDTO playerParticipationDTO) {
    playerParticipationDTO.setParticipationId(playerParticipation.getParticipationId());
    playerParticipationDTO.setParticipated(playerParticipation.getParticipated());
    playerParticipationDTO.setParticipationTime(playerParticipation.getParticipationTime());
    playerParticipationDTO.setGame(
        playerParticipation.getGame() == null ? null : playerParticipation.getGame().getGameId());
    playerParticipationDTO.setSeasonPlayer(
        playerParticipation.getSeasonPlayer() == null
            ? null
            : playerParticipation.getSeasonPlayer().getSeasonPlayerId());
    return playerParticipationDTO;
  }

  private PlayerParticipation mapToEntity(
      final PlayerParticipationDTO playerParticipationDTO,
      final PlayerParticipation playerParticipation) {
    playerParticipation.setParticipated(playerParticipationDTO.getParticipated());
    playerParticipation.setParticipationTime(playerParticipationDTO.getParticipationTime());
    final Game game =
        playerParticipationDTO.getGame() == null
            ? null
            : gameRepository
                .findById(playerParticipationDTO.getGame())
                .orElseThrow(() -> new NotFoundException("game not found"));
    playerParticipation.setGame(game);
    final SeasonPlayer seasonPlayer =
        playerParticipationDTO.getSeasonPlayer() == null
            ? null
            : seasonPlayerRepository
                .findById(playerParticipationDTO.getSeasonPlayer())
                .orElseThrow(() -> new NotFoundException("seasonPlayer not found"));
    playerParticipation.setSeasonPlayer(seasonPlayer);
    return playerParticipation;
  }
}
