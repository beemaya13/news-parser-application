package com.nilga.newsparserapp.service;

import com.nilga.newsparserapp.dto.NewsApiResponse;
import com.nilga.newsparserapp.repository.NewsRepository;
import com.nilga.shared.model.News;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * Service class for managing News entities and performing CRUD operations.
 */
@Service
@RequiredArgsConstructor
public class NewsService {

	private final NewsRepository newsRepository;
	private final ExternalNewsService externalNewsService;

	/**
	 * Adds or updates a news article.
	 * @param news The news article to save.
	 * @return The saved or updated news article.
	 */
	public News saveNews(News news) {
		return newsRepository.save(news);
	}

	/**
	 * Retrieves all news articles.
	 * @return A list of all news articles.
	 */
	public List<News> getAllNews() {
		return newsRepository.findAll();
	}

	/**
	 * Retrieves a news article by its ID.
	 * @param id The ID of the news article.
	 * @return The news article, if found.
	 */
	public Optional<News> getNewsById(Long id) {
		return newsRepository.findById(id);
	}

	/**
	 * Deletes a news article by its ID.
	 * @param id The ID of the news article to delete.
	 */
	public void deleteNewsById(Long id) {
		newsRepository.deleteById(id);
	}

	/**
	 * Retrieves news articles published within a specified time period ("morning",
	 * "day", or "evening"). If no news is found for today's date, it fetches
	 * news from the previous day.
	 *
	 * @param period The time period to filter the news by. Valid values are "morning",
	 *               "day", and "evening".
	 * @return A list of news articles published within the specified time period.
	 * @throws IllegalArgumentException if the time period is invalid.
	 */
	public List<News> getNewsByPeriod(String period) {
		ZoneId utcZone = ZoneId.of("UTC");
		LocalDate today = LocalDate.now(utcZone);

		// Determine if we should use today's or yesterday's date range
		LocalDate queryDate = hasNoTodayNews(utcZone) ? today.minusDays(1) : today;

		// Get the time range based on the period
		LocalTimeRange timeRange = getTimeRangeForPeriod(period.toLowerCase());

		// Create start and end time for the query
		LocalDateTime start = LocalDateTime.of(queryDate, timeRange.start);
		LocalDateTime end = LocalDateTime.of(queryDate, timeRange.end);

		return newsRepository.findByPublicationTimeBetween(start, end);
	}

	// Utility method to check if today's news is available
	private boolean hasNoTodayNews(ZoneId utcZone) {
		return newsRepository.findByPublicationTimeBetween(
				LocalDateTime.of(LocalDate.now(utcZone), LocalTime.MIDNIGHT),
				LocalDateTime.now(utcZone)).isEmpty();
	}

	// Utility method to get time range for morning, day, evening
	private LocalTimeRange getTimeRangeForPeriod(String period) {
		return switch (period) {
			case "morning" -> new LocalTimeRange(LocalTime.of(1, 0), LocalTime.of(12, 0));
			case "day" -> new LocalTimeRange(LocalTime.of(12, 0), LocalTime.of(18, 0));
			case "evening" -> new LocalTimeRange(LocalTime.of(18, 0), LocalTime.of(23, 59));
			default -> throw new IllegalArgumentException("Invalid time period");
		};
	}

	// Helper class to represent a time range
	private static class LocalTimeRange {
		LocalTime start;
		LocalTime end;

		public LocalTimeRange(LocalTime start, LocalTime end) {
			this.start = start;
			this.end = end;
		}
	}

	/**
	 * Fetches top headlines from the external NewsAPI and saves it to the DB.
	 * @return List of saved News objects.
	 */
	public List<News> fetchAndSaveExternalNews() {
		NewsApiResponse response = externalNewsService.getTopHeadlines();

		if (response == null) {
			// Log and throw a more detailed exception if needed
			System.err.println("Failed to fetch news from the external API.");
			throw new RuntimeException("Failed to fetch news from the external API");
		}

		List<NewsApiResponse.Article> articles = response.getArticles();
		if (articles == null) {
			System.err.println("No articles found in the response.");
			return Collections.emptyList();  // Return an empty list if no articles found
		}

		List<News> newsToSave = articles.stream().map(article -> {
			News news = new News();
			news.setHeadline(article.getTitle());
			news.setDescription(article.getDescription());
			OffsetDateTime offsetDateTime = OffsetDateTime.parse(article.getPublishedAt(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
			news.setPublicationTime(offsetDateTime.toLocalDateTime());
			return news;
		}).collect(Collectors.toList());

		return newsRepository.saveAll(newsToSave);
	}

}

