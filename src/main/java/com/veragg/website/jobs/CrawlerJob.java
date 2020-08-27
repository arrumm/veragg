package com.veragg.website.jobs;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import com.veragg.website.crawler.Crawling;

@EnableAsync
public interface CrawlerJob<T extends Crawling> {

    default String getId() {
        return this.getClass().getSimpleName();
    }

    T getCrawler();

    @Async
    default void execute() {
        getCrawler().crawl();
    }

}
