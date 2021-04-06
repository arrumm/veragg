package com.veragg.website.crawler;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.veragg.website.crawler.mapping.AuctionMapperService;
import com.veragg.website.crawler.model.HanmarkAuctionDTO;
import com.veragg.website.services.AuctionService;
import com.veragg.website.services.AuctionSourceService;

@Service("hanmarkCrawler")
@Primary
public class HanmarkCrawler extends AbstractCrawler<HanmarkAuctionDTO> {

    private static final String SOURCE_NAME = "HANMARK";
    private static final String START_URL = "https://www.hanmark.de/amtsgerichte.html";
    private static final String LINK_CRAWL_REGEXP = "(https://www.hanmark.de/).+(.html)";
    private static final String LINK_EXTRACT_REGEXP = "(https://www.hanmark.de/wertgutachten-)([0-9])+(.html)";

    private final Pattern LINK_CRAWL_PATTERN = Pattern.compile(LINK_CRAWL_REGEXP);
    private final Pattern LINK_EXTRACT_PATTERN = Pattern.compile(LINK_EXTRACT_REGEXP);

    @Autowired
    public HanmarkCrawler(AuctionMapperService<HanmarkAuctionDTO> mapperService, AuctionService auctionService, AuctionSourceService auctionSourceService,
            @Qualifier("hanmarkParser") Parsing<HanmarkAuctionDTO> auctionParser) {
        this.auctionMapper = mapperService;
        this.auctionService = auctionService;
        this.auctionSourceService = auctionSourceService;
        this.auctionParser = auctionParser;
    }

    @Override
    String getStartURL() {
        return START_URL;
    }

    @Override
    String getBaseUrl() {
        return StringUtils.EMPTY;
    }

    @Override
    int getMaxCrawlDepth() {
        return 2;
    }

    @Override
    Pattern getAuctionUrlPattern() {
        return LINK_EXTRACT_PATTERN;
    }

    @Override
    Pattern getContainerPageUrlPattern() {
        return LINK_CRAWL_PATTERN;
    }

    @Override
    String getSourceName() {
        return SOURCE_NAME;
    }
}
