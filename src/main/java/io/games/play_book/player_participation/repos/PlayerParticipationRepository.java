package io.games.play_book.player_participation.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import io.games.play_book.game.domain.Game;
import io.games.play_book.player_participation.domain.PlayerParticipation;
import io.games.play_book.season_player.domain.SeasonPlayer;

public interface PlayerParticipationRepository extends JpaRepository<PlayerParticipation, Long> {

  PlayerParticipation findFirstByGame(Game game);

  PlayerParticipation findFirstBySeasonPlayer(SeasonPlayer seasonPlayer);
}
