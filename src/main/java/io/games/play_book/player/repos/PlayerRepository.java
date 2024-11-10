package io.games.play_book.player.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import io.games.play_book.player.domain.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {}
