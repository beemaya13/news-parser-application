package com.nilga.newsparserapp.repository;

import com.nilga.shared.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing News entities.
 * Extends JpaRepository to provide CRUD functionality.
 */
@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

	/**
	 * Finds all news articles between a given time period.
	 * @param start Start of the time period.
	 * @param end End of the time period.
	 * @return List of news articles within the specified time period.
	 */
	List<News> findByPublicationTimeBetween(LocalDateTime start, LocalDateTime end);

	/**
	 * Finds a news article by its headline.
	 *
	 * @param headline The headline of the news article to search for.
	 * @return An {@link Optional} containing the news article if found, or an empty {@link Optional} if no article with the given headline exists.
	 */
	Optional<News> findByHeadline(String headline);
}
