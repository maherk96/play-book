package io.games.play_book.season.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import io.games.play_book.season.domain.Season;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SeasonRepository extends JpaRepository<Season, Long> {


    Optional<Season> findByName(String name);

    List<Season> findAllByOrderByStartDateAsc();

    /**
     * Finds active seasons based on the current date.
     * Active if startDate <= currentDate and (endDate >= currentDate or endDate is null)
     *
     * @param currentDate The current date.
     * @return List of active seasons.
     */
    List<Season> findByStartDateLessThanEqualAndEndDateGreaterThanEqualOrEndDateIsNull(
            LocalDate currentDate1, LocalDate currentDate2);

    /**
     * Finds all seasons with their associated games fetched eagerly.
     *
     * @return List of seasons with games.
     */
    @Query("SELECT s FROM Season s LEFT JOIN FETCH s.games")
    List<Season> findAllWithGames();
}
