package io.games.play_book.game_event.model;

import java.time.OffsetDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameEventDTO {

  private Long eventId;

  @NotNull
  @Size(max = 255)
  private String eventType;

  @NotNull private OffsetDateTime eventTime;

  @NotNull private Long game;

  @NotNull private Long seasonPlayer;
}
