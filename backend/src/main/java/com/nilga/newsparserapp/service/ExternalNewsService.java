package com.nilga.newsparserapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nilga.newsparserapp.dto.NewsApiResponse;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for fetching news articles from the external
 * NewsAPI.org service. This class makes an HTTP GET request to the NewsAPI
 * and parses the JSON response into a {@link NewsApiResponse} object.
 */
@Service
public class ExternalNewsService {

	@Value("${newsapi.key}")
	private String apiKey;

	private static final String NEWS_API_URL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=";
	private final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Fetches top headlines from NewsAPI.org.
	 *
	 * @return A {@link NewsApiResponse} object containing a list of news articles,
	 *         or {@code null} if the request failed.
	 */
	public NewsApiResponse getTopHeadlines() {
		String url = NEWS_API_URL + apiKey;
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			// Create the HTTP GET request
			HttpGet request = new HttpGet(url);

			// Execute the request and handle the response
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				int statusCode = response.getCode();

				// Check if the request was successful (HTTP 200)
				if (statusCode == 200) {
					String jsonResponse = EntityUtils.toString(response.getEntity());

					// Parse the JSON response into a NewsApiResponse object
					return objectMapper.readValue(jsonResponse, NewsApiResponse.class);
				} else {
					System.err.println("API returned status code: " + statusCode);
					return null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
