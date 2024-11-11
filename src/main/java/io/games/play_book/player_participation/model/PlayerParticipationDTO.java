package io.games.play_book.player_participation.model;

import java.time.OffsetDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerParticipationDTO {

  private Long participationId;

  @NotNull private Boolean participated;

  @NotNull private OffsetDateTime participationTime;

  @NotNull private Long game;

  @NotNull private Long seasonPlayer;
}
