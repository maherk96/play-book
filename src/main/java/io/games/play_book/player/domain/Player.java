package io.games.play_book.player.domain;

import java.time.OffsetDateTime;
import java.util.Set;

import io.games.play_book.season_player.domain.SeasonPlayer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Player {

  @Id
  @Column(nullable = false, updatable = false)
  @SequenceGenerator(
      name = "primary_sequence",
      sequenceName = "primary_sequence",
      allocationSize = 1,
      initialValue = 10000)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "primary_sequence")
  private Long playerId;

  @Column(nullable = false)
  private OffsetDateTime createdAt;

  @Column(nullable = false, length = 100)
  private String name;

  @OneToMany(mappedBy = "player")
  private Set<SeasonPlayer> playerSeasonPlayers;
}
