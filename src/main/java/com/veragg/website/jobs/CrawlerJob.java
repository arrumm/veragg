package com.veragg.website.jobs;

import com.veragg.website.crawler.Crawling;

public interface CrawlerJob<T extends Crawling> {

    default String getId() {
        return this.getClass().getSimpleName();
    }

    T getCrawler();

    default void execute() {
        getCrawler().crawl();
    }

}
