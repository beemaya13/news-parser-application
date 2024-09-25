package com.nilga.newsparserapp.controller;

import com.nilga.newsparserapp.service.NewsService;
import com.nilga.shared.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * REST Controller for managing News resources.
 */
@RestController
@RequestMapping("/api/news")
public class NewsController {

	@Autowired
	private NewsService newsService;

	/**
	 * Endpoint to get all news articles.
	 *
	 * @return List of news articles.
	 */
	@GetMapping
	public List<News> getAllNews() {
		return newsService.getAllNews();
	}

	/**
	 * Endpoint to save a news article.
	 *
	 * @param news News article to save.
	 * @return The saved news article.
	 */
	@PostMapping
	public News saveNews(@RequestBody News news) {
		return newsService.saveNews(news);
	}

	/**
	 * Endpoint to get a news article by ID.
	 *
	 * @param id The ID of the news article.
	 * @return The news article with the given ID.
	 */
	@GetMapping("/{id}")
	public News getNewsById(@PathVariable Long id) {
		return newsService.getNewsById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "News not found"));
	}

	/**
	 * Retrieves a list of news articles published within a specified time period.
	 * The time period can be "morning", "day", or "evening". The service will
	 * either return today's news or fallback to yesterday's if no news is found
	 * for today.
	 *
	 * @param period The time period to filter the news by. Valid values are
	 *               "morning", "day", and "evening".
	 * @return A list of news articles published within the specified time period.
	 * @throws ResponseStatusException if an invalid time period is provided.
	 */
	@GetMapping("/by-period")
	public List<News> getNewsByPeriod(@RequestParam String period) {
		return newsService.getNewsByPeriod(period);
	}


	/**
	 * Endpoint to delete a news article by ID.
	 *
	 * @param id The ID of the news article.
	 * @return ResponseEntity indicating the result of the operation.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteNewsById(@PathVariable Long id) {
		newsService.deleteNewsById(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Endpoint to fetch external news and save it to the DB.
	 *
	 * @return List of saved news articles.
	 */
	@GetMapping("/external")
	public List<News> fetchAndSaveExternalNews() {
		return newsService.fetchAndSaveExternalNews();
	}
}
