package com.nilga.newsparserapp.job;

import com.nilga.newsparserapp.service.NewsService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.nilga.newsparserapp.config.QuartzConfig;


/**
 * A Quartz job responsible for parsing news from an external source
 * and saving it to the database. This job is scheduled to run periodically
 * based on the trigger configuration in {@link QuartzConfig}.
 */
@Component
public class NewsParsingJob implements Job {

	@Autowired
	private NewsService newsService;

	/**
	 * This method is called when the Quartz scheduler triggers the job.
	 * It invokes the {@link NewsService#fetchAndSaveExternalNews()} method
	 * to fetch the latest news from an external API and save the news articles
	 * to the database.
	 *
	 * @param context the JobExecutionContext, which provides information
	 *                about the runtime environment of the job execution
	 */
	@Override
	public void execute(JobExecutionContext context) {
		newsService.fetchAndSaveExternalNews();
		System.out.println("News parsing job executed at " + java.time.LocalDateTime.now());
	}
}

