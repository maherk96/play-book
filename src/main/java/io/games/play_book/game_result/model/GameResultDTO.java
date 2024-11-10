package io.games.play_book.game_result.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameResultDTO {

  private Integer gameResultId;

  @NotNull
  @Digits(integer = 16, fraction = 2)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal chipCount;

  @NotNull private Integer game;

  @NotNull private Integer seasonPlayer;
}
