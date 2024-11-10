package io.games.play_book.player.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.games.play_book.player.domain.Player;
import io.games.play_book.player.model.PlayerDTO;
import io.games.play_book.player.repos.PlayerRepository;
import io.games.play_book.season_player.domain.SeasonPlayer;
import io.games.play_book.season_player.repos.SeasonPlayerRepository;
import io.games.play_book.util.NotFoundException;
import io.games.play_book.util.ReferencedWarning;

@Service
public class PlayerService {

  private final PlayerRepository playerRepository;
  private final SeasonPlayerRepository seasonPlayerRepository;

  public PlayerService(
      final PlayerRepository playerRepository,
      final SeasonPlayerRepository seasonPlayerRepository) {
    this.playerRepository = playerRepository;
    this.seasonPlayerRepository = seasonPlayerRepository;
  }

  public List<PlayerDTO> findAll() {
    final List<Player> players = playerRepository.findAll(Sort.by("playerId"));
    return players.stream().map(player -> mapToDTO(player, new PlayerDTO())).toList();
  }

  public PlayerDTO get(final Integer playerId) {
    return playerRepository
        .findById(playerId)
        .map(player -> mapToDTO(player, new PlayerDTO()))
        .orElseThrow(NotFoundException::new);
  }

  public Integer create(final PlayerDTO playerDTO) {
    final Player player = new Player();
    mapToEntity(playerDTO, player);
    return playerRepository.save(player).getPlayerId();
  }

  public void update(final Integer playerId, final PlayerDTO playerDTO) {
    final Player player = playerRepository.findById(playerId).orElseThrow(NotFoundException::new);
    mapToEntity(playerDTO, player);
    playerRepository.save(player);
  }

  public void delete(final Integer playerId) {
    playerRepository.deleteById(playerId);
  }

  private PlayerDTO mapToDTO(final Player player, final PlayerDTO playerDTO) {
    playerDTO.setPlayerId(player.getPlayerId());
    playerDTO.setCreatedAt(player.getCreatedAt());
    playerDTO.setName(player.getName());
    return playerDTO;
  }

  private Player mapToEntity(final PlayerDTO playerDTO, final Player player) {
    player.setCreatedAt(playerDTO.getCreatedAt());
    player.setName(playerDTO.getName());
    return player;
  }

  public ReferencedWarning getReferencedWarning(final Integer playerId) {
    final ReferencedWarning referencedWarning = new ReferencedWarning();
    final Player player = playerRepository.findById(playerId).orElseThrow(NotFoundException::new);
    final SeasonPlayer playerSeasonPlayer = seasonPlayerRepository.findFirstByPlayer(player);
    if (playerSeasonPlayer != null) {
      referencedWarning.setKey("player.seasonPlayer.player.referenced");
      referencedWarning.addParam(playerSeasonPlayer.getSeasonPlayerId());
      return referencedWarning;
    }
    return null;
  }
}
