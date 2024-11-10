package io.games.play_book.season_player.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import io.games.play_book.player.domain.Player;
import io.games.play_book.season.domain.Season;
import io.games.play_book.season_player.domain.SeasonPlayer;

public interface SeasonPlayerRepository extends JpaRepository<SeasonPlayer, Integer> {

  SeasonPlayer findFirstByPlayer(Player player);

  SeasonPlayer findFirstBySeason(Season season);
}
