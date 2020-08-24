package com.veragg.website.jobs;

import java.util.ArrayList;
import java.util.List;

import com.veragg.website.crawler.Crawling;

public class CrawlerJobConfigurations {

    private final List<CrawlerJobConfiguration> jobConfigurationList = new ArrayList<>();

    public List<CrawlerJobConfiguration> getJobConfigurations() {
        return jobConfigurationList;
    }

    public static CrawlerJobConfigurations config() {
        return new CrawlerJobConfigurations();
    }

    public CrawlerJobConfigurations job(CrawlerJobConfiguration job) {
        this.jobConfigurationList.add(job);
        return this;
    }

    public static class CrawlerJobConfiguration {

        private Class<? extends Crawling> crawlerClass;
        private String schedule;
        private String name;

        public Class<? extends Crawling> getCrawlerJobClass() {
            return crawlerClass;
        }

        public static CrawlerJobConfiguration from(Class<? extends Crawling> crawlerClass) {
            return new CrawlerJobConfiguration().crawlerClass(crawlerClass);
        }

        private CrawlerJobConfiguration crawlerClass(Class<? extends Crawling> crawlerClass) {
            this.crawlerClass = crawlerClass;
            return this;
        }

        public String getSchedule() {
            return schedule;
        }

        public CrawlerJobConfiguration schedule(String schedule) {
            this.schedule = schedule;
            return this;
        }

        public String getName() {
            return name;
        }

        public CrawlerJobConfiguration name(String name) {
            this.name = name;
            return this;
        }

        @Override
        public String toString() {
            return "JobConfig{" + "clazz=" + crawlerClass + ", schedule='" + schedule + '\'' + '}';
        }
    }
}
