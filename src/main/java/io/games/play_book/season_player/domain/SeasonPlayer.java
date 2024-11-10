package io.games.play_book.season_player.domain;

import java.math.BigDecimal;
import java.util.Set;

import io.games.play_book.game_buy_in.domain.GameBuyIn;
import io.games.play_book.game_event.domain.GameEvent;
import io.games.play_book.game_result.domain.GameResult;
import io.games.play_book.player.domain.Player;
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
public class SeasonPlayer {

  @Id
  @Column(nullable = false, updatable = false)
  @SequenceGenerator(
      name = "primary_sequence",
      sequenceName = "primary_sequence",
      allocationSize = 1,
      initialValue = 10000)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "primary_sequence")
  private Long seasonPlayerId;

  @Column(nullable = false, precision = 16, scale = 2)
  private BigDecimal initialChipCount;

  @Column(nullable = false, precision = 16, scale = 2)
  private BigDecimal remainingChipCount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "player_id", nullable = false)
  private Player player;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "season_id", nullable = false)
  private Season season;

  @OneToMany(mappedBy = "seasonPlayer")
  private Set<GameBuyIn> seasonPlayerGameBuyIns;

  @OneToMany(mappedBy = "seasonPlayer")
  private Set<GameEvent> seasonPlayerGameEvents;

  @OneToMany(mappedBy = "seasonPlayer")
  private Set<GameResult> seasonPlayerGameResults;

  @OneToMany(mappedBy = "seasonPlayer")
  private Set<PlayerParticipation> seasonPlayerPlayerParticipations;
}
