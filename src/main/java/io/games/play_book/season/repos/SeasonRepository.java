package io.games.play_book.season.repos;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.games.play_book.season.domain.Season;

public interface SeasonRepository extends JpaRepository<Season, Long> {

  Optional<Season> findByName(String name);

  List<Season> findAllByOrderByStartDateAsc();

  List<Season> findByStartDateLessThanEqualAndEndDateGreaterThanEqualOrEndDateIsNull(
      LocalDate currentDate1, LocalDate currentDate2);
}
