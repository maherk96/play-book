package io.games.play_book.game_event.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import io.games.play_book.game.domain.Game;
import io.games.play_book.game_event.domain.GameEvent;
import io.games.play_book.season_player.domain.SeasonPlayer;

public interface GameEventRepository extends JpaRepository<GameEvent, Long> {

  GameEvent findFirstByGame(Game game);

  GameEvent findFirstBySeasonPlayer(SeasonPlayer seasonPlayer);
}
