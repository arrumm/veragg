package com.veragg.website.jobs;

import org.springframework.stereotype.Component;

import com.veragg.website.crawler.AbstractCrawler;
import com.veragg.website.crawler.HanmarkCrawler;
import com.veragg.website.crawler.ZvgPortalCrawler;

import static com.veragg.website.jobs.CrawlerJobConfigurations.CrawlerJobConfiguration.from;

@Component
public class CrawlerJobsConfiguratorImpl implements CrawlerJobsConfigurator {
    @Override
    public CrawlerJobConfigurations configure() {
        //@formatter:off
        return new CrawlerJobConfigurations()
                .job(from(HanmarkCrawler.class).schedule("0 0 9 * * *").name("Hanmark crawler"))
                .job(from(ZvgPortalCrawler.class).schedule("0 30 9 * * *").name("ZVG-Portal crawler"))
                .job(from(AbstractCrawler.class).schedule("0 0 0 25 12 ?").name("abstract"));
        //@formatter:on
    }
}
