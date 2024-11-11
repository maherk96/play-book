package io.games.play_book.game_buy_in.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.games.play_book.game.domain.Game;
import io.games.play_book.game.repos.GameRepository;
import io.games.play_book.game_buy_in.domain.GameBuyIn;
import io.games.play_book.game_buy_in.model.GameBuyInDTO;
import io.games.play_book.game_buy_in.repos.GameBuyInRepository;
import io.games.play_book.season_player.domain.SeasonPlayer;
import io.games.play_book.season_player.repos.SeasonPlayerRepository;
import io.games.play_book.util.NotFoundException;

@Service
public class GameBuyInService {

  private final GameBuyInRepository gameBuyInRepository;
  private final GameRepository gameRepository;
  private final SeasonPlayerRepository seasonPlayerRepository;

  public GameBuyInService(
      final GameBuyInRepository gameBuyInRepository,
      final GameRepository gameRepository,
      final SeasonPlayerRepository seasonPlayerRepository) {
    this.gameBuyInRepository = gameBuyInRepository;
    this.gameRepository = gameRepository;
    this.seasonPlayerRepository = seasonPlayerRepository;
  }

  public List<GameBuyInDTO> findAll() {
    final List<GameBuyIn> gameBuyIns = gameBuyInRepository.findAll(Sort.by("gameBuyInId"));
    return gameBuyIns.stream().map(gameBuyIn -> mapToDTO(gameBuyIn, new GameBuyInDTO())).toList();
  }

  public GameBuyInDTO get(final long gameBuyInId) {
    return gameBuyInRepository
        .findById(gameBuyInId)
        .map(gameBuyIn -> mapToDTO(gameBuyIn, new GameBuyInDTO()))
        .orElseThrow(NotFoundException::new);
  }

  public long create(final GameBuyInDTO gameBuyInDTO) {
    final GameBuyIn gameBuyIn = new GameBuyIn();
    mapToEntity(gameBuyInDTO, gameBuyIn);
    return gameBuyInRepository.save(gameBuyIn).getGameBuyInId();
  }

  public void update(final long gameBuyInId, final GameBuyInDTO gameBuyInDTO) {
    final GameBuyIn gameBuyIn =
        gameBuyInRepository.findById(gameBuyInId).orElseThrow(NotFoundException::new);
    mapToEntity(gameBuyInDTO, gameBuyIn);
    gameBuyInRepository.save(gameBuyIn);
  }

  public void delete(final long gameBuyInId) {
    gameBuyInRepository.deleteById(gameBuyInId);
  }

  private GameBuyInDTO mapToDTO(final GameBuyIn gameBuyIn, final GameBuyInDTO gameBuyInDTO) {
    gameBuyInDTO.setGameBuyInId(gameBuyIn.getGameBuyInId());
    gameBuyInDTO.setBuyInAmount(gameBuyIn.getBuyInAmount());
    gameBuyInDTO.setGame(gameBuyIn.getGame() == null ? null : gameBuyIn.getGame().getGameId());
    gameBuyInDTO.setSeasonPlayer(
        gameBuyIn.getSeasonPlayer() == null
            ? null
            : gameBuyIn.getSeasonPlayer().getSeasonPlayerId());
    return gameBuyInDTO;
  }

  private GameBuyIn mapToEntity(final GameBuyInDTO gameBuyInDTO, final GameBuyIn gameBuyIn) {
    gameBuyIn.setBuyInAmount(gameBuyInDTO.getBuyInAmount());
    final Game game =
        gameBuyInDTO.getGame() == null
            ? null
            : gameRepository
                .findById(gameBuyInDTO.getGame())
                .orElseThrow(() -> new NotFoundException("game not found"));
    gameBuyIn.setGame(game);
    final SeasonPlayer seasonPlayer =
        gameBuyInDTO.getSeasonPlayer() == null
            ? null
            : seasonPlayerRepository
                .findById(gameBuyInDTO.getSeasonPlayer())
                .orElseThrow(() -> new NotFoundException("seasonPlayer not found"));
    gameBuyIn.setSeasonPlayer(seasonPlayer);
    return gameBuyIn;
  }
}
