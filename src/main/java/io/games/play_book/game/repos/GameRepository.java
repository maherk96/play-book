package io.games.play_book.game.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import io.games.play_book.game.domain.Game;
import io.games.play_book.season.domain.Season;

public interface GameRepository extends JpaRepository<Game, Long> {

  Game findFirstBySeason(Season season);
}
