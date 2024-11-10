package io.games.play_book.game.model;

import java.time.OffsetDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameDTO {

  private Long gameId;

  private OffsetDateTime createdAt;

  @NotNull private OffsetDateTime startTime;

  @NotNull private OffsetDateTime endTime;

  @NotNull private Long gameNumber;

  @NotNull private Long season;
}
