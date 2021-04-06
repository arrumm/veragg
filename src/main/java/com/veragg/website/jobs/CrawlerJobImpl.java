package com.veragg.website.jobs;

import com.veragg.website.crawler.Crawling;

public class CrawlerJobImpl implements CrawlerJob<Crawling> {

    private final Crawling crawler;

    public CrawlerJobImpl(Crawling crawler) {
        this.crawler = crawler;
    }

    @Override
    public Crawling getCrawler() {
        return crawler;
    }

}
