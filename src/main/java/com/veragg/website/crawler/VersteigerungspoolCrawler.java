package com.veragg.website.crawler;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.veragg.website.crawler.mapping.AuctionMapperService;
import com.veragg.website.crawler.model.VersteigerungspoolAuctionDTO;
import com.veragg.website.services.AuctionService;
import com.veragg.website.services.AuctionSourceService;

@Service("versteigerungspoolCrawler")
public class VersteigerungspoolCrawler extends AbstractCrawler<VersteigerungspoolAuctionDTO> {

    private static final String SOURCE_NAME = "VERSTEIGERUNGSPOOL";

    private static final String BASE_URL = "https://versteigerungspool.de/";

    private static final String START_URL = "https://versteigerungspool.de/amtsgerichte";
    private static final String LINK_CRAWL_REGEXP = "(?<=\\\")\\amtsgericht\\/.+suche.+(?=\\\"\\s)";
    private static final String LINK_EXTRACT_REGEXP = "(?<=\\\")\\/immobilie\\/.+(?=\\\"\\s)";

    private final Pattern LINK_CRAWL_PATTERN = Pattern.compile(LINK_CRAWL_REGEXP);
    private final Pattern LINK_EXTRACT_PATTERN = Pattern.compile(LINK_EXTRACT_REGEXP);

    @Autowired
    public VersteigerungspoolCrawler(AuctionMapperService<VersteigerungspoolAuctionDTO> mapperService, AuctionService auctionService, AuctionSourceService auctionSourceService,
            @Qualifier("versteigerungspoolParser") Parsing<VersteigerungspoolAuctionDTO> auctionParser) {
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
        return BASE_URL;
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
