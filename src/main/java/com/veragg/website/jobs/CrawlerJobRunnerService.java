package com.veragg.website.jobs;

import com.veragg.website.crawler.Crawling;

public interface CrawlerJobRunnerService {

    void run(CrawlerJob<? extends Crawling> job);

}
