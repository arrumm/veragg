package com.veragg.website.jobs;

import org.springframework.stereotype.Service;

import com.veragg.website.crawler.Crawling;

@Service
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
