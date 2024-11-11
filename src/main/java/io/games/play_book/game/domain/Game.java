package io.games.play_book.game.domain;

import java.time.OffsetDateTime;
import java.util.Set;

import io.games.play_book.game_buy_in.domain.GameBuyIn;
import io.games.play_book.game_event.domain.GameEvent;
import io.games.play_book.game_result.domain.GameResult;
import io.games.play_book.player_participation.domain.PlayerParticipation;
import io.games.play_book.season.domain.Season;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Game {

  @Id
  @Column(nullable = false, updatable = false)
  @SequenceGenerator(
      name = "primary_sequence",
      sequenceName = "primary_sequence",
      allocationSize = 1,
      initialValue = 10000)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "primary_sequence")
  private Long gameId;

  @Column private OffsetDateTime createdAt;

  @Column(nullable = false)
  private OffsetDateTime startTime;

  @Column(nullable = false)
  private OffsetDateTime endTime;

  @Column(nullable = false)
  private Long gameNumber;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "season_id", nullable = false)
  private Season season;

  @OneToMany(mappedBy = "game")
  private Set<GameBuyIn> gameGameBuyIns;

  @OneToMany(mappedBy = "game")
  private Set<GameEvent> gameGameEvents;

  @OneToMany(mappedBy = "game")
  private Set<GameResult> gameGameResults;

  @OneToMany(mappedBy = "game")
  private Set<PlayerParticipation> gamePlayerParticipations;
}
