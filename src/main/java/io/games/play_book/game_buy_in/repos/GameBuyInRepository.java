package io.games.play_book.game_buy_in.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import io.games.play_book.game.domain.Game;
import io.games.play_book.game_buy_in.domain.GameBuyIn;
import io.games.play_book.season_player.domain.SeasonPlayer;

public interface GameBuyInRepository extends JpaRepository<GameBuyIn, Integer> {

  GameBuyIn findFirstByGame(Game game);

  GameBuyIn findFirstBySeasonPlayer(SeasonPlayer seasonPlayer);
}
