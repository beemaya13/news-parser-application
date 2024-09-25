package com.nilga.newsparserapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsApiResponse {
	private String status;
	private int totalResults;
	private List<Article> articles;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Article {
		private String title;
		private String description;
		private String publishedAt;
	}
}
