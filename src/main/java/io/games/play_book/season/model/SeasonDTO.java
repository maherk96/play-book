package io.games.play_book.season.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeasonDTO {

  private Long seasonId;

  @NotNull private OffsetDateTime createdAt;

  private LocalDate endDate;

  @NotNull
  @Size(max = 100)
  private String name;

  @NotNull private LocalDate startDate;

  @NotNull
  @Digits(integer = 16, fraction = 2)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal minBuyIn;
}
