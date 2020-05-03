package com.veragg.website.crawler;

import java.util.regex.Pattern;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.veragg.website.domain.Auction;

import static java.util.Objects.isNull;

@Component
public class HanmarkCrawler extends AbstractCrawler implements ApplicationListener<ContextRefreshedEvent> {

    private final String START_URL = "https://www.hanmark.de/amtsgerichte.html";
    private final String LINK_CRAWL_REGEXP = "(https://www.hanmark.de/).+(.html)";
    private final String LINK_EXTRACT_REGEXP = "(https://www.hanmark.de/wertgutachten-)([0-9])+(.html)";

    private Pattern LINK_CRAWL_PATTERN;
    private Pattern LINK_EXTRACT_PATTERN;

    @Override
    Auction parseAuction(final String pageData) {
        //TODO: own rules to parse the page itself
        return null;
    }


    @Override
    String getStartURL() {
        return START_URL;
    }

    @Override
    int getMaxCrawlDepth() {
        return 2;
    }

    @Override
    Pattern getExtractLinkPattern() {
        if (isNull(LINK_EXTRACT_PATTERN)) {
            LINK_EXTRACT_PATTERN = Pattern.compile(LINK_EXTRACT_REGEXP);
        }
        return LINK_EXTRACT_PATTERN;
    }

    @Override
    Pattern getCrawlLInkPattern() {
        if (isNull(LINK_CRAWL_PATTERN)) {
            LINK_CRAWL_PATTERN = Pattern.compile(LINK_CRAWL_REGEXP);
        }
        return LINK_CRAWL_PATTERN;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        process();
    }
}
