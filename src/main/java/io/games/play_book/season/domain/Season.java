package io.games.play_book.season.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;

import io.games.play_book.game.domain.Game;
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
public class Season {

  @Id
  @Column(nullable = false, updatable = false)
  @SequenceGenerator(
      name = "primary_sequence",
      sequenceName = "primary_sequence",
      allocationSize = 1,
      initialValue = 10000)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "primary_sequence")
  private Long seasonId;

  @Column(nullable = false)
  private OffsetDateTime createdAt;

  @Column private LocalDate endDate;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false)
  private LocalDate startDate;

  @Column(nullable = false, precision = 16, scale = 2)
  private BigDecimal minBuyIn;

  @OneToMany(mappedBy = "season")
  private Set<Game> seasonGames;

  @OneToMany(mappedBy = "season")
  private Set<SeasonPlayer> seasonSeasonPlayers;
}
