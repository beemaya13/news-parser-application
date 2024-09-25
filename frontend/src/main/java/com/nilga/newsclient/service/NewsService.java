package com.nilga.newsclient.service;

import com.nilga.shared.model.News;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.util.List;

/**
 * Service class responsible for interacting with the backend API to fetch news data.
 * It uses HTTP requests to retrieve news based on different criteria, such as a
 * specific time period or all available news.
 */
public class NewsService {
	private static final String BASE_URL = "http://localhost:8080/api/news"; // Connect to backend API
	private final ObjectMapper objectMapper = new ObjectMapper()
			.registerModule(new JavaTimeModule());

	/**
	 * Fetches news articles for a specific time period (e.g., morning, day, evening).
	 *
	 * @param period The time period for which to retrieve the news (morning, day, evening).
	 * @return A list of news articles published during the specified time period.
	 * @throws IOException If an error occurs while performing the HTTP request or processing the response.
	 */
	public List<News> getNewsByPeriod(String period) throws IOException {
		String url = BASE_URL + "/by-period?period=" + period;

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpGet request = new HttpGet(url);
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				String jsonResponse = EntityUtils.toString(response.getEntity());
				// Parse the response directly into a List<News>
				return objectMapper.readValue(jsonResponse, new TypeReference<>() {
				});
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Fetches all available news articles from the backend API.
	 *
	 * @return A list of all news articles.
	 * @throws IOException If an error occurs while performing the HTTP request or processing the response.
	 */
	public List<News> getAllNews() throws IOException {
		String url = BASE_URL + "/external";  // Assuming /external endpoint returns a list of news

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpGet request = new HttpGet(url);
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				String jsonResponse = EntityUtils.toString(response.getEntity());
				// Deserialize the JSON response into a List<News>
				return objectMapper.readValue(jsonResponse, new TypeReference<List<News>>() {});
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
