package io.games.play_book.season.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.games.play_book.game.domain.Game;
import io.games.play_book.game.repos.GameRepository;
import io.games.play_book.season.domain.Season;
import io.games.play_book.season.model.SeasonDTO;
import io.games.play_book.season.repos.SeasonRepository;
import io.games.play_book.season_player.domain.SeasonPlayer;
import io.games.play_book.season_player.repos.SeasonPlayerRepository;
import io.games.play_book.util.NotFoundException;
import io.games.play_book.util.ReferencedWarning;

@Service
public class SeasonService {

  private static final Logger logger = LoggerFactory.getLogger(SeasonService.class);

  private final SeasonRepository seasonRepository;
  private final GameRepository gameRepository;
  private final SeasonPlayerRepository seasonPlayerRepository;

  public SeasonService(
      final SeasonRepository seasonRepository,
      final GameRepository gameRepository,
      final SeasonPlayerRepository seasonPlayerRepository) {
    this.seasonRepository = seasonRepository;
    this.gameRepository = gameRepository;
    this.seasonPlayerRepository = seasonPlayerRepository;
  }

  /**
   * Retrieves all seasons sorted by start date ascending.
   *
   * @return List of SeasonDTO
   */
  @Transactional(readOnly = true)
  public List<SeasonDTO> findAll() {
    logger.info("Fetching all seasons sorted by start date.");
    final List<Season> seasons = seasonRepository.findAll(Sort.by(Sort.Direction.ASC, "startDate"));
    return seasons.stream()
        .map(season -> mapToDTO(season, new SeasonDTO()))
        .collect(Collectors.toList());
  }

  /**
   * Retrieves a season by its ID.
   *
   * @param seasonId The ID of the season.
   * @return SeasonDTO
   * @throws NotFoundException if the season is not found.
   */
  @Transactional(readOnly = true)
  public SeasonDTO get(final long seasonId) {
    logger.info("Fetching season with ID: {}", seasonId);
    return seasonRepository
        .findById(seasonId)
        .map(season -> mapToDTO(season, new SeasonDTO()))
        .orElseThrow(() -> new NotFoundException("Season not found with ID: " + seasonId));
  }

  /**
   * Creates a new season.
   *
   * @param seasonDTO The SeasonDTO containing season details.
   * @return The ID of the created season.
   * @throws IllegalArgumentException if the season name already exists or dates are invalid.
   */
  @Transactional
  public long create(final SeasonDTO seasonDTO) {
    logger.info("Creating new season: {}", seasonDTO.getName());

    if (seasonRepository.findByName(seasonDTO.getName()).isPresent()) {
      throw new IllegalArgumentException(
          "Season with name '" + seasonDTO.getName() + "' already exists.");
    }

    if (seasonDTO.getEndDate() != null
        && !seasonDTO.getEndDate().isAfter(seasonDTO.getStartDate())) {
      throw new IllegalArgumentException("End date must be after start date.");
    }

    // Map DTO to entity
    final Season season = mapToEntity(seasonDTO, new Season());

    // Save season
    Season savedSeason = seasonRepository.save(season);
    logger.info("Season created with ID: {}", savedSeason.getSeasonId());

    return savedSeason.getSeasonId();
  }

  /**
   * Updates an existing season.
   *
   * @param seasonId The ID of the season to update.
   * @param seasonDTO The SeasonDTO containing updated details.
   * @throws NotFoundException if the season is not found.
   * @throws IllegalArgumentException if the season name already exists or dates are invalid.
   */
  @Transactional
  public void update(final long seasonId, final SeasonDTO seasonDTO) {
    logger.info("Updating season with ID: {}", seasonId);

    // Fetch existing season
    final Season season =
        seasonRepository
            .findById(seasonId)
            .orElseThrow(() -> new NotFoundException("Season not found with ID: " + seasonId));

    // Validate uniqueness of season name if it's being changed
    if (!season.getName().equals(seasonDTO.getName())
        && seasonRepository.findByName(seasonDTO.getName()).isPresent()) {
      throw new IllegalArgumentException(
          "Season with name '" + seasonDTO.getName() + "' already exists.");
    }

    // Validate date range
    if (seasonDTO.getEndDate() != null
        && !seasonDTO.getEndDate().isAfter(seasonDTO.getStartDate())) {
      throw new IllegalArgumentException("End date must be after start date.");
    }

    // Map DTO to entity
    mapToEntity(seasonDTO, season);

    // Save updated season
    seasonRepository.save(season);
    logger.info("Season with ID: {} has been updated.", seasonId);
  }

