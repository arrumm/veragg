package com.veragg.website.jobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.veragg.website.crawler.Crawling;

@Service
public class CrawlerJobRunnerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerJobRunnerService.class);

    private final ApplicationContext applicationContext;
    private final TaskScheduler taskScheduler;

    @Autowired
    public CrawlerJobRunnerService(ApplicationContext applicationContext, TaskScheduler taskScheduler) {
        this.applicationContext = applicationContext;
        this.taskScheduler = taskScheduler;
    }

    @PostConstruct
    private void initJobs() {
        LOGGER.info("initJobs(): *** JOBS INITIALIZATION ***");
        final CrawlerJobConfigurations jobsListConfiguration = getJobsConfigurations();
        List<CrawlerJobWrapper<CrawlerJob<?>>> crawlerJobWrapperList = new ArrayList<>();

        for (final CrawlerJobConfigurations.CrawlerJobConfiguration jobConfiguration : jobsListConfiguration.getJobConfigurations()) {
            final Crawling crawler = crawlerBean(jobConfiguration.getCrawlerJobClass());
            final CrawlerJob<? extends Crawling> job = new CrawlerJobImpl(crawler);
            crawlerJobWrapperList.add(new CrawlerJobWrapper<>(job, jobConfiguration));
        }

        schedule(crawlerJobWrapperList);
    }

    private void schedule(final Collection<CrawlerJobWrapper<CrawlerJob<?>>> crawlerJobWrapperCollection) {
        for (final CrawlerJobWrapper<CrawlerJob<?>> crawlerJobWrapper : crawlerJobWrapperCollection) {
            LOGGER.info("schedule(job = [{}])", crawlerJobWrapper.getJob());
            taskScheduler.schedule(() -> crawlerJobWrapper.getJob().execute(), new CronTrigger(crawlerJobWrapper.getConfiguration().getSchedule()));
        }
    }

    private CrawlerJobConfigurations getJobsConfigurations() {
        try {
            return applicationContext.getBean(CrawlerJobsConfigurator.class).configure();
        } catch (final BeansException e) {
            return new CrawlerJobConfigurations();
        }
    }

    private Crawling crawlerBean(final Class<? extends Crawling> clazz) {
        LOGGER.info("jobBean(clazz = [{}])", clazz);
        return applicationContext.getBean(clazz);
    }

}
