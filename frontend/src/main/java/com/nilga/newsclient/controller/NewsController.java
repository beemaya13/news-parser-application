package com.nilga.newsclient.controller;

import com.nilga.newsclient.service.NewsService;
import com.nilga.shared.model.News;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for managing the user interface in the JavaFX application.
 * This class interacts with the {@link NewsService} to fetch and display news
 * articles based on the selected time period (morning, day, or evening).
 */
public class NewsController {

	private final NewsService newsService = new NewsService();
	private List<News> newsList = new ArrayList<>();
	private int currentIndex = 0;

	@FXML
	private Label headlineLabel;

	@FXML
	private Label descriptionLabel;

	@FXML
	private Label publicationTimeLabel;

	/**
	 * Initializes the controller and loads the default news (morning news) when
	 * the JavaFX window is opened.
	 */
	@FXML
	public void initialize() {
		// Load the default news (e.g., morning news)
		loadNews("morning");
	}

	/**
	 * Loads news articles for a specific time period (morning, day, or evening).
	 *
	 * @param period The time period to load news for.
	 */
	public void loadNews(String period) {
		try {
			newsList = newsService.getNewsByPeriod(period);
			if (newsList != null && !newsList.isEmpty()) {
				displayNews(0);  // Display the first news item
			} else {
				System.out.println("No news found for the period: " + period);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Displays a specific news article based on the provided index.
	 *
	 * @param index The index of the news article to display.
	 */
	public void displayNews(int index) {
		if (index >= 0 && index < newsList.size()) {
			News news = newsList.get(index);
			headlineLabel.setText(news.getHeadline());
			headlineLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #800080; -fx-font-weight: bold;");
			descriptionLabel.setText(news.getDescription());
			publicationTimeLabel.setText(news.getPublicationTime().toString());
			currentIndex = index;
		}
	}

	/**
	 * Navigates between news articles in the list based on the direction parameter.
	 *
	 * @param direction The direction to navigate (1 for next, -1 for previous).
	 */
	@FXML
	public void navigateNews(int direction) {
		int newIndex = currentIndex + direction;
		if (newIndex >= 0 && newIndex < newsList.size()) {
			displayNews(newIndex);
		}
	}

	/**
	 * Navigates to the next news article.
	 */
	@FXML
	public void nextNews() {
		navigateNews(1);
	}

	/**
	 * Navigates to the previous news article.
	 */
	@FXML
	public void previousNews() {
		navigateNews(-1);
	}
}
