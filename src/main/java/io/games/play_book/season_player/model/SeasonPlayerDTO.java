package io.games.play_book.season_player.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeasonPlayerDTO {

  private Long seasonPlayerId;

  @NotNull
  @Digits(integer = 16, fraction = 2)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal initialChipCount;

  @NotNull
  @Digits(integer = 16, fraction = 2)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal remainingChipCount;

  @NotNull private Long player;

  @NotNull private Long season;
}
