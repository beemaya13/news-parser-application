package com.nilga.newsparserapp.config;

import com.nilga.newsparserapp.job.NewsParsingJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up Quartz scheduling jobs and triggers.
 * This configuration defines a job for parsing news and schedules it to run
 * every 20 minutes using a CRON expression.
 */
@Configuration
public class QuartzConfig {

    /**
     * Creates a JobDetail object for the NewsParsingJob. This JobDetail object
     * represents the definition of the job and contains metadata such as its name
     * and identity.
     *
     * @return the JobDetail object for the NewsParsingJob
     */
    @Bean
    public JobDetail newsParsingJobDetail() {
        return JobBuilder.newJob(NewsParsingJob.class)
                .withIdentity("newsParsingJob")
                .storeDurably()
                .build();
    }

    /**
     * Creates a Trigger object that schedules the execution of the NewsParsingJob
     * using a CRON expression. The job will be triggered every 20 minutes.
     *
     * The CRON expression "0 0/20 * * * ?" means:
     * - "0" at second 0 of the minute
     * - "0/20" every 20th minute of the hour (e.g., 00:00, 00:20, 00:40)
     * - "*" every hour, day, month, etc.
     *
     * @return the Trigger object that triggers the job every 20 minutes
     */
    @Bean
    public Trigger newsParsingJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(newsParsingJobDetail())
                .withIdentity("newsParsingTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/20 * * * ?"))  // CRON expression for every 20 minutes
                .build();
    }
}
