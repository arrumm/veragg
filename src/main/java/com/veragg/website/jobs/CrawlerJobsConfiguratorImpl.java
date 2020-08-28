package com.veragg.website.jobs;

import org.springframework.stereotype.Component;

import com.veragg.website.crawler.AbstractCrawler;
import com.veragg.website.crawler.HanmarkCrawler;

import static com.veragg.website.jobs.CrawlerJobConfigurations.CrawlerJobConfiguration.from;

@Component
public class CrawlerJobsConfiguratorImpl implements CrawlerJobsConfigurator {
    @Override
    public CrawlerJobConfigurations configure() {
        //@formatter:off
        return new CrawlerJobConfigurations()
                .job(from(HanmarkCrawler.class).schedule("0 0 90 * * *").name("Hanmark crawler"))
                .job(from(AbstractCrawler.class).schedule("0 0 0 25 12 ?").name("abstract"));
        //@formatter:on
    }
}