  /**
   * Deletes a season by its ID.
   *
   * @param seasonId The ID of the season to delete.
   * @throws NotFoundException if the season is not found.
   * @throws IllegalStateException if the season has existing references.
   */
  @Transactional
  public void delete(final long seasonId) {
    logger.info("Attempting to delete season with ID: {}", seasonId);

    // Check if season exists
    final Season season =
        seasonRepository
            .findById(seasonId)
            .orElseThrow(() -> new NotFoundException("Season not found with ID: " + seasonId));

    // Check for references
    ReferencedWarning warning = getReferencedWarning(seasonId);
    if (warning != null) {
      logger.warn("Season with ID: {} has existing references: {}", seasonId, warning.getKey());
      throw new IllegalStateException(
          "Cannot delete season as it is referenced by other entities.");
    }

    // Proceed with deletion
    seasonRepository.delete(season);
    logger.info("Season with ID: {} has been deleted.", seasonId);
  }

  /**
   * Maps a Season entity to a SeasonDTO.
   *
   * @param season The Season entity.
   * @param seasonDTO The SeasonDTO to populate.
   * @return The populated SeasonDTO.
   */
  private SeasonDTO mapToDTO(final Season season, final SeasonDTO seasonDTO) {
    seasonDTO.setSeasonId(season.getSeasonId());
    seasonDTO.setInitialChipCount(season.getInitialChipCount());
    seasonDTO.setCreatedAt(season.getCreatedAt());
    seasonDTO.setEndDate(season.getEndDate());
    seasonDTO.setName(season.getName());
    seasonDTO.setStartDate(season.getStartDate());
    seasonDTO.setMinBuyIn(season.getMinBuyIn());
    return seasonDTO;
  }

  /**
   * Maps a SeasonDTO to a Season entity.
   *
   * @param seasonDTO The SeasonDTO containing season details.
   * @param season The Season entity to populate.
   * @return The populated Season entity.
   */
  private Season mapToEntity(final SeasonDTO seasonDTO, final Season season) {
    season.setCreatedAt(seasonDTO.getCreatedAt());
    season.setEndDate(seasonDTO.getEndDate());
    season.setName(seasonDTO.getName());
    season.setStartDate(seasonDTO.getStartDate());
    season.setMinBuyIn(seasonDTO.getMinBuyIn());
    season.setInitialChipCount(seasonDTO.getInitialChipCount());
    return season;
  }

  /**
   * Checks if the season is referenced by any games or season players.
   *
   * @param seasonId The ID of the season.
   * @return ReferencedWarning if references exist, else null.
   */
  @Transactional(readOnly = true)
  public ReferencedWarning getReferencedWarning(final long seasonId) {
    logger.info("Checking references for season with ID: {}", seasonId);
    final Season season =
        seasonRepository
            .findById(seasonId)
            .orElseThrow(() -> new NotFoundException("Season not found with ID: " + seasonId));

    final Game seasonGame = gameRepository.findFirstBySeason(season);
    if (seasonGame != null) {
      ReferencedWarning referencedWarning = new ReferencedWarning();
      referencedWarning.setKey("season.game.season.referenced");
      referencedWarning.addParam(seasonGame.getGameId());
      logger.debug("Season is referenced by game with ID: {}", seasonGame.getGameId());
      return referencedWarning;
    }

    final SeasonPlayer seasonSeasonPlayer = seasonPlayerRepository.findFirstBySeason(season);
    if (seasonSeasonPlayer != null) {
      ReferencedWarning referencedWarning = new ReferencedWarning();
      referencedWarning.setKey("season.seasonPlayer.season.referenced");
      referencedWarning.addParam(seasonSeasonPlayer.getSeasonPlayerId());
      logger.debug(
          "Season is referenced by season player with ID: {}",
          seasonSeasonPlayer.getSeasonPlayerId());
      return referencedWarning;
    }

    logger.info("No references found for season with ID: {}", seasonId);
    return null;
  }
}
