package io.games.play_book.game_result.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import io.games.play_book.game.domain.Game;
import io.games.play_book.game_result.domain.GameResult;
import io.games.play_book.season_player.domain.SeasonPlayer;

public interface GameResultRepository extends JpaRepository<GameResult, Long> {

  GameResult findFirstByGame(Game game);

  GameResult findFirstBySeasonPlayer(SeasonPlayer seasonPlayer);
}
